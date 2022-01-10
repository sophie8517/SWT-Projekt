package kickstart.customer;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Stream;

import kickstart.forum.*;


@Component
@Order(10)
class CustomerDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final CustomerManagement customerManagement;

	CustomerDataInitializer(UserAccountManagement userAccountManagement, CustomerManagement customerManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(customerManagement, "CustomerRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.customerManagement = customerManagement;
	}

	@Override
	public void initialize() {
		if (userAccountManagement.findByUsername("admin").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers.");

		userAccountManagement.create("admin", Password.UnencryptedPassword.of("123"), Role.of("ADMIN"));
		var password = "123";
		var passwordCheck = "123";

		List.of(
				new RegistrationForm("Song", "Bai","song@tu-dresden.de", password, passwordCheck),
				new RegistrationForm("Nina", "Chen", "nina@tu-dresden.de", password, passwordCheck),
				new RegistrationForm("Mirek", "Kral", "mirek@tu-dresden.de", password, passwordCheck),
				new RegistrationForm("Sophie", "Schulze", "sophie@tu-dresden.de", password, passwordCheck),
				new RegistrationForm("Lukas", "Lei", "lukas@tu-dresden.de", password, passwordCheck),
				new RegistrationForm("Max", "Mustermann", "test@tu-dresden.de", password, passwordCheck)
		).forEach(customerManagement::createCustomer);




		LOG.info("Creating default groups");
		var leader = customerManagement.createCustomer(
				new RegistrationForm("init", "leader", "init@leader.de", "123", "123")
		);

		var swt09 = customerManagement.createGroup("swt09", leader);
		var initGroup = customerManagement.createGroup("initGroup", leader);

		LOG.info("Adding default customers to groupA");
		customerManagement.findAllCustomers().forEach(customer -> customerManagement.addMemberToGroup(customer, swt09, swt09.getPassword()));

		Stream.of( //
				new ForumEntry("H4xx0r", "first!!!", "47903@163.com"), //
				new ForumEntry("Arni", "Hasta la vista, baby", "arni@gmx.de"), //
				new ForumEntry("Duke Nukem", "It's time to kick ass and chew bubble gum. And I'm all out of gum.", "duke.nukem@tu-dresden.de"), //
				new ForumEntry("Gump1337",
						"Mama always said life was like a box of chocolates. You never know what you're gonna get.", "gump1337@outlook.live")) //
				.forEach(customerManagement::createComment);
	}
}

