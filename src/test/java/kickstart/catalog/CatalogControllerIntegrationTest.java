package kickstart.catalog;

import kickstart.AbstractIntegrationTest;
import kickstart.customer.Customer;
import kickstart.customer.CustomerManagement;
import kickstart.customer.CustomerRepository;
import kickstart.customer.Group;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
	
	@Autowired
	private CustomerManagement customerManagement;

	private Ticket t;
	private Football f_timeup,f_success;
	private ProductIdentifier id, id_f_timeup,id_f_success;
	private Customer c;
	private UserAccount ua;
	private FootballBet fb;
	private Group group;

	@BeforeEach
	void setUp() {
		c = customerManagement.findByUserAccount(customerManagement.findByEmail("test@tu-dresden.de").get());
		ua = c.getUserAccount();
		group = customerManagement.findByGroupName("initGroup");
		t = new Ticket("name1", LocalDateTime.now().plusDays(4), Money.of(7,EURO), Item.ItemType.TICKET);

		lotteryCatalog.save(t);
		f_timeup = new Football("n", LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(1),
				LocalTime.of(15,0)),Money.of(10,EURO), Item.ItemType.FOOTBALL,
				new Team("team1"),new Team("team2"),"liga","img1","img2");
		f_success = new Football("n", LocalDateTime.of(LocalDateTime.now().toLocalDate().plusDays(4),
				LocalTime.of(15,0)),Money.of(10,EURO), Item.ItemType.FOOTBALL,
				new Team("team1"),new Team("team2"),"liga","img1","img2");
		id = t.getId();
		id_f_timeup = f_timeup.getId();
		id_f_success = f_success.getId();

		fb = new FootballBet(f_success,LocalDateTime.now().minusDays(1),Money.of(12,EURO),c,f_success.getTimeLimit(),Ergebnis.HEIMSIEG);


	}

	@Test
	public void CatalogControllerIntegrationTestTicket(){
		Model model = new ExtendedModelMap();

		String returnedView = catalogController.ticketCatalog(model);

		assertThat(returnedView).isEqualTo("3_catalog_num");

		List<Item> items = lotteryCatalog.findByType(Item.ItemType.TICKET);

		assertThat(items).hasSize(2);

		List <Item> items2 = (List<Item>) model.getAttribute("ticketcatalog");
		assertThat(items2).hasSize(2);
	}

	@Test
	public void CatalogControllerIntegrationTestFootball(){
		Model model = new ExtendedModelMap();
		Optional<UserAccount> opt = Optional.empty();

		String returnedView = catalogController.footballCatalog(model,opt);

		assertThat(returnedView).isEqualTo("2_catalog_foot");

	}

	@Test
	public void CatalogControllerIntegrationTestFootballReg(){
		Model model = new ExtendedModelMap();
		f_success.addBet(fb);
		lotteryCatalog.save(f_success);
		lotteryCatalog.save(f_timeup);

		String returnedView = catalogController.footballCatalog(model, Optional.of(ua));
		List<Item> items = (List<Item>) model.getAttribute("footballcatalog");
		assertThat(returnedView).isEqualTo("2_catalog_foot");
		assertThat(items.contains(f_timeup)).isFalse();
		assertThat(items.contains(f_success)).isFalse();

		lotteryCatalog.delete(f_success);
		lotteryCatalog.delete(f_timeup);
	}

	@Test
	public void CatalogControllerIntegrationTestFootballGroupReg(){
		Model model = new ExtendedModelMap();
		f_success.addBet(fb);
		lotteryCatalog.save(f_success);
		lotteryCatalog.save(f_timeup);

		String returnedView = catalogController.footballCatalogGroup(model, Optional.of(ua));
		List<Item> items = (List<Item>) model.getAttribute("footballcatalog");
		assertThat(returnedView).isEqualTo("catalog_group");
		assertThat(items.contains(f_timeup)).isFalse();
		assertThat(items.contains(f_success)).isTrue();

		lotteryCatalog.delete(f_success);
		lotteryCatalog.delete(f_timeup);
	}

	@Test
	public void CatalogControllerIntegrationTestFootballGroup(){
		Model model = new ExtendedModelMap();
		Optional<UserAccount> opt = Optional.empty();

		String returnedView = catalogController.footballCatalogGroup(model,opt);

		assertThat(returnedView).isEqualTo("catalog_group");
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
		System.out.println(t.getNumberBetsbyCustomer(c));
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
		assertThat(returnView).isEqualTo("not_enough_money");
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
		assertThat(c.getBalance()).isEqualTo(Money.of(43,EURO));
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
		assertThat(c.getBalance()).isEqualTo(Money.of(28,EURO));
		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CatalogControllerFootError(){
		lotteryCatalog.save(f_success);
		String returnView = catalogController.bet_foot(id_f_success,1,12.0,Optional.of(ua));
		assertThat(returnView).isEqualTo("not_enough_money");
		lotteryCatalog.delete(f_success);

	}

	@Test
	public void CatalogControllerFootGroupBet(){
		lotteryCatalog.save(f_success);

		RedirectAttributes redir = new RedirectAttributesModelMap();

		String returnedView = catalogController.bet_foot_group(id_f_success,1,12.0,"initGroup",Optional.of(ua),redir);
		assertThat(returnedView).isEqualTo("redirect:/footballgroup");
		assertThat(redir.getFlashAttributes().containsKey("message1")).isTrue();
		assertThat(redir.getFlashAttributes().get("message1")).isEqualTo("Sie sind nicht teil dieser Gruppe!");
		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CatalogControllerFootGroupBet2(){
		f_success.addGroupBet(fb);
		fb.setGroupName("initGroup");
		lotteryCatalog.save(f_success);
		customerManagement.addMemberToGroup(c,group,group.getPassword());

		RedirectAttributes redir = new RedirectAttributesModelMap();

		String returnedView = catalogController.bet_foot_group(id_f_success,1,12.0,"initGroup",Optional.of(ua),redir);
		assertThat(returnedView).isEqualTo("redirect:/footballgroup");
		assertThat(redir.getFlashAttributes().containsKey("message")).isTrue();
		assertThat(redir.getFlashAttributes().get("message")).isEqualTo("Für dieses Spiel hat die Gruppe schon eine Wette abgegeben.");

		customerManagement.removeMemberOfGroup(c,group);
		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CatalogControllerFootGroupBetTimeUp(){
		lotteryCatalog.save(f_timeup);
		customerManagement.addMemberToGroup(c,group,group.getPassword());
		RedirectAttributes redir = new RedirectAttributesModelMap();

		String returnedView = catalogController.bet_foot_group(id_f_timeup,1,14.0,"initGroup",Optional.of(ua),redir);
		assertThat(returnedView).isEqualTo("time_up.html");

		customerManagement.removeMemberOfGroup(c,group);
		lotteryCatalog.delete(f_timeup);
	}

	@Test
	public void CatalogControllerFootGroupBetSuccess(){
		lotteryCatalog.save(f_success);
		customerManagement.addMemberToGroup(c,group,group.getPassword());
		c.setBalance(Money.of(20,EURO));
		RedirectAttributes redir = new RedirectAttributesModelMap();

		String returnedView = catalogController.bet_foot_group(id_f_success,1,14.0,"initGroup",Optional.of(ua),redir);

		assertThat(returnedView).isEqualTo("redirect:/");
		assertThat(c.getBalance()).isEqualTo(Money.of(6,EURO));
		customerManagement.removeMemberOfGroup(c,group);

		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CatalogControllerFootGroupBetError(){
		lotteryCatalog.save(f_success);
		customerManagement.addMemberToGroup(c,group,group.getPassword());
		RedirectAttributes redir = new RedirectAttributesModelMap();

		String returnedView = catalogController.bet_foot_group(id_f_success,1,14.0,"initGroup",Optional.of(ua),redir);

		assertThat(returnedView).isEqualTo("not_enough_money");
		assertThat(c.getBalance()).isEqualTo(Money.of(0,EURO));

		customerManagement.removeMemberOfGroup(c,group);
		lotteryCatalog.delete(f_success);
	}

	@Test
	public void CheckInsetFoot(){

		c.setBalance(Money.of(40,EURO));

		try {
			f_success.addBet(new FootballBet(f_success,LocalDateTime.now(),Money.of(8,EURO),c,LocalDateTime.now().plusDays(7),Ergebnis.GASTSIEG));
		}
		catch (IllegalArgumentException e){
			assertThat(e.getMessage()).isEqualTo("der Wetteinsatz darf nicht kleiner als der Mindesteinsatz sein");
		}

	}

	@Test
	public void CheckUpdatedBalance(){
		lotteryCatalog.save(f_success);
		c.setBalance(Money.of(40,EURO));

		String returnView = catalogController.bet_foot(id_f_success,1,12.0,Optional.of(ua));

		assertThat(c.getBalance()).isEqualTo(Money.of(28,EURO));



		lotteryCatalog.delete(f_success);

	}

	@AfterEach
	void breakDown(){

		lotteryCatalog.delete(t);

		c.setBalance(Money.of(0,EURO));
		customerRepository.save(c);
	}

}
