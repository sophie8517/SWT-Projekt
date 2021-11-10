package customer;

import com.mysema.commons.lang.Assert;
import static org.salespointframework.core.Currencies.*;


import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

public class CustomerController{
	private final CustomerManagement customerManagement;

	public CustomerController(CustomerManagement customerManagement) {
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");
		this.customerManagement = customerManagement;
	}

	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}
		customerManagement.createCustomer(form);
		return "redirect:/";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/customers")
	@PreAuthorize("hasRole('ADMIN')")
	String customers(Model model) {

		model.addAttribute("customerList", customerManagement.findAllCustomers());
		return "customers";
	}

	@PostMapping("/register")
	String createNewGroup(@Valid RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}

		customerManagement.createCustomer(form);
		return "redirect:/";
	}

	public String create(Model model, RegistrationForm form){
		return "redirect:/";
	}

	public String charge(double balance, long customerId){
		customerManagement.charge(Money.of(balance, EURO),customerManagement.findByCustomerId(customerId));
		return "redirect:/";
	}

	public String addMember(long customerId, long groupId, String password){
		 customerManagement.addMemberToGroup(
		 		customerManagement.findByCustomerId(customerId), customerManagement.findByGroupId(groupId),password);
		 return "redirect:/";
	}

	public String removeMember(long customerId, long groupId){
		customerManagement.removeMemberOfGroup(
				customerManagement.findByCustomerId(customerId), customerManagement.findByGroupId(groupId));
		return "redirect:/";
	}

	public String deleteGroup(Model model, long groupId){
		customerManagement.deleteGroup(customerManagement.findByGroupId(groupId));
		model.addAttribute("group",customerManagement.findAllGroups());
		return "redirect:/";
	}
}
