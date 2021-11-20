package kickstart.statistic;

import kickstart.customer.CustomerManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	public String toBetPage() { return "statistic_bets"; }
}
