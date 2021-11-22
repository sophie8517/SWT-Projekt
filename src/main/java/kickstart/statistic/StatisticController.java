package kickstart.statistic;

import kickstart.catalog.*;
import kickstart.customer.CustomerManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticController {

	private final CustomerManagement customerManagement;
	private LotteryCatalog lotteryCatalog;

	StatisticController(CustomerManagement customerManagement, LotteryCatalog lotteryCatalog){
		this.customerManagement = customerManagement;
		this.lotteryCatalog = lotteryCatalog;
	}

	@GetMapping("/statistic")
	public String statistic(Model model){
		model.addAttribute("customers", customerManagement.findAllCustomers());
		return "statistic";
	}

	//zeigt Einkommen und Verluste an
	@GetMapping("/statistic_bets")
	public String toBetPage(Model model, @RequestParam("id") long id) {
		var customer = customerManagement.findByCustomerId(id);

		Ticket t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);

		List<Item> foots = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);
		List<FootballBet> result = new ArrayList<>();
		for(Item i: foots){
			Football f = (Football) i;
			result.addAll(f.getFootballBetsbyCustomer(customer));
		}

		model.addAttribute("football", result.toString());
		model.addAttribute("number", t.getNumberBetsbyCustomer(customer).toString());

		return "statistic_bets";
	}
}