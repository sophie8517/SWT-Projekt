package kickstart.catalog;

import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.salespointframework.core.Currencies.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import kickstart.catalog.Item.ItemType;

@Controller
public class CatalogController {

	private final LotteryCatalog lotteryCatalog;
	private CustomerRepository customerRepository;



	public CatalogController(LotteryCatalog lotteryCatalog,CustomerRepository customerRepository){
		this.lotteryCatalog = lotteryCatalog;
		this.customerRepository = customerRepository;
	}



	@GetMapping("/zahlenlotterie")
	String ticketCatalog(Model model){
		model.addAttribute("ticketcatalog", lotteryCatalog.findByType(ItemType.TICKET));
		model.addAttribute("title", "catalog.ticket.title");

		return "3_catalog_num";
	}

	@GetMapping("/football")
	String footballCatalog(Model model, @LoggedIn Optional<UserAccount> userAccount){

		List<Item> foots = lotteryCatalog.findByType(ItemType.FOOTBALL);
		List<Item> result = new ArrayList<>();
		if(!userAccount.isEmpty()){
			Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());
			for(Item i:foots){
				Football f = (Football) i;
				if(f.getFootballBetsbyCustomer(c).isEmpty()){
					result.add(i);
				}
			}
		}
		else{
			result = foots;
		}


		model.addAttribute("footballcatalog", result);
		model.addAttribute("title", "catalog.football.title");

		return "2_catalog_foot";
	}

	@GetMapping("/contact")
	String catalog(Model model){
		model.addAttribute("title","kontakt.title");

		return "contact";
	}

	@PostMapping("/lottery/numbit")
	String bet_num(@RequestParam("pid")ProductIdentifier id, @RequestParam("zahl1") int zahl1, @RequestParam("zahl2") int zahl2, @RequestParam("zahl3")int zahl3, @RequestParam("zahl4")int zahl4, @RequestParam("zahl5")int zahl5, @RequestParam("zahl6")int zahl6,@RequestParam("dauer")int dauer, @LoggedIn Optional<UserAccount> userAccount){

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());

		List<Integer> nums = new ArrayList<>();
		Set<Integer> checker = new HashSet<>();
		checker.add(zahl1);
		checker.add(zahl2);
		checker.add(zahl3);
		checker.add(zahl4);
		checker.add(zahl5);
		checker.add(zahl6);

		if(checker.size() == 6){
			nums.addAll(checker);
		}
		else{

			return "wronginput";
		}
		LocalDate exp;
		if(dauer == 1){
			exp = LocalDate.now().plusDays(7);
		}
		if(dauer == 2){
			exp = LocalDate.now().plusMonths(1);
		}
		if(dauer == 3){
			exp = LocalDate.now().plusMonths(6);
		}
		else{
			exp = LocalDate.now().plusYears(1);
		}

		//add: check if all numbers are different
		NumberBet nb = new NumberBet(t, LocalDateTime.now(), Money.of(t.getPrice().getNumber(), EURO),c, nums);
		nb.setExpiration(exp);

		t.addBet(nb);
		//c.addNumberBet(nb);

		lotteryCatalog.save(t);
		//customerRepository.save(c);

		return "redirect:/";

	}

	@PostMapping("/lottery/wronginput")
	String wrong_input(@RequestParam("option1") int number){
		if(number == 0){
			return "redirect:/3_catalog_num.html";
		}
		else{
			return "redirect:/";
		}
	}

	@PostMapping("/lottery/footbit")
	String bet_foot(@RequestParam("pid")ProductIdentifier id, @RequestParam("fussballwette") int number,@RequestParam("einsatz") double einsatz ,@LoggedIn Optional<UserAccount> userAccount){

		Football foot = (Football) lotteryCatalog.findById(id).get();

		Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());
		System.out.println(einsatz);


		String  tip;

		if(number == 1){
			tip = "Gast gewinnt";
		}
		else if(number == 2){
			tip = "Heim gewinnt";
		}
		else{
			tip = "Unentschieden";
		}


		FootballBet f = new FootballBet(foot,LocalDateTime.now(), Money.of(einsatz, EURO),c, tip);
		foot.addBet(f);
		//c.addFootballBet(f);
		System.out.println(foot.getFootballBets());
		lotteryCatalog.save(foot);
		//customerRepository.save(c);



		return "redirect:/";

	}
	@GetMapping("/showbets")
	public String show_bets(Model model, @LoggedIn Optional<UserAccount> userAccount){
		Customer c = customerRepository.findCustomerByUserAccount(userAccount.get());
		Money balance = c.getBalance();
		Ticket t = (Ticket) lotteryCatalog.findByType(ItemType.TICKET).get(0);
		List<Item> foots = lotteryCatalog.findByType(ItemType.FOOTBALL);
		List<FootballBet> result = new ArrayList<>();
		for(Item i: foots){
			Football f = (Football) i;
			result.addAll(f.getFootballBetsbyCustomer(c));
		}

		model.addAttribute("balance", balance);
		model.addAttribute("numberBets", t.getNumberBetsbyCustomer(c));
		model.addAttribute("footballBets", result);

		return "customer_bets";
	}


}