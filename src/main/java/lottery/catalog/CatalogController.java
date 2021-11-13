package lottery.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.salespointframework.core.Currencies.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lottery.catalog.Item.ItemType;

@Controller
public class CatalogController {

	private final LotteryCatalog lotteryCatalog;



	public CatalogController(LotteryCatalog lotteryCatalog){
		this.lotteryCatalog = lotteryCatalog;
	}



	@GetMapping("/zahlenlotterie")
	String ticketCatalog(Model model){
		model.addAttribute("ticketcatalog", lotteryCatalog.findByType(ItemType.TICKET));
		model.addAttribute("title", "catalog.ticket.title");

		return "3_catalog_num";
	}

	@GetMapping("/football")
	String footballCatalog(Model model){
		model.addAttribute("footballcatalog", lotteryCatalog.findByType(ItemType.FOOTBALL));
		model.addAttribute("title", "catalog.football.title");

		return "2_catalog_foot";
	}

	@PostMapping("/lottery/numbit")
	String bet_num(@RequestParam("pid")ProductIdentifier id, @RequestParam("zahl1") int zahl1, @RequestParam("zahl2") int zahl2, @RequestParam("zahl3")int zahl3, @RequestParam("zahl4")int zahl4, @RequestParam("zahl5")int zahl5, @RequestParam("zahl6")int zahl6){

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();

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

		//add: check if all numbers are different
		t.addBet(new NumberBet(t, LocalDateTime.now(), Money.of(t.getPrice().getNumber(), EURO), nums));
		lotteryCatalog.save(t);

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
	String bet_foot(@RequestParam("pid")ProductIdentifier id, @RequestParam("fussballwette") int number){

		Football foot = (Football) lotteryCatalog.findById(id).get();


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


		FootballBet f = new FootballBet(foot,LocalDateTime.now(), Money.of(foot.getPrice().getNumber(), EURO), tip);
		foot.addBet(f);
		System.out.println(foot.getFootballBits());
		lotteryCatalog.save(foot);



		return "redirect:/";

	}


}