package kickstart.statistic;

import kickstart.customer.CustomerManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatisticController {

	private final CustomerManagement customerManagement;

	StatisticController(CustomerManagement customerManagement){
		this.customerManagement = customerManagement;
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
		model.addAttribute("football", customer.getAllFootballBetList().toString());
		model.addAttribute("number", customer.getAllNumberBetList().toString());

		return "statistic_bets";
	}
}