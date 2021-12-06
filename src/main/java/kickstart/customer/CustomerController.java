package kickstart.customer;

import com.mysema.commons.lang.Assert;
import static org.salespointframework.core.Currencies.*;


import org.h2.engine.User;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController{
	private final CustomerManagement customerManagement;

	public CustomerController(CustomerManagement customerManagement) {
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");
		this.customerManagement = customerManagement;
	}

	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result, RedirectAttributes redirAttrs) {

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

		if(customerManagement.findByEmail(form.getEmail()).isPresent()){
			redirAttrs.addFlashAttribute("message", "Name you entered is already exist!");
			return "redirect:/register";
		}

		customerManagement.createCustomer(form);
		return "redirect:/";
	}

	@GetMapping("/register")
	String register() {
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
				//model.addAttribute("profile", new Profile(customer.getUserAccount().getFirstname(),
				 customer.getUserAccount().getLastname(),customer.getUserAccount().getEmail()));
				model.addAttribute("profile", new Profile(customer.getUserAccount().getUsername(),
				 customer.getUserAccount().getEmail(), customer.getUserAccount().getEmail()));
				System.out.println(new Profile(customer.getUserAccount().getFirstname(),
				customer.getUserAccount().getLastname(),customer.getUserAccount().getEmail()));
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

//	@PostMapping("/registerGroup")
//	String createNewGroup(@Valid RegistrationForm form, Errors result) {
//
//		if (result.hasErrors()) {
//			return "register";
//		}
//
//		customerManagement.createCustomer(form);
//		return "redirect:/";
//	}
//
//	@GetMapping("/registerGroup")
//	public String create(){
//		return "group_create";
//	}

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

	@PostMapping("/group/exit")
	public String exit(@RequestParam("groupName") String groupName, @LoggedIn Optional<UserAccount> userAccount, RedirectAttributes redir){
		var group = customerManagement.findByGroupName(groupName);
		var customer = customerManagement.findByUserAccount(userAccount.get());

		if (!group.contains(customer)) {
			redir.addFlashAttribute("message", "Customer doesn't exist!");
			return "redirect:/group";
		}

		customerManagement.removeMemberOfGroup(customer, group);

		if (group.getMembers().isEmpty()) {
			customerManagement.deleteGroup(group);
		}

		return "redirect:/group";
	}


	@GetMapping("/group")
	public String Group(Model model, @LoggedIn Optional<UserAccount> userAccount){

		var customer = customerManagement.findByUserAccount(userAccount.get());

		List<Group> groups = customer.getGroup();

		model.addAttribute("groups", groups);

		return "group";
	}

	@GetMapping("/group_join")
	public String join(){
		return "group_join";
	}

	@PostMapping("/group_join")
	public String joinGroup(@RequestParam("name") String name, @RequestParam("password") String password,
							@LoggedIn Optional<UserAccount> userAccount, RedirectAttributes redir){
		Assert.notNull(name, "name must not be null");
		Assert.notNull(password, "password must not be null");
		var customer = customerManagement.findByUserAccount(userAccount.get());
		var group = customerManagement.findByGroupName(name);
		System.out.println(group);

		System.out.println(group.getPassword() + " and " + password);
		if(!group.getPassword().equals(password)){
			redir.addFlashAttribute("message", "Password doesn't match!");
			return "redirect:/group_join";
		}

		if(group.contains(customer)) {
			redir.addFlashAttribute("message", "Customer is already in the Group!");
			return "redirect:/group_join";
		}

		customerManagement.addMemberToGroup(customer, group, password);

		return "redirect:/group";
	}

	@GetMapping("/group_create")
	public String createGroupPage(Model model){
		return "group_create";
	}

	@PostMapping("/group_create")
	public String createGroup(@RequestParam("groupName") String groupName, @LoggedIn Optional<UserAccount> userAccount,
<<<<<<< HEAD
							  RedirectAttributes redirAttrs){

		Assert.notNull(groupName, "Registration form must not be null!");

		if (customerManagement.findByGroupName(groupName) != null) {
			redirAttrs.addFlashAttribute("message", "Group name already exists, please give another name");
			return "redirect:/group_create";
		}
=======
							  RedirectAttributes redir){

		Assert.notNull(groupName, "groupName must not be null!");

		if (customerManagement.findByGroupName(groupName) != null) {
			redir.addFlashAttribute("message", "Group name already exists, please give an another name");
			return "redirect:/group_create";
		}

>>>>>>> test

		var customer = customerManagement.findByUserAccount(userAccount.get());
		customerManagement.createGroup(groupName, customer);
		return "redirect:/group";
	}


	@PostMapping("/close")
	String close(){
		return "redirect:/group";
	}


}
