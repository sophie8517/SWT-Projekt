package kickstart.catalog;

import kickstart.AbstractIntegrationTest;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.salespointframework.core.Currencies.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CatalogControllerIntegrationTest  extends AbstractIntegrationTest {

	@Autowired
	CatalogController catalogController;

	@Autowired
	private LotteryCatalog lotteryCatalog;

	@Autowired
	private CustomerRepository customerRepository;

	private Ticket t;
	private Football f_timeup,f_success;
	private CatalogDataInitializer cd;
	private ProductIdentifier id, id_f_timeup,id_f_success;
	private Customer c;
	private UserAccount ua;

	@BeforeEach
	void setUp() {

		t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		f_timeup = new Football("n", LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1),
				LocalTime.of(15,0)),Money.of(10,EURO), Item.ItemType.FOOTBALL,
				new Team("team1"),new Team("team2"),"liga","img1","img2");
		f_success = new Football("n", LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(4),
				LocalTime.of(15,0)),Money.of(10,EURO), Item.ItemType.FOOTBALL,
				new Team("team1"),new Team("team2"),"liga","img1","img2");
		id = t.getId();
		id_f_timeup = f_timeup.getId();
		id_f_success = f_success.getId();

		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
	}

	@Test
	public void CatalogControllerIntegrationTestTicket(){
		Model model = new ExtendedModelMap();

		String returnedView = catalogController.ticketCatalog(model);

		assertThat(returnedView).isEqualTo("3_catalog_num");

		List<Item> items = lotteryCatalog.findByType(Item.ItemType.TICKET);

		assertThat(items).hasSize(1);
	}

	@Test
	public void CatalogControllerIntegrationTestFootball(){
		Model model = new ExtendedModelMap();
		Optional<UserAccount> opt = Optional.empty();

		String returnedView = catalogController.footballCatalog(model,opt);

		assertThat(returnedView).isEqualTo("2_catalog_foot");

		List<Item> items = lotteryCatalog.findByType(Item.ItemType.FOOTBALL);

		assertThat(items).hasSize(20);
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
		assertThat(returnView).isEqualTo("error");
	}

	@Test
	public void CatalogControllerSuccess(){

		c.setBalance(Money.of(50,EURO));
		//customerRepository.save(c);


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

	@Test
	public void CatalogControllerFootTimeUp(){
		lotteryCatalog.save(f_timeup);

		String returnView = catalogController.bet_foot(id_f_timeup,1,12.0,Optional.of(ua));
		assertThat(returnView).isEqualTo("time_up.html");
		lotteryCatalog.delete(f_timeup);
	}



	@Test
	public void CatalogControllerFootSuccess(){
		lotteryCatalog.save(f_success);
		c.setBalance(Money.of(40,EURO));
		String returnView = catalogController.bet_foot(id_f_success,1,12.0,Optional.of(ua));
		assertThat(returnView).isEqualTo("redirect:/");
		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CatalogControllerFootError(){
		lotteryCatalog.save(f_success);
		String returnView = catalogController.bet_foot(id_f_success,1,12.0,Optional.of(ua));
		assertThat(returnView).isEqualTo("error");
		lotteryCatalog.delete(f_success);

	}

	@AfterEach
	void breakDown(){
		List<NumberBet> nbs = t.getNumberBetsbyCustomer(c);
		for(NumberBet n:nbs){
			t.removeBet(n);
		}
		c.setBalance(Money.of(0,EURO));
		lotteryCatalog.save(t);


		customerRepository.save(c);
	}

}
