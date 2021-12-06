package kickstart.customer;

import kickstart.AbstractIntegrationTests;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;

class CustomerManagementTest extends AbstractIntegrationTests {
	@Autowired
	CustomerManagement customerManagement;

	@Autowired
	UserAccountManagement userAccountManagement;


	@Test
	void createCustomer() {
		String firstname = "Test";
		String lastname = "Customer";
		String email = "test@customer.de";
		String password = "123";
		RegistrationForm form = new RegistrationForm(firstname, lastname, email, password, password);
		Customer customer = customerManagement.createCustomer(form);

		assertEquals(customer, customerManagement.findByCustomerId(customer.getId()));
	}


	@Test
	void createGroup() {
		Customer leader = customerManagement.createCustomer(new RegistrationForm(
				"Test", "Leader", "test@leader.de", "123", "123"));
		String name = "create group";
		GroupRegistrationForm form = new GroupRegistrationForm(name);
		Group group = customerManagement.createGroup(form, leader);
		assertEquals(group, customerManagement.findByGroupId(group.getId()));
	}

	@Test
	void deleteGroup() {
		Customer leader = customerManagement.createCustomer(new RegistrationForm("Test", "Leader", "test@test.de", "123", "123"));
		String name = "test group";
		GroupRegistrationForm form = new GroupRegistrationForm(name);
		Group group = customerManagement.createGroup(form, leader);
		customerManagement.deleteGroup(group);
		assertNull(customerManagement.findByGroupId(group.getId()));
	}

	@Test
	void findAllCustomers() {
		String password = "123";

		Customer a = customerManagement.createCustomer(
				new RegistrationForm("test", "alpha", "test@alpha.de", password, password));
		Customer b = customerManagement.createCustomer(
				new RegistrationForm("test", "bravo", "test@bravo.de", password, password));
		Customer c = customerManagement.createCustomer(
				new RegistrationForm("test", "charlie", "test@charlie.de", password, password));

		List<Customer> customers = List.of(a, b, c);
		List<Customer> customers1 = customerManagement.findAllCustomers().filter(customer -> customer.getUserAccount().getFirstname().equals("test")).toList();
		System.out.println(customers1);
		System.out.println(customers);

		assertEquals(customers.size(), customers1.size());
		assertEquals(customers, customers1);
	}

	@Test
	void findAllGroups() {
		Customer leader = customerManagement.createCustomer(
				new RegistrationForm("test", "leader", "test@leader.de", "123", "123"));
		Group a = customerManagement.createGroup(new GroupRegistrationForm("testA"), leader);
		List<Group> groups = List.of(a);
		List<Group> groups1 = customerManagement.findAllGroups().toList();
		System.out.println(groups);
		System.out.println(groups1);
	}

	@Test
	void addMemberToGroup() {
	}

	@Test
	void removeMemberOfGroup() {
	}

	@Test
	void charge() {
		Customer customer = customerManagement.createCustomer(
				new RegistrationForm("test", "charge", "test@charge.de", "123", "123"));
		customerManagement.charge(Money.of(20, EURO), customer);

		assertTrue(customer.getBalance().isEqualTo(Money.of(20, EURO)));

		try {
			customerManagement.charge(Money.of(0, EURO), customer);
			fail("Method charge should throw an IllegalArgumentException if charge money not bigger than 0");
		} catch (IllegalArgumentException ignored) {}
	}
}