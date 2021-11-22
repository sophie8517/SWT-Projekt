package kickstart.customer;

import com.mysema.commons.lang.Assert;
import static org.salespointframework.core.Currencies.*;


import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class CustomerController{
	private final CustomerManagement customerManagement;

	public CustomerController(CustomerManagement customerManagement) {
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");
		this.customerManagement = customerManagement;
	}

	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result) {

		System.out.println(form.getPassword());
		System.out.println("passwort2:"+ form.getPasswordCheck());
		System.out.println(result.hasErrors());
		System.out.println(form.check());

		if (result.hasErrors()) {
			return "register";
		}
		if(!form.check()){
			return "register";
		}
		customerManagement.createCustomer(form);
		return "redirect:/";
	}

	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	@GetMapping("/profile")
	String getProfile(Model model, @LoggedIn Optional<UserAccount> userAccount) {
	/*	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("Authenticate " + authentication.getName());
		String username = authentication.getName();
		for (Customer customer : customerManagement.findAllCustomers()){
			System.out.println(customer.getUserAccount().getUsername());
			if (username.equals(customer.getUserAccount().getUsername())){
				//model.addAttribute("profile", new Profile(customer.getUserAccount().getFirstname(), customer.getUserAccount().getLastname(),customer.getUserAccount().getEmail()));
				model.addAttribute("profile", new Profile(customer.getUserAccount().getUsername(), customer.getUserAccount().getEmail(), customer.getUserAccount().getEmail()));
				System.out.println(new Profile(customer.getUserAccount().getFirstname(),customer.getUserAccount().getLastname(),customer.getUserAccount().getEmail()));
			}
		}

	 */
		model.addAttribute("firstname", userAccount.get().getFirstname());
		model.addAttribute("lastname", userAccount.get().getLastname());
		model.addAttribute("email", userAccount.get().getEmail());
		return "meinProfil";
	}


	@GetMapping("/customers")
	@PreAuthorize("hasRole('ADMIN')")
	String customers(Model model) {
		model.addAttribute("customerList", customerManagement.findAllCustomers());
		return "customers";
	}

	@PostMapping("/registerGroup")
	String createNewGroup(@Valid RegistrationForm form, Errors result) {

		if (result.hasErrors()) {
			return "register";
		}

		customerManagement.createCustomer(form);
		return "redirect:/";
	}

	@GetMapping("/registerGroup")
	public String create(Model model, RegistrationForm form){
		return "redirect:/";
	}

	@PostMapping("/balance/charge")
	public String charge(@RequestParam("money") double money, @LoggedIn Optional<UserAccount> userAccount){
		//customerManagement.charge(Money.of(balance, EURO),customerManagement.findByCustomerId(customerId));

		var customer = customerManagement.findByUserAccount(userAccount.get());
		customerManagement.charge(Money.of(money, EURO), customer);
		return "redirect:/balance";
	}

	@GetMapping("/balance")
	public String viewBalance(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		var customer = customerManagement.findByUserAccount(userAccount.get());
		model.addAttribute("firstname", customer.getUserAccount().getFirstname());
		model.addAttribute("lastname", customer.getUserAccount().getLastname());
		model.addAttribute("email", customer.getUserAccount().getEmail());
		model.addAttribute("balance", customer.getBalance());

		return "balance";
	}

	@PostMapping("/addMember")
	public String addMember(long customerId, long groupId, String password){
		 customerManagement.addMemberToGroup(
		 		customerManagement.findByCustomerId(customerId), customerManagement.findByGroupId(groupId),password);
		 return "redirect:/";
	}

	@PostMapping("/removeMember")
	public String removeMember(long customerId, long groupId){
		customerManagement.removeMemberOfGroup(
				customerManagement.findByCustomerId(customerId), customerManagement.findByGroupId(groupId));
		return "redirect:/";
	}

	@PostMapping("/deleteGroup")
	public String deleteGroup(Model model, long groupId){
		customerManagement.deleteGroup(customerManagement.findByGroupId(groupId));
		model.addAttribute("group",customerManagement.findAllGroups());
		return "redirect:/";
	}
}
