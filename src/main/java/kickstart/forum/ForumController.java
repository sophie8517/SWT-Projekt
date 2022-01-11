package kickstart.forum;


import java.util.Optional;

import javax.validation.Valid;

import kickstart.customer.CustomerManagement;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
class ForumController {

	// A special header sent with each AJAX request
	private static final String IS_AJAX_HEADER = "X-Requested-With=XMLHttpRequest";


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

	//TODO:link
	@GetMapping("/forum")
	String comment(Model model, @ModelAttribute(binding = false) ForumForm form, @LoggedIn Optional<UserAccount> userAccount) {

		model.addAttribute("entries", forumManagement.findAll());
		model.addAttribute("form", form);

		if(userAccount.get().hasRole(Role.of("ADMIN"))) return "forum";

		var customer = customerManagement.findByUserAccount(userAccount.get());
		model.addAttribute("name", customer.toString());
		model.addAttribute("email", customer.getUserAccount().getEmail());

		return "forum";
	}


	//TODO:LINK
	@PostMapping("/forum")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addEntry(@Valid @ModelAttribute("form") ForumForm form, Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {

		var customer = customerManagement.findByUserAccount(userAccount.get());


		if (errors.hasErrors()) {
			System.out.println("comment has errors");
			return comment(model, form, userAccount);
		}

		//forum.save(form.toNewEntry());


		model.addAttribute("entry", forumManagement.createComment(form.toNewEntry()));
		model.addAttribute("index", forumManagement.count());

		return "redirect:/forum";
	}


/*	//TODO:LINK
	@PostMapping(path = "/forum", headers = IS_AJAX_HEADER)
	String addEntry(@Valid ForumForm form, Model model) {



		return "forum :: entry";
	}*/


	//TODO:LINK
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
	}
}
