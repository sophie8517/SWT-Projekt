package kickstart.statistic;

import kickstart.customer.Customer;
import kickstart.customer.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Collection;

@Controller
public class StatisticController {

//	@GetMapping("/statistic")
//	public String test(Model model) {
//		model.addAttribute("msg", "hello");
//
//		model.addAttribute("users", Arrays.asList("qinjiang", "kuangshen"));
//		return "statistic";
//	}

	@Autowired
	CustomerDao customerDao;

	@GetMapping("/statistic")
	public String list(Model model){
		Collection<Customer> customers = customerDao.getAll();
		model.addAttribute("customers", customers);
		return "statistic";
	}

}
