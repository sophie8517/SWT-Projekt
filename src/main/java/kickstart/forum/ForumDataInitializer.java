package kickstart.forum;

import kickstart.customer.Customer;
import kickstart.customer.CustomerManagement;
import kickstart.customer.CustomerRepository;
import kickstart.customer.RegistrationForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Stream;

@Component
class ForumDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(kickstart.forum.ForumDataInitializer.class);

	private ForumManagement forumManagement;
	private PrivateChatManagement privateChatManagement;
	private CustomerRepository customerRepository;

	ForumDataInitializer(ForumManagement forumManagement, PrivateChatManagement privateChatManagement, CustomerRepository customerRepository) {

		Assert.notNull(forumManagement, "ForumManagement must not be null!");
		Assert.notNull(privateChatManagement, "PrivateChatManagement must not be null!");
		Assert.notNull(customerRepository, "CustomerRepository must not be null!");
		this.forumManagement = forumManagement;
		this.privateChatManagement = privateChatManagement;
		this.customerRepository = customerRepository;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default themes.");

        /*Stream.of( //
						new ForumEntry("Administrator","Welcome to Mach Dein Glück!", "none"), //
						new ForumEntry("Arni", "Hasta la vista, baby", "arni@gmx.de"), //
						new ForumEntry("Duke Nukem", "It's time to kick ass and chew bubble gum. And I'm all out of gum.", "duke.nukem@tu-dresden.de"), //
						new ForumEntry("Gump1337",
								"Mama always said life was like a box of chocolates. You never know what you're gonna get.", "gump1337@outlook.live")) //
				.forEach(forumManagement::createComment);*/

		Theme temp1 = forumManagement.createTheme("Test");
		Theme temp2 = forumManagement.createTheme("SWT");
		Theme temp3 = forumManagement.createTheme("Eine Frage");

		LOG.info("Creating default comments");
		Stream.of(
				new ForumEntry("Administrator", "Welcome to Mach Dein Glück!", "none"),
				new ForumEntry("Arni", "Hasta la vista, baby", "arni@gmx.de")
		).forEach(forumEntry -> forumManagement.createComment(temp1, forumEntry));

		Stream.of(
				new ForumEntry("Song Bai", "SWT Aufgabe", "song@tu-dresden.de"),
				new ForumEntry("Mirek Kral", "Aufgabe", "mirek@tu-dresden.de")
		).forEach(forumEntry -> forumManagement.createComment(temp2, forumEntry));

		Stream.of(
				new ForumEntry("Hey", "Where am i?", "qwert@trewq.org"),
				new ForumEntry("Gump1337",
						"Mama always said life was like a box of chocolates. You never know what you're gonna get.", "gump1337@outlook.live")
		).forEach(forumEntry -> forumManagement.createComment(temp3, forumEntry));

		LOG.info("Creating default chats.");

		List<Customer> customers = customerRepository.findAll().toList();
		//PrivateChat privateChat1 = new PrivateChat(customers.get(0).getUserAccount());
		//privateChat1.addGroupPartner(customers.get(1).getUserAccount());
		PrivateChat privateChat1 = privateChatManagement.createPrivateChat(customers.get(0).getUserAccount(), customers.get(1).getUserAccount());

		Stream.of(
				new ForumEntry(customers.get(0).getUserAccount().getFirstname() + " " + customers.get(0).getUserAccount().getLastname() , "SWT Aufgabe", customers.get(0).getUserAccount().getEmail())
		).forEach(forumEntry -> privateChatManagement.createMessage(privateChat1, forumEntry));
	}
}