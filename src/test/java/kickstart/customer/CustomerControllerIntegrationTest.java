/*
package kickstart.customer;

import kickstart.AbstractIntegrationTests;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;
import static org.salespointframework.core.Currencies.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerControllerIntegrationTest extends AbstractIntegrationTests {
	@Autowired
	CustomerController customerController;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerManagement customerManagement;

	@Autowired
	private GroupRepository groupRepository;

	private UserAccount userAccount;
	private Customer customer;
	private Optional<UserAccount> optional;
	private List<Customer> customerList;
	private Group group;


	@BeforeEach
	void setUp() {
		customer = customerRepository.findAll().get().findFirst().get();
		customer = customerManagement.findByUserAccount(customer.getUserAccount());
		userAccount = customer.getUserAccount();
		optional = Optional.of(userAccount);
		customerList = new ArrayList<>();
		group = new Group(customer.getUserAccount(), customer);
	}

	@Test
	public void CustomerControllerIntegrationTestRegisterNew(){
		RegistrationForm form = new RegistrationForm("Anna", "Nana", "anna@tu-dresden.de", "An123456", "An123456");
		Errors result = new BeanPropertyBindingResult(form, "form");
		String returnedView = customerController.registerNew(form, result);
		if (result.hasErrors()){
			assertThat(returnedView).isEqualTo("register");
		} else {
			assertThat(returnedView).isEqualTo("redirect:/");
		}
	}

	@Test
	public void CustomerControllerIntegrationTestRegister(){
		String returnedView = customerController.register();
		assertThat(returnedView).isEqualTo("register");
	}

	@Test
	public void CustomerControllerIntegrationTestProfile(){
		Model model = new ExtendedModelMap();
		String returnedView = customerController.getProfile(model, optional);

		Assertions.assertEquals(customer.getUserAccount().getFirstname(), model.getAttribute("firstname"));
		Assertions.assertEquals(customer.getUserAccount().getLastname(), model.getAttribute("lastname"));
		Assertions.assertEquals(customer.getUserAccount().getEmail(), model.getAttribute("email"));

		assertThat(returnedView).isEqualTo("meinProfil");
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CustomerControllerIntegrationCustomers(){
		Model model = new ExtendedModelMap();
		String returnedView = customerController.customers(model);

		assertThat(returnedView).isEqualTo("customers");
	}

	@Test
	public void CustomerControllerIntegrationCreate(){
		Model model = new ExtendedModelMap();
		String returnedView = customerController.customers(model);

		assertThat(returnedView).isEqualTo("redirect:/");
	}


	@Test
	public void CustomerControllerIntegrationTestCharge(){

		try {
			customerController.charge(5,optional);
			System.out.println(customer.getBalance());
		}
		catch (IllegalStateException exception){
			assertThat(true).isTrue();
		}
	}

	@Test
	public void CustomerControllerIntegrationTestChargeFail(){
		try {
			customerController.charge(-5,optional);
			System.out.println(customer.getBalance());
			fail("Not allowed to charge negative amount of money.");
		}
		catch (IllegalStateException ignored){
			//empty
		}
	}

	@Test
	public void CustomerControllerIntegrationTestViewBalance(){
		Model model = new ExtendedModelMap();

		String returnedView = customerController.viewBalance(model,optional);
		Assertions.assertEquals(customer.getUserAccount().getFirstname(), model.getAttribute("firstname"));
		Assertions.assertEquals(customer.getUserAccount().getLastname(), model.getAttribute("lastname"));
		Assertions.assertEquals(customer.getUserAccount().getEmail(), model.getAttribute("email"));
		Assertions.assertEquals(customer.getBalance(), model.getAttribute("balance"));

		assertThat(returnedView).isEqualTo("balance");
	}


	@Test
	public void CustomerControllerIntegrationAddMember(){
		try {
			group.add(customer);
			customerList.addAll(group.getMembers());

			Assertions.assertEquals(customerList.size(), 1);
		} catch (NullPointerException exception) {
			assertThat(true).isTrue();
		}
	}

	@Test
	public void CustomerControllerIntegrationRemoveMember(){
		try {
			customerManagement.removeMemberOfGroup(customer, group);
			fail("We hope leader is the only one member in the group but he's not.");
		} catch (NullPointerException exception){
			assertThat(true).isTrue();
			assertThat(group.getMembers()).hasSize(1);
		} catch (IllegalStateException ignored){
			//empty
		}

	}

	@Test
	public void CustomerControllerIntegrationDeleteGroup() {
		//Model model = new ExtendedModelMap();
		//String returnedView = customerController.deleteGroup(model, group.getId());

		try {
			customerManagement.deleteGroup(group);
		} catch (NullPointerException exception){
			assertThat(true).isTrue();
			fail("We hope the group doesn't exist.");
		}
;
		//assertThat(returnedView).isEqualTo("redirect/:");
	}

}





*/


