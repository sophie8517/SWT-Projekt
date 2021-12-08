package kickstart.order;

import kickstart.AbstractIntegrationTest;
import kickstart.catalog.*;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import kickstart.lotteryresult.ResultController;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.EURO;
public class OrderControllerIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	private LotteryCatalog lotteryCatalog;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderController orderController;

	@Autowired
	ResultController resultController;

	private Customer c;
	private UserAccount ua;
	private Ticket t;
	private List<Integer> l;
	private ProductIdentifier fid,f2id,f3id,f4id;
	private Football f,f2,f3,f4;
	private FootballBet fb,fb2,fb3,fb4;
	private String fb_id, fb2_id,fb3_id,fb4_id;
	private Money balance;


	@BeforeEach
	void setUp(){
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
		balance = c.getBalance();
		t = new Ticket("name1", LocalDateTime.of(LocalDate.of(2021,12,5),
				LocalTime.of(15,0)), Money.of(7,EURO), Item.ItemType.TICKET);
		lotteryCatalog.save(t);
		l = new ArrayList<>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(6);


		//------------
		f = new Football("abc",LocalDateTime.now().plusMinutes(2),Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("t1"),new Team("t2"),"liga","i1","i2");
		fb = new FootballBet(f,LocalDateTime.now().minusDays(3),Money.of(10,EURO),c,f.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f.addBet(fb);
		fid = f.getId();
		fb_id = fb.getIdstring();
		lotteryCatalog.save(f);

		f2 = new Football("def",LocalDateTime.now().plusDays(1),Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("team1"),new Team("team2"),"2. liga","img1","img2");
		fb2 = new FootballBet(f2,LocalDateTime.now().minusDays(2),Money.of(12,EURO),c,f2.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f2.addBet(fb2);
		f2id = f2.getId();
		fb2_id = fb2.getIdstring();
		lotteryCatalog.save(f2);

		f3 = new Football("ghi",LocalDateTime.now().plusMinutes(5),Money.of(12,EURO), Item.ItemType.FOOTBALL,new Team("host"),new Team("guest"),"1. liga","imgh1","imgg2");
		fb3 = new FootballBet(f3,LocalDateTime.now().minusDays(3),Money.of(14,EURO),c,f.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f3.addBet(fb3);
		f3id = f3.getId();
		fb3_id = fb3.getIdstring();
		lotteryCatalog.save(f3);

		f4 = new Football("ghi",LocalDateTime.now().minusMinutes(90),Money.of(12,EURO), Item.ItemType.FOOTBALL,new Team("winner"),new Team("loser"),"1. liga","imgw","imgl");
		fb4 = new FootballBet(f4,LocalDateTime.now().minusDays(3),Money.of(14,EURO),c,f4.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f4.addBet(fb4);
		f4id = f4.getId();
		fb4_id = fb4.getIdstring();
		lotteryCatalog.save(f4);

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ViewBetsTest(){
		NumberBet nb = new NumberBet(t, LocalDateTime.now(), Money.of(t.getPrice2(),EURO),c,LocalDateTime.now().plusDays(7),l,9);
		t.addBet(nb);
		lotteryCatalog.save(t);

		Model model = new ExtendedModelMap();
		String returnView = orderController.viewBets(model, Optional.of(ua));
		assertThat(returnView).isEqualTo("customer_bets");
		List<NumberBet> bets = (List<NumberBet>) model.getAttribute("numberBets");
		assertThat(bets.size()).isEqualTo(1);

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestTimeUp(){

		String returnView = orderController.raiseFootBet(fid,fb_id,12.0);
		assertThat(returnView).isEqualTo("time_up.html");
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestError(){
		c.setBalance(Money.of(2,EURO));
		String returnView = orderController.raiseFootBet(f2id,fb2_id,15.0);
		assertThat(returnView).isEqualTo("keinGeld");

		//balance doesn't change if customer has not enough money
		assertThat(c.getBalance()).isEqualTo(Money.of(2,EURO));
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestSuccess(){
		c.setBalance(Money.of(10,EURO));
		String returnView = orderController.raiseFootBet(f2id,fb2_id,15.0);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(fb2.getInset()).isEqualTo(Money.of(15,EURO));
		assertThat(c.getBalance()).isEqualTo(Money.of(7,EURO));
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeFootTest(){
		Model model = new ExtendedModelMap();
		String returnView = orderController.changeFoot(model,f2id,fb2_id);
		assertThat(returnView).isEqualTo("changeFootTip.html");
		FootballBet footbet = (FootballBet) model.getAttribute("footbet");
		assertThat(footbet).isEqualTo(fb2);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeFootBetTipTestTimeUp(){
		String returnView = orderController.changeFootbetTip(fid,fb_id,2);
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(fb.getTip()).isEqualTo(Ergebnis.UNENTSCHIEDEN);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeFootBetTipTestSuccess(){
		String returnView = orderController.changeFootbetTip(f2id,fb2_id,2);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(fb2.getTip()).isEqualTo(Ergebnis.HEIMSIEG);

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestTimeUp(){
		String returnView = orderController.removeFootballBets(fid,fb_id);
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f.getFootballBets().contains(fb)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestStatusOPEN(){
		String returnView = orderController.removeFootballBets(f2id,fb2_id);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance.add(fb2.getInset()));
		assertThat(f2.getFootballBets().contains(fb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestStatusSuccess(){
		fb2.changeStatus(Status.LOSS);
		String returnView = orderController.removeFootballBets(f2id,fb2_id);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance);
		assertThat(f2.getFootballBets().contains(fb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestTimeUp5Minutes(){
		String returnView = orderController.removeFootballBets(f3id,fb3_id);
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f3.getFootballBets().contains(fb3)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);

	}

	@Test
	@WithMockUser(roles={"ADMIN","CUSTOMER"})
	public void RemoveFootballBetsTestAfterEvaluation(){

		String returnView = orderController.removeFootballBets(f4id,fb4_id);
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f4.getFootballBets().contains(fb4)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);

		String myview = resultController.evalFootballBets(f4id,2);
		//Ergebnis von f4 wurde auf 3 = UNENTSCHIEDEN gesetzt
		//Status von fb4 ist nicht mehr OPEN
		String resultView = orderController.removeFootballBets(f4id,fb4_id);
		assertThat(resultView).isEqualTo("redirect:/customer_bets");
		assertThat(f4.getFootballBets().contains(fb4)).isFalse();
		assertThat(c.getBalance()).isEqualTo(balance);

	}

	@AfterEach
	void breakDown(){

		lotteryCatalog.delete(t);
		lotteryCatalog.delete(f);
		lotteryCatalog.delete(f2);
		lotteryCatalog.delete(f3);
		lotteryCatalog.delete(f4);
		c.setBalance(balance);
		customerRepository.save(c);
	}


}
