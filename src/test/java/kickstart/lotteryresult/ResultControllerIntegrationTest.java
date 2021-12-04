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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.salespointframework.core.Currencies.*;


public class ResultControllerIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	CatalogController catalogController;

	@Autowired
	private ResultController resultController;

	@Autowired
	private LotteryCatalog lotteryCatalog;



	@Autowired
	private CustomerRepository customerRepository;

	private Ticket t, t2;
	private ProductIdentifier tid, tid2;
	private NumberBet nb, nb2;
	private Customer c;
	private UserAccount ua;
	private List <Integer> l;


	@BeforeEach
	void setup() {
		t = new Ticket("A", LocalDateTime.now(), Money.of(10, EURO), Item.ItemType.TICKET);
		t2 = new Ticket("B", LocalDateTime.now().plusDays(3), Money.of(10, EURO), Item.ItemType.TICKET);
		c = customerRepository.findAll().get().findFirst().get();
		ua = c.getUserAccount();
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
		assertThat(returnedView2).isEqualTo("schon_ausgewertet");
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusWIN(){
		resultController.evaluateNum(t, LocalDate.now(), l, 0);
		assertThat(nb.getStatus()).isEqualTo(Status.WIN);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusLOSS(){
		resultController.evaluateNum(t, LocalDate.now(), l, 1);
		assertThat(nb.getStatus()).isEqualTo(Status.LOSS);
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void checkNumBetsStatusEXPIRED(){
		resultController.evaluateNum(t, LocalDate.now(), l, 2);
		assertThat(nb2.getStatus()).isEqualTo(Status.EXPIRED);
	}

	@AfterEach
	void breakDown(){
		lotteryCatalog.delete(t);
		lotteryCatalog.delete(t2);
		c.setBalance(Money.of(0, EURO));
		customerRepository.save(c);
	}

}