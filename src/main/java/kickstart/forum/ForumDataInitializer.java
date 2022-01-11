package kickstart.forum;

import kickstart.customer.CustomerManagement;
import kickstart.customer.RegistrationForm;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
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

	ForumDataInitializer(ForumManagement forumManagement) {

		Assert.notNull(forumManagement, "ForumManagement must not be null!");

		this.forumManagement = forumManagement;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default comments.");

		Stream.of( //
				new ForumEntry("Administrator", "Welcome to Mach Dein Glück!", "none"), //
				new ForumEntry("Arni", "Hasta la vista, baby", "arni@gmx.de"), //
				new ForumEntry("Duke Nukem", "It's time to kick ass and chew bubble gum. And I'm all out of gum.", "duke.nukem@tu-dresden.de"), //
				new ForumEntry("Gump1337",
						"Mama always said life was like a box of chocolates. You never know what you're gonna get.", "gump1337@outlook.live")) //
				.forEach(forumManagement::createComment);
	}
}
