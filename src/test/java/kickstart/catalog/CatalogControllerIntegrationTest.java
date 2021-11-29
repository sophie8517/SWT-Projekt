package kickstart.catalog;

import kickstart.AbstractIntegrationTest;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.salespointframework.core.Currencies.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CatalogControllerIntegrationTest  extends AbstractIntegrationTest {

	@Autowired
	CatalogController catalogController;


	private static LotteryCatalog lotteryCatalog;


	private static CustomerRepository customerRepository;
	private Ticket t;
	private CatalogDataInitializer cd;
	private ProductIdentifier id;
	private Customer c;
	private UserAccount ua;

	@BeforeEach
	void setUp() {
		cd = new CatalogDataInitializer(lotteryCatalog);
		cd.initialize();
		t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		id = t.getId();
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void CatalogControllerIntegrationTestTicket(){
		Model model = new ExtendedModelMap();

		String returnedView = catalogController.ticketCatalog(model);

		assertThat(returnedView).isEqualTo("3_catalog_num");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("3_catalog_num");

		assertThat(object).hasSize(1);
	}

	@Test
	public void CatalogControllerIntegrationTestFootball(){
		Model model = new ExtendedModelMap();
		Optional<UserAccount> opt = Optional.empty();

		String returnedView = catalogController.footballCatalog(model,opt);

		assertThat(returnedView).isEqualTo("2_catalog_foot");

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("2_catalog_foot");

		assertThat(object).hasSize(20);
	}

	@Test
	public void CatalogControllerWrongInput(){


		int z1,z2,z3,z4,z5,z6,z7,dauer;
		z1 = 13;
		z2 = 21;
		z3 = 6;
		z4 = 26;
		z5 = 11;
		z6 = 6;
		z7 = 0;
		dauer = 1;
		String returnView = catalogController.bet_num(id,z1,z2,z3,z4,z5,z6,z7,dauer,Optional.of(ua));
		assertThat(returnView).isEqualTo("wronginput.html");
	}

	@Test
	public void CatalogControllerError(){


		int z1,z2,z3,z4,z5,z6,z7,dauer;
		z1 = 13;
		z2 = 21;
		z3 = 6;
		z4 = 26;
		z5 = 11;
		z6 = 7;
		z7 = 0;
		dauer = 1;
		String returnView = catalogController.bet_num(id,z1,z2,z3,z4,z5,z6,z7,dauer,Optional.of(ua));
		assertThat(returnView).isEqualTo("error.html");
	}

	@Test
	public void CatalogControllerSuccess(){
		Ticket t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		ProductIdentifier id = t.getId();
		Customer c = customerRepository.findAll().get().findFirst().get();
		c.setBalance(Money.of(50,EURO));
		customerRepository.save(c);
		UserAccount ua = c.getUserAccount();

		int z1,z2,z3,z4,z5,z6,z7,dauer;
		z1 = 13;
		z2 = 21;
		z3 = 6;
		z4 = 26;
		z5 = 11;
		z6 = 7;
		z7 = 0;
		dauer = 1;
		String returnView = catalogController.bet_num(id,z1,z2,z3,z4,z5,z6,z7,dauer,Optional.of(ua));
		assertThat(returnView).isEqualTo("redirect:/");
	}

}
