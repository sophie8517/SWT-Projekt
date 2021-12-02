package kickstart.lotteryresult;

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

import static org.assertj.core.api.Assertions.as;
import static org.salespointframework.core.Currencies.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ResultControllerIntegrationTest extends AbstractIntegrationTest {
	@Autowired
	ResultController resultController;

	@Autowired
	private LotteryCatalog lotteryCatalog;

	@Autowired
	private CustomerRepository customerRepository;

	private Ticket t;
	private Football f,f2;
	private ProductIdentifier fid,f2id;
	private Customer c;
	private UserAccount ua;
	private FootballBet fb;
	private LocalDateTime now = LocalDateTime.now();
	private Money balance;

	@BeforeEach
	void setup(){
		t = (Ticket) lotteryCatalog.findByType(Item.ItemType.TICKET).get(0);
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
		balance = c.getBalance();
		f = new Football("n", LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(15,0)), Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("t1"), new Team("t2"),"liga","i1","i2");
		fid = f.getId();
		fb = new FootballBet(f,LocalDateTime.now().minusDays(5),Money.of(15,EURO),c,f.getTimeLimit(),Ergebnis.UNENTSCHIEDEN);
		f.addBet(fb);
		f2 = new Football("n2", now.plusMinutes(10), Money.of(10,EURO), Item.ItemType.FOOTBALL,new Team("t1"), new Team("t2"),"liga","i1","i2");
		f2id = f2.getId();
		lotteryCatalog.save(f);
		lotteryCatalog.save(f2);
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
		assertThat(fb.getStatus()).isEqualTo(Status.WIN);
		assertThat(f.getErgebnis()).isEqualTo(Ergebnis.UNENTSCHIEDEN);


	}
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void CheckStatusLOSS(){
		String returnView = resultController.evalFootballBets(fid,1);
		assertThat(fb.getStatus()).isEqualTo(Status.LOSS);
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
	}

	@AfterEach
	void breakDown(){
		lotteryCatalog.delete(f);
		lotteryCatalog.delete(f2);
		//fb.setTippedStatus(Ergebnis.LEER);
		c.setBalance(balance); //reset balance to balance before executing test
		customerRepository.save(c);
	}
}
