package kickstart.forum;


import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import kickstart.customer.CustomerManagement;
import kickstart.customer.Group;
import kickstart.customer.GroupRepository;
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

	private final GroupChatManagement groupChatManagement;

	//private final GroupRepository groupRepository;

	ForumController(ForumManagement forumManagement, CustomerManagement customerManagement,
					GroupChatManagement groupChatManagement, UserAccountManagement userAccountManagement,
					PrivateChatManagement privateChatManagement) {

		Assert.notNull(forumManagement, "ForumManagement must not be null!");
		Assert.notNull(customerManagement, "CustomerManagement must not be null!");
		Assert.notNull(customerManagement,"UserAccountManagement must not be null!");
		Assert.notNull(customerManagement,"PrivateChatManagement must not be null!");
		this.forumManagement = forumManagement;
		this.customerManagement = customerManagement;
		this.userAccountManagement = userAccountManagement;
		this.privateChatManagement = privateChatManagement;
		this.groupChatManagement = groupChatManagement;
		//this.groupRepository = groupRepository;
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
	String theme(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		var customer = customerManagement.findByUserAccount(userAccount.get());

		model.addAttribute("customer", customer);
		model.addAttribute("themes", forumManagement.findAll());
		model.addAttribute("privateChats", privateChatManagement.findAllByUserAccount(userAccount.get()));
		model.addAttribute("groupChats", groupChatManagement.findAllGroupChat(customer));
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
	String comment(Model model,@RequestParam("themeName") String name, @ModelAttribute(binding = false) ForumForm form,
				   @LoggedIn Optional<UserAccount> userAccount) {

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
	String startNewPrivateConversation(Model model, @RequestParam("chatId") String chatId,
									   @ModelAttribute(binding = false) ForumForm form,
									   @LoggedIn Optional<UserAccount> userAccount){
		long id = Long.parseLong(chatId);
		var chat = privateChatManagement.findByPrivateChatId(id);
		//model.addAttribute("catalog",catalog.findByName(name));
		model.addAttribute("invitee",
				chat.getPartners().get(1).getFirstname() + " " + chat.getPartners().get(1).getLastname());
		model.addAttribute("form", form);
		model.addAttribute("id", chat.getId());
		model.addAttribute("name",
				userAccount.get().getFirstname() + " " + userAccount.get().getLastname());
		model.addAttribute("email", userAccount.get().getEmail());
		model.addAttribute("chat", chat.getPartners());
		model.addAttribute("forums", chat.getForums());
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
		return "chat";
	}

	@PostMapping("/chat/{chatId}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addPrivateComment(@PathVariable String chatId, @Valid @ModelAttribute("form") ForumForm form,
							 Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {

		//var customer = customerManagement.findByUserAccount(userAccount.get());
		System.out.println(chatId);
		long id = Long.parseLong(chatId);
		var chat = privateChatManagement.findByPrivateChatId(id);


		if (errors.hasErrors()) {
			System.out.println("Comment has errors");
			return startNewPrivateConversation(model, String.valueOf(chat.getId()), form, userAccount);
		}

		//forum.save(form.toNewEntry());
//		model.addAttribute("entry", forumManagement.createComment(theme,form.toNewEntry()));
//		model.addAttribute("index", forumManagement.countComment());
		privateChatManagement.createMessage(chat, form.toNewEntry());

		return "redirect:/forum";
	}

	@GetMapping ("/search")
	String searchPartner(@RequestParam("invitee") String email, @LoggedIn Optional<UserAccount> inviter,
						 RedirectAttributes redir){
		if(customerManagement.findByEmail(email).isEmpty()) {
			redir.addFlashAttribute("message", "Customer not found or email was wrong!");
			return "redirect:/forum";
		}

		if (email.equals(inviter.get().getEmail())) {
			redir.addFlashAttribute("message", "You cannot enter your email");
			return "redirect:/forum";
		}

		var invitee = customerManagement.findByEmail(email).get();
		Iterator<PrivateChat> it = privateChatManagement.findAll().iterator();
		while(it.hasNext()) {
			PrivateChat privateChat = it.next();
			if (privateChat.contains(inviter.get()) && privateChat.contains(invitee)) {
				return "redirect:/forum";
			}
		}
		privateChatManagement.createPrivateChat(invitee, inviter.get());
		return "redirect:/forum";
	}

	//TODO:LINK
	@GetMapping("/groupChat")
	String groupChatComment(Model model, @RequestParam("groupChatName") String name,
							@ModelAttribute(binding = false) ForumForm form,
							@LoggedIn Optional<UserAccount> userAccount) {

		var groupChat = groupChatManagement.findByGroupChatName(name);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		model.addAttribute("groupChatName", name);
		model.addAttribute("forums", groupChat.getForums());
		model.addAttribute("form", form);
		model.addAttribute("name", customer.toString());
		model.addAttribute("email", customer.getUserAccount().getEmail());

		return "groupChat";
	}

	//TODO:LINK
	@PostMapping("/groupChat/{groupChatName}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addGrouChatComment(@PathVariable String groupChatName, @Valid @ModelAttribute("form") ForumForm form,
							  Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		var groupChat = groupChatManagement.findByGroupChatName(groupChatName);

		if(errors.hasErrors()) {
			System.out.println("comment has errors.");
			return groupChatComment(model, groupChatName, form, userAccount);
		}

		groupChatManagement.createComment(groupChat, form.toNewEntry());

		return "redirect:/forum";
	}


	//TODO:LINK
	@PostMapping("/theme/{themeName}")
	@PreAuthorize("hasRole('CUSTOMER')")
	String addComment(@PathVariable String themeName, @Valid @ModelAttribute("form") ForumForm form,
					  Errors errors, Model model, @LoggedIn Optional<UserAccount> userAccount) {

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

	@PostMapping("/theme/like/{forumId}")
	String like(@PathVariable String forumId, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(forumId);
		var forum = forumManagement.findForumById(id);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		if(!forum.likedContains(customer)) {
			if(forum.unlikedContains(customer)){
				forumManagement.cancelDislike(customer, forum);
			}
			forumManagement.likeComment(customer, forum);
		} else {
			forumManagement.cancelLike(customer, forum);
		}

		return "redirect:/forum";
	}

	@PostMapping("/theme/unlike/{forumId}")
	String unlike(@PathVariable String forumId, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(forumId);
		var forum = forumManagement.findForumById(id);
		var customer = customerManagement.findByUserAccount(userAccount.get());
		if(!forum.unlikedContains(customer)) {
			if(forum.likedContains(customer)){
				forumManagement.cancelLike(customer, forum);
			}
			forumManagement.unlikeComment(customer, forum);
		} else {
			forumManagement.cancelDislike(customer, forum);
		}

		return "redirect:/forum";
	}


	@PostMapping("/forum/sendMoney")
	public String transfer(@RequestParam("money") double money, @RequestParam("receiver") String email,
						   RedirectAttributes redir, @LoggedIn Optional<UserAccount> userAccount){

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
