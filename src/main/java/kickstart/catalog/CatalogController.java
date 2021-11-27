package kickstart.catalog;

import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.salespointframework.core.Currencies.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import kickstart.catalog.Item.ItemType;

@Controller
public class CatalogController {

	private final LotteryCatalog lotteryCatalog;
	private CustomerRepository customerRepository;



	public CatalogController(LotteryCatalog lotteryCatalog, CustomerRepository customerRepository){
		this.lotteryCatalog = lotteryCatalog;
		this.customerRepository = customerRepository;
	}



	@GetMapping("/zahlenlotterie")
	String ticketCatalog(Model model){

		List<Item> result = lotteryCatalog.findByType(ItemType.TICKET);
		Ticket t = (Ticket) result.get(0);

		//ziehungsdatum in ticket speichern
		//wenn heute uhrzeit und datum nach ziehungsdatum -> ziehungsdatum neu setzen
		LocalDate now = LocalDate.now();
		//wenn heute nicht der Tag der Ziehung ist
		if(!now.equals(t.getTimeLimit().toLocalDate())){
			while(t.getTimeLimit().toLocalDate().isBefore(now)){
				t.setTimeLimit(t.getTimeLimit().plusDays(7));
			}
		}
		lotteryCatalog.save(t);

		model.addAttribute("ticketcatalog", result);
		model.addAttribute("title", "catalog.ticket.title");

		return "3_catalog_num";
	}


	@GetMapping("/football")
	String footballCatalog(Model model, @LoggedIn Optional<UserAccount> userAccount){

		List<Item> foots = lotteryCatalog.findByType(ItemType.FOOTBALL);
		List<Item> result = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		//ein user kann auf ein fussballspiel nur eine wette abgeben

		if(!userAccount.isEmpty()){
			Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());
			for(Item i:foots){
				Football f = (Football) i;
				LocalDateTime spieltag_minus24h = LocalDateTime.of(f.getTimeLimit().toLocalDate().minusDays(1),LocalTime.of(0,0));
				if(f.getFootballBetsbyCustomer(c).isEmpty() && !now.isAfter(spieltag_minus24h)){
					result.add(i);
				}
			}
		} else{

			for(Item i: foots){
				if(now.isBefore(i.getTimeLimit().minusMinutes(5))){
					result.add(i);
				}
			}
		}


		model.addAttribute("footballcatalog", result);
		model.addAttribute("title", "catalog.football.title");


		return "2_catalog_foot";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/footballadmin")
	String footballCatalogAdmin(Model model){

		List<Item> foots = lotteryCatalog.findByType(ItemType.FOOTBALL);
		List<Item> result = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();


		for(Item i: foots){
			Football f = (Football) i;
			if(f.getErgebnis() == Ergebnis.LEER && now.isAfter(i.getTimeLimit())){
				result.add(i);
			}
		}

		model.addAttribute("footballcatalog", result);
		model.addAttribute("title", "catalog.football.admin.title");

		return "2_catalog_foot";
	}



	@PostMapping("/numbit")
	String bet_num(@RequestParam("pid")ProductIdentifier id, @RequestParam("zahl1") int zahl1,
				   @RequestParam("zahl2") int zahl2, @RequestParam("zahl3")int zahl3, @RequestParam("zahl4")int zahl4,
				   @RequestParam("zahl5")int zahl5, @RequestParam("zahl6")int zahl6,@RequestParam("zusatz")int zusatz,
				   @RequestParam("dauer")int dauer, @LoggedIn Optional<UserAccount> userAccount) {

		LocalDateTime now = LocalDateTime.now();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());
		Money money = c.getBalance();
		/*
		LocalDate basisdate;
		LocalDate dayofDraw;

		if(now.getDayOfWeek().getValue() == 7){
			dayofDraw = now.toLocalDate().plusDays(7);

		}else{
			dayofDraw = t.getTimeLimit().toLocalDate();
		}

		 */
		List<Integer> nums = new ArrayList<>();
		Set<Integer> checker = new HashSet<>();
		checker.add(zahl1);
		checker.add(zahl2);
		checker.add(zahl3);
		checker.add(zahl4);
		checker.add(zahl5);
		checker.add(zahl6);
		//checker.add(zusatz);

		if (checker.size() == 6 && !checker.contains(zusatz)) {
			nums.addAll(checker);

		} else {

			return "wronginput.html";
		}
		LocalDate exp;

		if (dauer == 1) {
			exp = LocalDate.now().plusDays(7);
		}
		if (dauer == 2) {
			exp = LocalDate.now().plusMonths(1);
		}
		if (dauer == 3) {
			exp = LocalDate.now().plusMonths(6);
		} else {
			exp = LocalDate.now().plusYears(1);
		}

		if (c.getBalance().isLessThan(t.getPrice())) {
			return "error.html"; //change to error page for not enough money
		}

		//add: check if all numbers are different
		NumberBet nb = new NumberBet(t, LocalDateTime.now(), Money.of(t.getPrice().getNumber(), EURO), c,
				LocalDateTime.of(exp, t.getTimeLimit().toLocalTime()), nums, zusatz);

		t.addBet(nb);

		//c.addNumberBet(nb);

		if (money.isLessThan(nb.getInset())) {
			return "error";
		} else {
			money = money.subtract(nb.getInset());
			c.setBalance(money);
			customerRepository.save(c);

			lotteryCatalog.save(t);
			//customerRepository.save(c);

			return "redirect:/";
		}
	}



	@PostMapping("/footbit")
	String bet_foot(@RequestParam("pid")ProductIdentifier id, @RequestParam("fussballwette") int number, @RequestParam("inset") double inset, @LoggedIn Optional<UserAccount> userAccount){

		LocalDateTime now = LocalDateTime.now();
		Football foot = (Football) lotteryCatalog.findById(id).get();
		Customer customer = customerRepository.findCustomerByUserAccount(userAccount.get());
		LocalDateTime spieltag_minus24h = LocalDateTime.of(foot.getTimeLimit().toLocalDate().minusDays(1),LocalTime.of(0,0));
		Money money = customer.getBalance();
		//System.out.println(inset);
		if(!now.isAfter(spieltag_minus24h)){
			Ergebnis  status;

			if(number == 1){
				status = Ergebnis.GASTSIEG;
			} else if(number == 2){
				status = Ergebnis.HEIMSIEG;
			} else{
				status = Ergebnis.UNENTSCHIEDEN;
			}


			FootballBet f = new FootballBet(foot,LocalDateTime.now(), Money.of(inset, EURO), customer, foot.getTimeLimit(),
					status);
			foot.addBet(f);

			if (money.isLessThan(f.getInset())) {
				return "error";
			} else {
				money = money.subtract(f.getInset());
				customer.setBalance(money);
				customerRepository.save(customer);
			}

			//customer.addFootballBet(f);
			System.out.println(foot.getFootballBets());
			lotteryCatalog.save(foot);
			//customerRepository.save(customer);

			return "redirect:/";
		}
		return "error.html";

	}

	@PostMapping("/wronginput")
	String wrong_input(){

		return "redirect:/";
	}
	@PostMapping("/timeup")
	String timeup(){

		return "redirect:/";
	}


}