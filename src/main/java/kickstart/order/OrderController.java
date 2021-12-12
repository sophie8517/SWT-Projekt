package kickstart.order;
import kickstart.catalog.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;


import kickstart.customer.*;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.salespointframework.core.Currencies.EURO;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")

public class OrderController {

	private final CustomerManagement customerManagement;
	private CustomerRepository customerRepository;
	private LotteryCatalog lotteryCatalog;

	OrderController(CustomerManagement customerManagement, LotteryCatalog lotteryCatalog,
					CustomerRepository customerRepository){
		this.customerManagement = customerManagement;
		this.lotteryCatalog = lotteryCatalog;
		this.customerRepository = customerRepository;
	}

	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	@GetMapping("/customer_bets")
	public String viewBets(Model model,@LoggedIn Optional<UserAccount> userAccount){

		var customer = customerManagement.findByUserAccount(userAccount.get());


		List<Item> items= lotteryCatalog.findByType(Item.ItemType.TICKET);
		List<NumberBet> nums = new ArrayList<>();
		for(Item i: items){
			Ticket t = (Ticket) i;
			nums.addAll(t.getNumberBetsbyCustomer(customer));
		}
		List<Item> foots = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);
		List<FootballBet> result = new ArrayList<>();
		List<FootballBet> groupBets = new ArrayList<>();
		List<Group> customergroups = customer.getGroup();
		for(Item i: foots){
			Football f = (Football) i;
			result.addAll(f.getFootballBetsbyCustomer(customer));
			for(Group g: customergroups){
				groupBets.addAll(f.getGroupFootballBetsbyGroup(g.getGroupName()));
			}
		}

		model.addAttribute("numberBets", nums);
		model.addAttribute("footballBets", result);
		model.addAttribute("groupBets",groupBets);

		return "customer_bets";
	}

	@PostMapping("/raiseFootBet")
	public String raiseFootBet(@RequestParam("pid") ProductIdentifier id,
							   @RequestParam("betid")String bet_id, @RequestParam("newinsetfoot")double inset){

		LocalDateTime date = LocalDateTime.now();


		Money money = Money.of(inset, EURO);


		Football f = (Football) lotteryCatalog.findById(id).get();
		FootballBet bet = f.findbyBetId(bet_id);

		if(bet != null) {
			if(date.isBefore(f.getTimeLimit().minusMinutes(5))) {
				var customer = bet.getCustomer();
				Money diff = money.subtract(bet.getInset());
				if(customer.getBalance().isGreaterThanOrEqualTo(diff)){
					customer.setBalance(customer.getBalance().add(bet.getInset()));
					bet.setInset(money);
					customer.setBalance(customer.getBalance().subtract(bet.getInset()));
					customerRepository.save(customer);
					lotteryCatalog.save(f);


				}else{
					return "error";
				}

			}else{
				return "time_up.html";
			}
		}

		return "redirect:/customer_bets";
	}

	@GetMapping("/changeFoot")
	public String changeFoot(Model model, @RequestParam("item")ProductIdentifier id, @RequestParam("betid")String bid){
		Football f = (Football) lotteryCatalog.findById(id).get();
		FootballBet bet = f.findbyBetId(bid);

		model.addAttribute("footbet",bet);

		return "changeFootTip.html";
	}

	@PostMapping("/changeFootTip")
	public String changeFootbetTip(@RequestParam("pid")ProductIdentifier id,@RequestParam("betid")String bet_id,
								   @RequestParam("newtip")int number){

		LocalDateTime date = LocalDateTime.now();


		Ergebnis  status;

		if(number == 1){
			status = Ergebnis.GASTSIEG;
		} else if(number == 2){
			status = Ergebnis.HEIMSIEG;
		} else{
			status = Ergebnis.UNENTSCHIEDEN;
		}

		Football f = (Football) lotteryCatalog.findById(id).get();
		FootballBet bet = f.findbyBetId(bet_id);
		if(bet != null) {
			if(date.isBefore(f.getTimeLimit().minusMinutes(5))) {
				bet.setTippedStatus(status);
				lotteryCatalog.save(f);

				return "redirect:/customer_bets";
			}else{
				return "time_up.html";
			}
		}


		return "redirect:/customer_bets";
	}

	@PostMapping("/raiseNumBet")
	public String raiseNumBet(Model model, @RequestParam("pid") ProductIdentifier id,@RequestParam("betid")String bet_id,
							  @RequestParam("newinset")double inset){

		Money money = Money.of(inset, EURO);
		LocalDateTime date = LocalDateTime.now();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		NumberBet bet = t.findbyBetId(bet_id);
		if(bet != null) {
			//erhöhen möglich, wenn:
			// bis 5 Minuten vor der Ziehung
			//die Ziehung dieser Woche abgeschlossen ist
			//man am Ziehungstag den Lottoschein ausgefüllt hat -> d.h. er ist für diese Ziehung noch nicht gültig

			if (date.isBefore(t.getTimeLimit().minusMinutes(5))
					||t.getCheckEvaluation().contains(t.getTimeLimit().toLocalDate())
					|| bet.getDate().toLocalDate().isEqual(t.getTimeLimit().toLocalDate())) {
				var customer = bet.getCustomer();
				Money diff = money.subtract(bet.getInset());
				if(customer.getBalance().isGreaterThanOrEqualTo(diff)){
					customer.setBalance(customer.getBalance().add(bet.getInset()));
					bet.setInset(money);
					customer.setBalance(customer.getBalance().subtract(bet.getInset()));
					customerRepository.save(customer);
					lotteryCatalog.save(t);
				}else{
					return "error";
				}

			}else{
				return "time_up.html";
			}
		}


		return "redirect:/customer_bets";
	}

	@GetMapping("/changeNums")
	public String changeNums(Model model, @RequestParam("item")ProductIdentifier id, @RequestParam("betid")String bet_id){

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		NumberBet bet = t.findbyBetId(bet_id);

		model.addAttribute("numbet", bet);
		return "changeNumTip.html";
	}

	@PostMapping("/changeNumbetTip")
	public String changeNumbetTip(@RequestParam("pid") ProductIdentifier id,@RequestParam("betid")String bet_id,
								  @RequestParam("zahl1") int zahl1, @RequestParam("zahl2") int zahl2,
								  @RequestParam("zahl3")int zahl3, @RequestParam("zahl4")int zahl4,
								  @RequestParam("zahl5")int zahl5, @RequestParam("zahl6")int zahl6,
								  @RequestParam("zusatz")int zusatz){

		LocalDateTime date = LocalDateTime.now();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		NumberBet bet = t.findbyBetId(bet_id);
		if(bet != null) {
			if (date.isBefore(t.getTimeLimit().minusMinutes(5))) {
				List<Integer> nums = new ArrayList<>();
				Set<Integer> checker = new HashSet<>();
				checker.add(zahl1);
				checker.add(zahl2);
				checker.add(zahl3);
				checker.add(zahl4);
				checker.add(zahl5);
				checker.add(zahl6);


				if(checker.size() == 6 ){
					nums.addAll(checker);

				} else{

					return "wronginput.html";
				}
				bet.setNumbers(nums);
				bet.setAdditionalNum(zusatz);

				lotteryCatalog.save(t);
			}else{
				return "time_up.html";
			}
		}


		return "redirect:/customer_bets";

	}


	@PostMapping("/removeNumberBets")
	public String removeNumberBets(@RequestParam("ticketid")ProductIdentifier id, @RequestParam("numbetid")String bid){
		LocalDateTime date = LocalDateTime.now();
		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		NumberBet numberBetRemove = t.findbyBetId(bid);
		Customer customer = numberBetRemove.getCustomer();
		LocalDateTime time = t.getTimeLimit();
		if (date.isBefore(time.minusMinutes(5))
				||t.getCheckEvaluation().contains(time.toLocalDate())
				|| numberBetRemove.getDate().toLocalDate().isEqual(time.toLocalDate())){

			if(numberBetRemove.getStatus().equals(Status.OPEN)){
				Money oldbalance = customer.getBalance();
				Money newbalance =oldbalance.add(numberBetRemove.getInset());
				customer.setBalance(newbalance);
				customerRepository.save(customer);
			}
			t.removeBet(numberBetRemove);
			lotteryCatalog.save(t);


			return "redirect:/customer_bets";
		}
		return "time_up.html";

	}

	@PostMapping("/removeFootballBets")
	public String removeFootballBets(@RequestParam("itemid")ProductIdentifier id, @RequestParam("betid")String bid){

		LocalDateTime date = LocalDateTime.now();
		Football football = (Football) lotteryCatalog.findById(id).get();
		FootballBet footballBetRemove = football.findbyBetId(bid);
		LocalDateTime time = football.getTimeLimit().minusMinutes(5);
		Customer customer = footballBetRemove.getCustomer();
		if (date.isBefore(time) || !football.getErgebnis().equals(Ergebnis.LEER)){

			if(footballBetRemove.getStatus().equals(Status.OPEN)){
				Money oldbalance = customer.getBalance();
				Money newbalance =oldbalance.add(footballBetRemove.getInset());
				customer.setBalance(newbalance);
				customerRepository.save(customer);
			}
			if(footballBetRemove.getGroupName().equals("")){
				football.removeBet(footballBetRemove);
			}else{
				football.removeGroupBet(footballBetRemove);
			}


			lotteryCatalog.save(football);


			return "redirect:/customer_bets";
		}
		return "time_up.html";

	}

	@PostMapping("/order")
	String income(Model model, LocalDateTime date, FootballBet footballBet, Customer customer, Match match){
		LocalDateTime temp = date.plusMinutes(90);
		LocalDateTime time = LocalDateTime.now();

		Ticket t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);


		List<Item> foots = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);
		List<FootballBet> result = new ArrayList<>();
		for(Item i: foots){
			Football f = (Football) i;
			result.addAll(f.getFootballBetsbyCustomer(customer));
		}


		if (temp.equals(time) || time.isAfter(temp)) {
			//customer.getBalance().add(Money.of(15, EURO));
			for (FootballBet fakeBet : result) {
				if (fakeBet.equals(footballBet) && fakeBet.getTip().equals(match.result())) {

						Money income = customer.getBalance().add(Money.of(15, EURO));
						customer.setBalance(income);

				}
			}
			for(NumberBet nb: t.getNumberBetsbyCustomer(customer)){
				if(nb.getStatus()==Status.WIN){
					Money income = customer.getBalance().add(Money.of(t.getPrice2(),EURO));
					customer.setBalance(income);
				}
			}
		}

		model.addAttribute("income", customer.getBalance());
		return "redirect:/";

	}

	@PreAuthorize("hasRole('ADMIN')")
	String result(Model model, Football football, int scoreHost, int scoreGuest){
		football.getHost().setScore(scoreHost);
		football.getGuest().setScore(scoreGuest);
		Match match = new Match(football.getHost(), football.getGuest());
		model.addAttribute("result", match.result());
		return "redirect:/";
	}



}
