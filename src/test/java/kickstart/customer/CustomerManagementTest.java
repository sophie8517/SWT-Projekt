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
		Customer customer = customerManagement.createCustomer(new RegistrationForm(
				"Test", "Leader", "test@leader.de", "123", "123"));
		String name = "create group";
		Group group = customerManagement.createGroup(name, customer);
		assertEquals(group, customerManagement.findByGroupName(group.getGroupName()));
	}

	@Test
	void deleteGroup() {
		Customer customer = customerManagement.createCustomer(new RegistrationForm("Test", "Leader", "test@test.de", "123", "123"));
		String name = "test group";
		Group group = customerManagement.createGroup(name, customer);
		customerManagement.deleteGroup(group);
		assertNull(customerManagement.findByGroupName(group.getGroupName()));
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
		Group a = customerManagement.createGroup("testA", leader);
		Group b = customerManagement.createGroup("testB", leader);
		List<Group> groups = List.of(a, b);
		List<Group> groups1 = customerManagement.findAllGroups().filter(group -> group.contains(leader)).toList();
		System.out.println(groups);
		System.out.println(groups1);
		assertEquals(groups.size(), groups1.size());
		assertEquals(groups, groups1);
	}

	@Test
	void addMemberToGroup() {
		Customer leader = customerManagement.createCustomer(
				new RegistrationForm("test", "leader", "test@leader.de", "123", "123"));
		Group testGroup = customerManagement.createGroup("testGroup", leader);
		Customer customer = customerManagement.findAllCustomers().stream().findFirst().get();
		Customer temp = new Customer();
		assertTrue(testGroup.getMembers().size() == 1);

		customerManagement.addMemberToGroup(customer, testGroup, testGroup.getPassword());

		assertTrue(testGroup.getMembers().size() == 2);
	}

	@Test
	void removeMemberOfGroup() {
		Customer customerA = customerManagement.createCustomer(
				new RegistrationForm("test", "alpha", "test@alpha.de", "123", "123"));
		Customer leader = customerManagement.createCustomer(
				new RegistrationForm("test", "leader", "test@leader.de", "123", "123"));
		Group testGroup = customerManagement.createGroup("testGroup", leader);
		customerManagement.addMemberToGroup(customerA, testGroup, testGroup.getPassword());
		assertTrue(testGroup.getMembers().size() == 2);

		customerManagement.removeMemberOfGroup(customerA, testGroup);
		assertTrue(testGroup.getMembers().size() == 1);
	}

	@Test
	void charge() {
		Customer customer = customerManagement.createCustomer(
				new RegistrationForm("test", "charge", "test@charge.de", "123", "123"));
		customerManagement.charge(Money.of(20, EURO), customer);

		assertTrue(customer.getBalance().isEqualTo(Money.of(20, EURO)));

	}
}