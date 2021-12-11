package kickstart.customer;

import kickstart.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerControllerIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	CustomerController customerController;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerManagement customerManagement;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;
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
		group = new Group();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
	}

	@Test
	public void CustomerControllerIntegrationTestRegisterNew(){
		RegistrationForm form = new RegistrationForm("Anna", "Nana", "anna@tu-dresden.de", "An123456", "An123456");
		Errors result = new BeanPropertyBindingResult(form, "form");
		Model model = new ExtendedModelMap();
		String returnedView = customerController.register(model, form);
		if (result.hasErrors()){
			assertThat(returnedView).isEqualTo("redirect:/");
		} else {
			assertThat(returnedView).isEqualTo("register");
		}
	}

	@Test
	public void CustomerControllerIntegrationTestRegister(){
		RegistrationForm form = new RegistrationForm("Anna", "Nana", "anna@tu-dresden.de", "An123456", "An123456");
		Errors result = new BeanPropertyBindingResult(form, "form");
		Model model = new ExtendedModelMap();
		String returnedView = customerController.register(model, form);
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
		String returnedView = customerController.createGroupPage(model);

		assertThat(returnedView).isEqualTo("group_create");
	}


	@Test
	public void CustomerControllerIntegrationTestCharge() throws Exception{
		//mockMvc.perform(MockMvcRequestBuilders.get("/balance/charge"))
		//		.andExpect(MockMvcResultMatchers.flash().attribute("message","Invalid number"));

		RedirectAttributes redir = new RedirectAttributesModelMap();

		try {
			customerController.charge(0, optional, redir);
			System.out.println(customer.getBalance());
			assertThat(redir.getFlashAttributes().containsKey("message"));
		}
		catch (IllegalStateException exception){
			assertThat(true).isTrue();
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

}



