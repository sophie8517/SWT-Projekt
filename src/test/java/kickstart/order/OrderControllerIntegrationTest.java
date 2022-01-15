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

import java.time.LocalDateTime;
import java.util.*;

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
	private ProductIdentifier fid,f2id,f3id,f4id,tid,tid2,tid3;
	private Football f,f2,f3,f4;
	private FootballBet fb,fb2,fb3,fb4;
	private String fb_id, fb2_id,fb3_id,fb4_id,nb_id,nb2_id,nb3_id;
	private Ticket t, t2, t3;
	private NumberBet nb,nb2, nb3;
	private List<Integer> l;
	private Set<Integer> s;
	private Money balance;


	@BeforeEach
	void setUp(){
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
		balance = c.getBalance();

		//------------footBet
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

		f4 = new Football("ghi2",LocalDateTime.now().minusMinutes(90),Money.of(12,EURO), Item.ItemType.FOOTBALL,new Team("winner"),new Team("loser"),"1. liga","imgw","imgl");
		fb4 = new FootballBet(f4,LocalDateTime.now().minusDays(3),Money.of(14,EURO),c,f4.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f4.addBet(fb4);
		f4id = f4.getId();
		fb4_id = fb4.getIdstring();
		lotteryCatalog.save(f4);

		//------------NumberBet
		t = new Ticket("A", LocalDateTime.now().plusMinutes(2), Money.of(10, EURO), Item.ItemType.TICKET);
		nb = new NumberBet(t, LocalDateTime.now().minusDays(3), Money.of(10, EURO), c, t.getTimeLimit(), l, 0);
		t.addBet(nb);
		tid = t.getId();
		nb_id = nb.getIdstring();
		lotteryCatalog.save(t);

		l = new ArrayList<>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(6);

		//success
		t2 = new Ticket("B", LocalDateTime.now().plusDays(1), Money.of(10, EURO), Item.ItemType.TICKET);
		nb2 = new NumberBet(t2, LocalDateTime.now().minusDays(2), Money.of(10, EURO), c, t2.getTimeLimit(), l, 0);
		t2.addBet(nb2);
		tid2 = t2.getId();
		nb2_id = nb2.getIdstring();
		lotteryCatalog.save(t2);

		//time_up
		t3 = new Ticket("C",LocalDateTime.now().plusMinutes(5), Money.of(10, EURO), Item.ItemType.TICKET);
		nb3 = new NumberBet(t3, LocalDateTime.now().minusDays(3), Money.of(12, EURO), c, t3.getTimeLimit(), l, 0);
		t3.addBet(nb3);
		tid3 = t3.getId();
		nb3_id = nb3.getIdstring();
		lotteryCatalog.save(t3);
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
		assertThat(bets.size()).isEqualTo(4);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestTimeUp(){

		String returnView = orderController.raiseFootBet(fid,fb_id,12.0,Optional.of(ua));
		assertThat(returnView).isEqualTo("time_up.html");
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestError(){
		c.setBalance(Money.of(2,EURO));
		String returnView = orderController.raiseFootBet(f2id,fb2_id,15.0, Optional.of(ua));
		assertThat(returnView).isEqualTo("error");

		//balance doesn't change if customer has not enough money
		assertThat(c.getBalance()).isEqualTo(Money.of(2,EURO));
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTestSuccess(){
		c.setBalance(Money.of(10,EURO));
		String returnView = orderController.raiseFootBet(f2id,fb2_id,15.0, Optional.of(ua));
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
		assertThat(fb2.getTip()).isEqualTo(Ergebnis.GASTSIEG);

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestTimeUp(){
		String returnView = orderController.removeFootballBets(fid,fb_id,Optional.of(ua));
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f.getFootballBets().contains(fb)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestStatusOPEN(){
		String returnView = orderController.removeFootballBets(f2id,fb2_id, Optional.of(ua));
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance.add(fb2.getInset()));
		assertThat(f2.getFootballBets().contains(fb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestStatusSuccess(){
		fb2.changeStatus(Status.VERLOREN);
		String returnView = orderController.removeFootballBets(f2id,fb2_id, Optional.of(ua));
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance);
		assertThat(f2.getFootballBets().contains(fb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveFootballBetsTestTimeUp5Minutes(){
		String returnView = orderController.removeFootballBets(f3id,fb3_id, Optional.of(ua));
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f3.getFootballBets().contains(fb3)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);

	}

	@Test
	@WithMockUser(roles={"ADMIN","CUSTOMER"})
	public void RemoveFootballBetsTestAfterEvaluation(){

		String returnView = orderController.removeFootballBets(f4id,fb4_id, Optional.of(ua));
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(f4.getFootballBets().contains(fb4)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);

		String myview = resultController.evalFootballBets(f4id,2);
		//Ergebnis von f4 wurde auf 3 = UNENTSCHIEDEN gesetzt
		//Status von fb4 ist nicht mehr OPEN
		String resultView = orderController.removeFootballBets(f4id,fb4_id,Optional.of(ua));
		assertThat(resultView).isEqualTo("redirect:/customer_bets");
		assertThat(f4.getFootballBets().contains(fb4)).isFalse();
		assertThat(c.getBalance()).isEqualTo(balance);

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseNumBetTestSuccess(){
		Model model = new ExtendedModelMap();
		c.setBalance(Money.of(10,EURO));
		String returnView = orderController.raiseNumBet(model, tid2,nb2_id,15.0);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(nb2.getInset()).isEqualTo(Money.of(15,EURO));
		assertThat(c.getBalance()).isEqualTo(Money.of(5,EURO));
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseNumBetTestError(){
		Model model = new ExtendedModelMap();
		c.setBalance(Money.of(2,EURO));
		String returnView = orderController.raiseNumBet(model, tid2, nb2_id,15.0);
		assertThat(returnView).isEqualTo("error");

		//balance doesn't change if customer has not enough money
		assertThat(c.getBalance()).isEqualTo(Money.of(2,EURO));
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseNumBetTestTimeUp(){
		Model model = new ExtendedModelMap();
		String returnView = orderController.raiseNumBet(model, tid3, nb3_id, 12.0);
		assertThat(returnView).isEqualTo("time_up.html");
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeNumsTest(){
		Model model = new ExtendedModelMap();
		String returnView = orderController.changeNums(model,tid2,nb2_id);
		assertThat(returnView).isEqualTo("changeNumTip.html");
		NumberBet numBet = (NumberBet) model.getAttribute("numbet");
		assertThat(numBet).isEqualTo(nb2);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeNumBetTipTestSuccess(){
		String returnView = orderController.changeNumbetTip(tid2, nb2_id, 2, 3, 4, 5, 6, 7, 0);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeNumBetTipTestWrongInput(){

			String returnView = orderController.changeNumbetTip(tid2, nb2_id, 2, 2, 4, 5, 6, 7, 0);
			assertThat(returnView).isEqualTo("wronginput.html");

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ChangeNumBetTipTestTimeUp(){
		Model model = new ExtendedModelMap();
		String returnView = orderController.changeNumbetTip(tid3, nb3_id, 1, 2, 3, 4, 5, 6, 7);
		assertThat(returnView).isEqualTo("time_up.html");

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveNumberBetsTestStatusSuccess(){
		nb2.changeStatus(Status.VERLOREN);
		String returnView = orderController.removeNumberBets(tid2,nb2_id);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance);
		assertThat(t2.getNumberBits().contains(nb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveNumberBetsTestStatusOPEN(){
		String returnView = orderController.removeNumberBets(tid2,nb2_id);
		assertThat(returnView).isEqualTo("redirect:/customer_bets");
		assertThat(c.getBalance()).isEqualTo(balance.add(nb2.getInset()));
		assertThat(t2.getNumberBits().contains(nb2)).isFalse();

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RemoveNumberBetsTestTimeUp(){
		String returnView = orderController.removeNumberBets(tid,nb_id);
		assertThat(returnView).isEqualTo("time_up.html");
		assertThat(t.getNumberBits().contains(nb)).isTrue();
		assertThat(c.getBalance()).isEqualTo(balance);
	}




	@AfterEach
	void breakDown(){
		lotteryCatalog.delete(f);
		lotteryCatalog.delete(f2);
		lotteryCatalog.delete(f3);
		lotteryCatalog.delete(f4);
		lotteryCatalog.delete(t);
		lotteryCatalog.delete(t2);
		lotteryCatalog.delete(t3);
		c.setBalance(balance);
		customerRepository.save(c);
	}


}
