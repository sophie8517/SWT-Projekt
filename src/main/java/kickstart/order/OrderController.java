package kickstart.order;
import kickstart.catalog.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import kickstart.customer.*;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import static org.salespointframework.core.Currencies.EURO;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")

public class OrderController {

	private final CustomerManagement customerManagement;
	private LotteryCatalog lotteryCatalog;

	OrderController(CustomerManagement customerManagement, LotteryCatalog lotteryCatalog){
		this.customerManagement = customerManagement;
		this.lotteryCatalog = lotteryCatalog;
	}

	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
	@GetMapping("/customer_bets")
	public String viewBets(Model model,@LoggedIn Optional<UserAccount> userAccount){

		var customer = customerManagement.findByUserAccount(userAccount.get());


		Ticket t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		List<Item> foots = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);
		List<FootballBet> result = new ArrayList<>();
		for(Item i: foots){
			Football f = (Football) i;
			result.addAll(f.getFootballBetsbyCustomer(customer));
		}

		model.addAttribute("numberBets", t.getNumberBetsbyCustomer(customer));
		model.addAttribute("footballBets", result);

		return "customer_bets";
	}

	@PostMapping("/raiseFootBet")
	public String raiseFootBet(Model model, @RequestParam("pid") ProductIdentifier id,
							   @RequestParam("betid")long bet_id, @RequestParam("newinset")double inset){
		/*
		LocalDateTime date = LocalDateTime.now();
		LocalDateTime time = date.plusMinutes(5);
		if (date.isAfter(time) && money.isLessThan(bet.getInset())){
			throw new IllegalStateException("Inset too low or change too late.");
		}
		 */
		LocalDateTime date = LocalDateTime.now();


		Money money = Money.of(inset, EURO);

		Football f = (Football) lotteryCatalog.findById(id).get();
		FootballBet bet = f.findbyBetId(bet_id);

		if(bet != null) {
			if(date.isBefore(f.getTimeLimit().minusMinutes(5))) {
				bet.setInset(money);
				lotteryCatalog.save(f);
				//model.addAttribute("raisedMoney",bet.getInset());
				//return "redirect:/";
			}
		}

		return "redirect:/";
	}

	@PostMapping("/raiseNumBet")
	public String raiseNumBet(Model model, @RequestParam("pid") ProductIdentifier id,@RequestParam("betid")long bet_id,
							  @RequestParam("newinset")double inset){

		Money money = Money.of(inset, EURO);
		LocalDateTime date = LocalDateTime.now();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		NumberBet bet = t.findbyBetId(bet_id);
		if(bet != null) {
			if (date.isBefore(t.getTimeLimit().minusMinutes(5))) {
				bet.setInset(money);
				lotteryCatalog.save(t);
			}
		}


		return "redirect:/";
	}


	@PostMapping("/removeNumberBets")
	public String removeNumberBets(Model model, Customer customer, Bet numberBetRemove, LocalDateTime date){

		if (!date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){

			Ticket t = (Ticket) numberBetRemove.getItem();
			NumberBet b = (NumberBet) numberBetRemove;
			t.removeBet(b);
			lotteryCatalog.save(t);

			model.addAttribute("removeNumberBets", t.getNumberBetsbyCustomer(customer));
			return "redirect:/";
		}
		throw new IllegalStateException("Cancel possible at most 5 minutes before the draw.");

	}

	@PostMapping("/removeFootballBets")
	public String removeFootballBets(Model model, Customer customer, FootballBet footballBetRemove, LocalDateTime date){
		LocalDateTime time = date.plusMinutes(5);
		if (date.isBefore(time)){

			Football football = (Football) footballBetRemove.getItem();
			football.removeBet(footballBetRemove);

			List<Item> foots = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);
			List<FootballBet> result = new ArrayList<>();
			for(Item i: foots){
				Football f = (Football) i;
				result.addAll(f.getFootballBetsbyCustomer(customer));
			}
			lotteryCatalog.save(football);

			model.addAttribute("removeFootballBets", result);
			return "redirect:/";
		}
		throw new IllegalStateException("Cancel possible at most 5 minutes before the match.");

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
