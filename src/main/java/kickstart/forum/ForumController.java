package kickstart.forum;


import java.util.Optional;

import javax.validation.Valid;

import kickstart.customer.CustomerManagement;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.salespointframework.core.Currencies.EURO;

@Controller
class ForumController {

	// A special header sent with each AJAX request
//	private static final String IS_AJAX_HEADER = "X-Requested-With=XMLHttpRequest";



	private final ForumManagement forumManagement;
	private final CustomerManagement customerManagement;

	ForumController(ForumManagement forumManagement, CustomerManagement customerManagement) {

		Assert.notNull(forumManagement, "ForumManagement must not be null!");
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");

		this.forumManagement = forumManagement;
		this.customerManagement = customerManagement;
	}

	/**
	 * Handles requests to the application root URI. Note, that you can use {@code redirect:} as prefix to trigger a
	 * browser redirect instead of simply rendering a view.
	 *
	 * @return a redirect string
	 */

/*	//TODO:link
	@GetMapping("/forum")
	String index() {
		return "redirect:/forum";
	}*/


	@GetMapping("/forum")
	String theme(Model model) {
		model.addAttribute("themes", forumManagement.findAll());
		return "forum";
	}

	@PostMapping("/forum")
	String addTheme(@RequestParam("title") String title) {
		Assert.notNull(title, "Theme must not be null or empty!");
		forumManagement.createTheme(title);
		return "redirect:/forum";
	}

	//TODO:link
	@GetMapping("/theme")
	String comment(Model model,@RequestParam("themeName") String name, @ModelAttribute(binding = false) ForumForm form, @LoggedIn Optional<UserAccount> userAccount) {

		var theme = forumManagement.findByThemeName(name);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		model.addAttribute("themeName", theme.getName());
		model.addAttribute("forums", theme.getForums());
		model.addAttribute("form", form);
		model.addAttribute("name", customer.toString());
		model.addAttribute("email", customer.getUserAccount().getEmail());

		return "theme";
	}


	//TODO:LINK
	@PostMapping("/theme/{themeName}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addComment(@PathVariable String themeName, @Valid @ModelAttribute("form") ForumForm form, Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {

		//var customer = customerManagement.findByUserAccount(userAccount.get());
		var theme = forumManagement.findByThemeName(themeName);



		if (errors.hasErrors()) {
			System.out.println("comment has errors");
			return comment(model, themeName, form, userAccount);
		}

		//forum.save(form.toNewEntry());


//		model.addAttribute("entry", forumManagement.createComment(theme,form.toNewEntry()));
//		model.addAttribute("index", forumManagement.countComment());
		forumManagement.createComment(theme, form.toNewEntry());
		return "redirect:/forum";
	}

	@PostMapping("/theme/like/{forumId}")
	String like(@PathVariable String forumId, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(forumId);
		var forum = forumManagement.findForumById(id);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		if(!forum.likedContains(customer)) {
			if(forum.unlikedContains(customer)) forum.removeUnlike(customer);
			forumManagement.likeComment(customer, forum);
		}

		return "redirect:/forum";
	}

	@PostMapping("/theme/unlike/{forumId}")
	String unlike(@PathVariable String forumId, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(forumId);
		var forum = forumManagement.findForumById(id);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		if(!forum.unlikedContains(customer)) {
			if(forum.likedContains(customer)) forum.removeLike(customer);
			forumManagement.unlikeComment(customer, forum);
		}

		return "redirect:/forum";
	}


	@PostMapping("/forum/sendMoney")
	public String charge(@RequestParam("money") double money, @LoggedIn Optional<UserAccount> userAccount,
						 RedirectAttributes redir){
		if (Money.of(money, EURO).isLessThanOrEqualTo(Money.of(0, EURO))) {
			redir.addFlashAttribute("message", "Invalid number");
			return "redirect:/forum";
		}

		var customer = customerManagement.findByUserAccount(userAccount.get());
		customerManagement.charge(Money.of(money, EURO), customer);
		return "redirect:/forum";
	}


	@GetMapping("/sendMoney")
	public String sendMoney(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		var customer = customerManagement.findByUserAccount(userAccount.get());
		model.addAttribute("firstname", customer.getUserAccount().getFirstname());
		model.addAttribute("lastname", customer.getUserAccount().getLastname());
		model.addAttribute("email", customer.getUserAccount().getEmail());
		model.addAttribute("balance", customer.getBalance());

		return "sendMoney";
	}

/*
	//TODO:LINK
	@PostMapping(path = "/forum", headers = IS_AJAX_HEADER)
	String addEntry(@Valid ForumForm form, Model model) {



		return "forum :: theme";
	}

 */

/*	//TODO:LINK
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/forum/{entry}")
	String removeEntry(@PathVariable Optional<ForumEntry> entry) {

		return entry.map(it -> {

			forumManagement.delete(it);
			return "redirect:/forum";

		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}


	//TODO:LINK
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(path = "/forum/{entry}", headers = IS_AJAX_HEADER)
	HttpEntity<?> removeEntryJS(@PathVariable Optional<ForumEntry> entry) {

		return entry.map(it -> {

			forumManagement.delete(it);
			return ResponseEntity.ok().build();

		}).orElseGet(() -> ResponseEntity.notFound().build());
	}*/
}
