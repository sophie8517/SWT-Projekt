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

		userAccountManagement.create("admin", Password.UnencryptedPassword.of("lottery"), Role.of("ADMIN"));
		var password = "lottery";
		var passwordCheck = "lottery";

		List.of(
				new RegistrationForm("Juergen Staub", password, passwordCheck)
		).forEach(customerManagement::createCustomer);
	}
}

