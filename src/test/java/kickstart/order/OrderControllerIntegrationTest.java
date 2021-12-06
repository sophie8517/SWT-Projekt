package kickstart.order;

import kickstart.AbstractIntegrationTest;
import kickstart.catalog.*;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
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

	private Customer c;
	private UserAccount ua;
	private Ticket t;
	private List<Integer> l;
	private ProductIdentifier fid;
	private Football f;
	private FootballBet fb;
	private String fb_id;

	@BeforeEach
	void setUp(){
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
		t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
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

	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void ViewBetsTest(){
		Model model = new ExtendedModelMap();
		String returnView = orderController.viewBets(model, Optional.of(ua));
		assertThat(returnView).isEqualTo("customer_bets");
		List<NumberBet> bets = (List<NumberBet>) model.getAttribute("numberBets");
		assertThat(bets.size()).isEqualTo(0);
		NumberBet nb = new NumberBet(t, LocalDateTime.now(), Money.of(t.getPrice2(),EURO),c,LocalDateTime.now().plusDays(7),l,9);
	}

	@Test
	@WithMockUser(username = "test", roles = "CUSTOMER")
	public void RaiseFootBetTest(){

		String returnView = orderController.raiseFootBet(fid,fb_id,12.0);
		assertThat(returnView).isEqualTo("time_up.html");
	}

	@AfterEach
	void breakDown(){
		List<NumberBet> mybets = t.getNumberBits();
		for(NumberBet nb: mybets){
			t.removeBet(nb);
		}
		lotteryCatalog.save(t);
		lotteryCatalog.delete(f);
	}


}
