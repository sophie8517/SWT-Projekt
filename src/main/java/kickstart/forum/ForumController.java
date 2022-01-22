package kickstart.forum;


import java.util.Optional;

import javax.validation.Valid;

import kickstart.customer.CustomerManagement;
import kickstart.customer.RegistrationForm;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.salespointframework.core.Currencies.EURO;

@Controller
class ForumController {

	// A special header sent with each AJAX request
//	private static final String IS_AJAX_HEADER = "X-Requested-With=XMLHttpRequest";


	private final ForumManagement forumManagement;
	private final CustomerManagement customerManagement;
	private final UserAccountManagement userAccountManagement;
	private final PrivateChatManagement privateChatManagement;

	ForumController(ForumManagement forumManagement, CustomerManagement customerManagement, UserAccountManagement userAccountManagement, PrivateChatManagement privateChatManagement) {
		Assert.notNull(forumManagement, "ForumManagement must not be null!");
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");
		Assert.notNull(customerManagement,"UserAccountManagement must not be null!");
		Assert.notNull(customerManagement,"PrivateChatManagement must not be null!");
		this.forumManagement = forumManagement;
		this.customerManagement = customerManagement;
		this.userAccountManagement = userAccountManagement;
		this.privateChatManagement = privateChatManagement;
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
		model.addAttribute("privateChats", privateChatManagement.findAll());
		return "forum";
	}

	@PostMapping("/forum")
	String addTheme(@RequestParam("title") String title) {
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
	@GetMapping("/chat")
	String startNewPrivateConversation(Model model, @RequestParam("invitee") String inviteeName, @LoggedIn Optional<UserAccount> userAccount, PrivateChat privateChat){
		var chat = privateChatManagement.findByPrivateChatId(privateChat.getId());
		//model.addAttribute("catalog",catalog.findByName(name));
		model.addAttribute("invitee",inviteeName);
		model.addAttribute("name", userAccount.get());
		model.addAttribute("chat", privateChat.getPartners());
		/*
		if (result.hasErrors()){
			return "forum";
		}
		for (UserAccount userAccount1 : userAccountManagement.findAll() ){
			if (!invitee.getEmail().equals(userAccount1.getEmail())){
				redirAttrs.addFlashAttribute("message", "User not found.");
				return "redirect:/forum";
			}
		}

		privateChatManagement.createPrivateChat(userAccount.get(), invitee);
 		*/
		return "theme";
	}

	@PostMapping("/chat/{chatName}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addComment(@PathVariable String chatName, @Valid @ModelAttribute("form") ForumForm form, Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount, PrivateChat privateChat) {

		//var customer = customerManagement.findByUserAccount(userAccount.get());
		var chat = privateChatManagement.findByPrivateChatId(privateChat.getId());


		if (errors.hasErrors()) {
			System.out.println("Comment has errors");
			return comment(model, chatName, form, userAccount);
		}

		//forum.save(form.toNewEntry());
//		model.addAttribute("entry", forumManagement.createComment(theme,form.toNewEntry()));
//		model.addAttribute("index", forumManagement.countComment());
		privateChatManagement.createMessage(chat, form.toNewEntry());

		return "redirect:/forum";
	}

	@GetMapping ("/search")
	String searchPartner(@RequestParam("invitee") String email, @LoggedIn Optional<UserAccount> inviter){
		var invitee = customerManagement.findByEmail(email).get();

		if (!customerManagement.findByEmail(email).isPresent()){
			System.out.println("Entered email has errors");
		}
		privateChatManagement.createPrivateChat(invitee, inviter.get());
		return "redirect:/forum";
	}

	//TODO:LINK
	@PostMapping("/theme/{themeName}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addComment(@PathVariable String themeName, @Valid @ModelAttribute("form") ForumForm form, Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {

		//var customer = customerManagement.findByUserAccount(userAccount.get());
		var theme = forumManagement.findByThemeName(themeName);


		if (errors.hasErrors()) {
			System.out.println("Comment has errors");
			return comment(model, themeName, form, userAccount);
		}

		//forum.save(form.toNewEntry());
//		model.addAttribute("entry", forumManagement.createComment(theme,form.toNewEntry()));
//		model.addAttribute("index", forumManagement.countComment());
		forumManagement.createComment(theme, form.toNewEntry());

		return "redirect:/forum";
	}


	@PostMapping("/forum/sendMoney")
	public String charge(@RequestParam("money") double money, @RequestParam("invitee") String email, RedirectAttributes redir, @LoggedIn Optional<UserAccount> userAccount){

		var invitee = customerManagement.findByEmail(email).get();
		if (Money.of(money, EURO).isLessThanOrEqualTo(Money.of(0, EURO))) {
			redir.addFlashAttribute("message", "Invalid number");
			return "redirect:/forum";
		}
		var inviter = customerManagement.findByUserAccount(userAccount.get());
		if (inviter.getBalance().isLessThan(Money.of(money, EURO))){
			redir.addFlashAttribute("message", "Not enough money");
			return "not_enough_money";
		}
		customerManagement.transfer(Money.of(money, EURO), inviter);
		var customer = customerManagement.findByUserAccount(invitee);
		customerManagement.charge(Money.of(money, EURO), customer);
		return "redirect:/forum";
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
