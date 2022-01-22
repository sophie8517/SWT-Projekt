package kickstart.catalog;

import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import kickstart.customer.Group;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		//Ticket t = (Ticket) result.get(0);

		//ziehungsdatum in ticket speichern
		//wenn heute uhrzeit und datum nach ziehungsdatum -> ziehungsdatum neu setzen
		LocalDate now = LocalDate.now();
		//wenn heute nicht der Tag der Ziehung ist
		for(Item t: result){
			if(!now.equals(t.getTimeLimit().toLocalDate())){
				while(t.getTimeLimit().toLocalDate().isBefore(now)){
					t.setTimeLimit(t.getTimeLimit().plusDays(7));
				}
				lotteryCatalog.save(t);
			}
		}




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
				if(now.isBefore(i.getTimeLimit())){
					result.add(i);
				}
			}
		}


		model.addAttribute("footballcatalog", result);
		model.addAttribute("title", "catalog.football.title");


		return "2_catalog_foot";
	}

	@GetMapping("/footballgroup")
	String footballCatalogGroup(Model model, @LoggedIn Optional<UserAccount> userAccount){

		List<Item> foots = lotteryCatalog.findByType(ItemType.FOOTBALL);
		List<Item> result = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		//ein user kann auf ein fussballspiel nur eine wette abgeben

		if(!userAccount.isEmpty()){

			for(Item f:foots){

				LocalDateTime spieltag_minus24h = LocalDateTime.of(f.getTimeLimit().toLocalDate().minusDays(1),LocalTime.of(0,0));
				if(!now.isAfter(spieltag_minus24h)){
					result.add(f);
				}
			}
		} else{

			for(Item i: foots){
				if(now.isBefore(i.getTimeLimit())){
					result.add(i);
				}
			}
		}


		model.addAttribute("footballcatalog", result);
		model.addAttribute("title", "catalog.football.title");


		return "catalog_group";
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
		Money price = Money.of(t.getPrice2(),EURO);

		List<Integer> nums = new ArrayList<>();
		Set<Integer> checker = new HashSet<>();
		checker.add(zahl1);
		checker.add(zahl2);
		checker.add(zahl3);
		checker.add(zahl4);
		checker.add(zahl5);
		checker.add(zahl6);

		if (checker.size() == 6 ) {
			nums.addAll(checker);
			Collections.sort(nums);

		} else {

			return "wronginput.html";
		}
		LocalDate exp;

		if (dauer == 1) {
			exp = now.toLocalDate().plusDays(7);
		}else if (dauer == 2) {
			exp = now.toLocalDate().plusMonths(1);
		}else if (dauer == 3) {
			exp = now.toLocalDate().plusMonths(6);
		}else {
			exp = now.toLocalDate().plusYears(1);
		}


		if (money.isLessThan(price)) {
			return "not_enough_money";
		} else {
			money = money.subtract(price);
			c.setBalance(money);
			customerRepository.save(c);
			NumberBet nb = new NumberBet(t, now, price, c,
					LocalDateTime.of(exp, t.getTimeLimit().toLocalTime()), nums, zusatz);

			t.addBet(nb);
			lotteryCatalog.save(t);
			//NumberBet sophie = new NumberBet(t,LocalDateTime.of(2022,1,8,17,15),Money.of(12,EURO),c,LocalDateTime.of(2022,1,15,17,15),nums,9);
			//t.addBet(sophie);

			//lotteryCatalog.save(t);
			//System.out.println(t.getNumberBits());
			//customerRepository.save(c);
			//System.out.println(t.getNumberBits());

			return "redirect:/";
		}



	}

	@GetMapping("/footballcatalog")
	public String catalog(){
		return "catalog";
	}

	@PostMapping("/footbit")
	String bet_foot(@RequestParam("pid")ProductIdentifier id, @RequestParam("fussballwette") int number,
					@RequestParam("inset") double inset, @LoggedIn Optional<UserAccount> userAccount){

		LocalDateTime now = LocalDateTime.now();
		Football foot = (Football) lotteryCatalog.findById(id).get();
		Customer customer = customerRepository.findCustomerByUserAccount(userAccount.get());
		LocalDateTime spieltag_minus24h = LocalDateTime.of(foot.getTimeLimit().toLocalDate().minusDays(1),LocalTime.of(0,0));
		Money money = customer.getBalance();
		Money insetMoney = Money.of(inset,EURO);
		//System.out.println(inset);
		if(now.isBefore(spieltag_minus24h)){
			Ergebnis  status;

			if(number == 1){
				status = Ergebnis.HEIMSIEG;
			} else if(number == 2){
				status = Ergebnis.GASTSIEG;
			} else{
				status = Ergebnis.UNENTSCHIEDEN;
			}




			if(money.isLessThan(insetMoney)){
				return "not_enough_money";
			} else{
				money = money.subtract(insetMoney);
				customer.setBalance(money);
				customerRepository.save(customer);
				FootballBet f = new FootballBet(foot,LocalDateTime.now(), Money.of(inset, EURO), customer,
						foot.getTimeLimit(), status);
				foot.addBet(f);
				lotteryCatalog.save(foot);
				return "redirect:/";
			}

		}
		return "time_up.html";

	}

	private String checkAddGroupBet(int number, Money money, Money insetMoney,Customer customer, Football foot,
								   String groupName){
		String result;

		Ergebnis  status;

		if(number == 1){
			status = Ergebnis.HEIMSIEG;
		} else if(number == 2){
			status = Ergebnis.GASTSIEG;
		} else{
			status = Ergebnis.UNENTSCHIEDEN;
		}




		if(money.isLessThan(insetMoney)){
			result = "not_enough_money";
		} else{
			money = money.subtract(insetMoney);
			customer.setBalance(money);
			customerRepository.save(customer);
			FootballBet f = new FootballBet(foot,LocalDateTime.now(), insetMoney, customer,
					foot.getTimeLimit(), status);
			f.setGroupName(groupName);
			foot.addGroupBet(f);
			lotteryCatalog.save(foot);
			result = "redirect:/";
		}


		return result;
	}

	@PostMapping("/footbitgroup")
	public String bet_foot_group(@RequestParam("pid")ProductIdentifier id, @RequestParam("fussballwette") int number,
					@RequestParam("inset") double inset, @RequestParam("groupfoot")String groupName,
					@LoggedIn Optional<UserAccount> userAccount, RedirectAttributes redirAttrs){

		LocalDateTime now = LocalDateTime.now();
		Football foot = (Football) lotteryCatalog.findById(id).get();
		Customer customer = customerRepository.findCustomerByUserAccount(userAccount.get());
		List<Group> customergroups = customer.getGroup();
		LocalDateTime spieltag_minus24h = LocalDateTime.of(foot.getTimeLimit().toLocalDate().minusDays(1),LocalTime.of(0,0));
		Money money = customer.getBalance();
		Money insetMoney = Money.of(inset,EURO);
		//System.out.println(inset);
		int found = 0;
		for(Group g:customergroups){
			if(g.getGroupName().equals(groupName)){
				found =1;
				break;
			}
		}
		if(found == 0){
			redirAttrs.addFlashAttribute("message1","Sie sind nicht teil dieser Gruppe!");
			return "redirect:/footballgroup";
		}

		if(!foot.getGroupFootballBetsbyGroup(groupName).isEmpty()){
			redirAttrs.addFlashAttribute("message","Für dieses Spiel hat die Gruppe schon eine Wette abgegeben.");
			return "redirect:/footballgroup";
		}

		String result;

		if(now.isBefore(spieltag_minus24h)){
			result = checkAddGroupBet(number,money,insetMoney,customer,foot,groupName);
		}else {
			result = "time_up.html";
		}
		return result;

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