package kickstart.catalog;

import kickstart.catalog.LotteryCatalog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CatalogController {

	private LotteryCatalog lotteryCatalog;

	public CatalogController(LotteryCatalog lotteryCatalog){
		this.lotteryCatalog = lotteryCatalog;
	}

	@GetMapping("/zahlenlotterie")
	String ticketCatalog(Model model){
		model.addAttribute("ticketcatalog", lotteryCatalog.findByType("Ticket"));
		model.addAttribute("title", "ticketcatalog.title");

		return "3_catalog_num";
	}

	@GetMapping("/football")
	String footballCatalog(Model model){
		model.addAttribute("footballcatalog", lotteryCatalog.findByType("Football"));
		model.addAttribute("title", "football.title");

		return "2_catalog_foot";
	}

	@PostMapping("/lottery/numbit")
	String bet_num(@RequestParam("id") Ticket t, @RequestParam("zahl1") int zahl1, @RequestParam("zahl2") int zahl2, @RequestParam("zahl3")int zahl3, @RequestParam("zahl4")int zahl4, @RequestParam("zahl5")int zahl5, @RequestParam("zahl6")int zahl6){

		List<Integer> nums = new ArrayList<>();
		nums.add(zahl1);
		nums.add(zahl2);
		nums.add(zahl3);
		nums.add(zahl4);
		nums.add(zahl5);
		nums.add(zahl6);

		//add: check if all numbers are different
		t.addBit(new NumberBit(t, t.getNumberBits().size()+1, LocalDateTime.now(), t.getPrice2(), nums));
		lotteryCatalog.save(t);

		return "redirect:/3_catalog_num";

	}

	@PostMapping("/lottery/footbit")
	String bet_foot(@RequestParam("id") Football foot, @RequestParam("fussballwette") int number){

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

		FootballBit f = new FootballBit(foot, foot.getFootballBits().size() + 1, LocalDateTime.now(), foot.getPrice2(), tip);
		foot.addBit(f);
		lotteryCatalog.save(foot);

		return "redirect:/2_catalog_foot";

	}


}