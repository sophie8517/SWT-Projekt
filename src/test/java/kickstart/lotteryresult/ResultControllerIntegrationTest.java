package kickstart.lotteryresult;

import kickstart.AbstractIntegrationTest;
import kickstart.catalog.*;
import kickstart.customer.*;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.salespointframework.core.Currencies.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



public class ResultControllerIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	ResultController resultController;

	@Autowired
	private LotteryCatalog lotteryCatalog;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerManagement customerManagement;

	@Autowired
	private GroupRepository groupRepository;

	private Ticket t, t2;
	private Football f,f2;
	private ProductIdentifier fid,f2id,tid, tid2;
	private Customer c,c2,ctest;
	private UserAccount ua;
	private FootballBet fb, fgroup;
	private LocalDateTime now = LocalDateTime.now();
	private Money balance,balance_c2,balance_test;
	
	private NumberBet nb, nb2;
	
	private List <Integer> l;
	private Group gruppe;

	@BeforeEach
	void setup(){
		t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		gruppe = customerManagement.findByGroupName("initGroup");
		c = customerRepository.findAll().get().findFirst().get();
		ctest = customerManagement.findByUserAccount(customerManagement.findByEmail("test@tu-dresden.de").get());
		customerManagement.addMemberToGroup(ctest,gruppe,gruppe.getPassword());
		c2 = customerManagement.findByUserAccount(customerManagement.findByEmail("init@leader.de").get());
		ua = c.getUserAccount();
		balance = c.getBalance();
		balance_test = ctest.getBalance();
		balance_c2 = c2.getBalance();
		//----Football------
		f = new Football("n", LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(15,0)), Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("t1"), new Team("t2"),"liga","i1","i2");
		fid = f.getId();
		fb = new FootballBet(f,LocalDateTime.now().minusDays(5),Money.of(15,EURO),c,f.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f.addBet(fb);
		fgroup = new FootballBet(f,LocalDateTime.now().minusDays(5),Money.of(16,EURO),c2,f.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		fgroup.setGroupName("initGroup");
		f.addGroupBet(fgroup);

		f2 = new Football("n2", now.plusMinutes(10), Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("t1"), new Team("t2"),"liga","i1","i2");
		f2id = f2.getId();
		lotteryCatalog.save(f);
		lotteryCatalog.save(f2);

		//------Ticket-------
		t = new Ticket("A", LocalDateTime.now().minusMinutes(5), Money.of(10, EURO), Item.ItemType.TICKET);
		t2 = new Ticket("B", LocalDateTime.now().plusDays(3), Money.of(10, EURO), Item.ItemType.TICKET);
		tid = t.getId();
		tid2 = t2.getId();
		l = new ArrayList<>();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);
		l.add(5);
		l.add(6);
		nb = new NumberBet(t, LocalDateTime.now().minusDays(3), Money.of(10, EURO), c, LocalDateTime.now().plusDays(4), l, 0);
		t.addBet(nb);
		nb2 = new NumberBet(t, LocalDateTime.now().minusDays(8), Money.of(10, EURO), c, LocalDateTime.now().minusDays(1), l, 0);
		t.addBet(nb2);
		lotteryCatalog.save(t);
		lotteryCatalog.save(t2);


	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void EvalFootballBetsTestNotPossible(){

		String returnView = resultController.evalFootballBets(f2id,1);
		assertThat(returnView).isEqualTo("noFootEval");
	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void EvalFootBetsTestSuccess(){
		String returnView = resultController.evalFootballBets(fid,1);
		assertThat(returnView).isEqualTo("redirect:/");
	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CheckStatusWIN(){
		String returnView = resultController.evalFootballBets(fid,3);
		assertThat(fb.getStatus()).isEqualTo(Status.GEWONNEN);
		assertThat(f.getErgebnis()).isEqualTo(Ergebnis.UNENTSCHIEDEN);


	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CheckStatusLOSS(){
		String returnView = resultController.evalFootballBets(fid,2);
		assertThat(fb.getStatus()).isEqualTo(Status.VERLOREN);
		assertThat(f.getErgebnis()).isEqualTo(Ergebnis.GASTSIEG);
	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CheckBalanceSame(){
		String returnView = resultController.evalFootballBets(fid,1);
		assertThat(c.getBalance()).isEqualTo(balance);
	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CheckBalanceHigher(){
		String returnView = resultController.evalFootballBets(fid,3);
		assertThat(c.getBalance()).isEqualTo(balance.add(fb.getInset()));
		assertThat(ctest.getBalance()).isEqualTo(balance_test.add(Money.of(8,EURO)));

	}


	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void EvalNumBetsTestNotPossible(){

		String returnedView = resultController.evalNumberBets(tid2);

		assertThat(returnedView).isEqualTo("keineZiehung");
		}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void EvalNumBetsTestSuccess(){

		String returnedView = resultController.evalNumberBets(tid);
		assertThat(returnedView).isEqualTo("redirect:/");
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void EvalNumBetsTestNotPossible2(){
		String returnedView = resultController.evalNumberBets(tid);
		String returnedView2 = resultController.evalNumberBets(tid);
		//returnedView2 is not "schon_ausgewertet", because after evaluating the bets, the timeLimit of t is set
		// to old_timeLimit + 7 days
		assertThat(returnedView2).isEqualTo("keineZiehung");
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusWIN(){
		resultController.evaluateNum(t, LocalDate.now(), l, 0);
		assertThat(nb.getStatus()).isEqualTo(Status.GEWONNEN);
		assertThat(c.getBalance()).isEqualTo(balance.add(nb.getInset()));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusLOSS(){
		resultController.evaluateNum(t, LocalDate.now(), l, 1);
		assertThat(nb.getStatus()).isEqualTo(Status.VERLOREN);
		assertThat(c.getBalance()).isEqualTo(balance);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusEXPIRED(){
		resultController.evaluateNum(t, LocalDate.now(), l, 2);
		assertThat(nb2.getStatus()).isEqualTo(Status.ABGELAUFEN);
	}

	//TODO
	//vielleicht Dauerauftrag testen

	@AfterEach
	void breakDown(){
		lotteryCatalog.delete(f);
		lotteryCatalog.delete(f2);

		lotteryCatalog.delete(t);
		lotteryCatalog.delete(t2);
		//fb.setTippedStatus(Ergebnis.LEER);
		c.setBalance(balance); //reset balance to balance before executing test
		ctest.setBalance(balance_test);
		c2.setBalance(balance_c2);
		customerManagement.removeMemberOfGroup(ctest,gruppe);
		customerRepository.save(c);
		customerRepository.save(ctest);
		customerRepository.save(c2);
	

		
		
	}

}
