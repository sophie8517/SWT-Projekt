package kickstart.lotteryresult;

import kickstart.catalog.*;
import kickstart.customer.Customer;
import kickstart.customer.CustomerManagement;
import kickstart.customer.CustomerRepository;
import kickstart.customer.Group;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.salespointframework.core.Currencies.EURO;

@Controller
public class ResultController {

	private LotteryCatalog lotteryCatalog;
	private CustomerRepository customerRepository;
	private CustomerManagement customerManagement;


	public ResultController(LotteryCatalog lotteryCatalog, CustomerRepository customerRepository,
							CustomerManagement customerManagement){
		this.lotteryCatalog = lotteryCatalog;
		this.customerRepository = customerRepository;
		this.customerManagement = customerManagement;
	}


	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/evalnum")
	public String evalNumberBets(@RequestParam("pid") ProductIdentifier id){
		LocalDateTime todaytime = LocalDateTime.now();
		LocalDate today = todaytime.toLocalDate();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();


		if(todaytime.isAfter(t.getTimeLimit())) {

			if(t.getCheckEvaluation().contains(t.getTimeLimit().toLocalDate())){
				return "schon_ausgewertet";
			}
			NumberLottery numlot = new NumberLottery();
			List<Integer> gewinnzahlen = numlot.generate_nums();
			int zusatzzahl = numlot.generateAdditionalNumber();

			evaluateNum(t,today, gewinnzahlen,zusatzzahl);
			return "redirect:/";

			

		}
		return "keineZiehung"; //noch ändern zu noch keine Auswertung möglich

	}

	public List<NumberBet> validBets(Ticket t, LocalDate today){
		List<NumberBet> result = new ArrayList<>();
		List<NumberBet> allbets = t.getNumberBits();

		for (NumberBet nb : allbets) {
			if (!nb.getExpiration().isBefore(t.getTimeLimit())) {
				if(!nb.getDate().toLocalDate().equals(today)) {
					result.add(nb);
				}
			}else{
				nb.changeStatus(Status.EXPIRED);
			}
		}
		lotteryCatalog.save(t);
		return result;
	}

	public boolean compareNumbers(List<Integer> userzahlen, List<Integer> lottozahlen){

		for(int i: userzahlen){
			if(!lottozahlen.contains(i)){
				return false;
			}
		}
		return true;
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void evaluateNum(Ticket t, LocalDate today, List<Integer> gewinnzahlen, int zusatzzahl){


		t.setWinNumbers(gewinnzahlen);
		t.setAdditionalNumber(zusatzzahl);

		List<NumberBet> wetten_valid = validBets(t,today);


		for (NumberBet nb : wetten_valid) {

			if (compareNumbers(nb.getNumbers(),gewinnzahlen) && nb.getAdditionalNum() == zusatzzahl) {
				nb.changeStatus(Status.WIN);

				Customer c = nb.getCustomer();
				Money bal = c.getBalance().add(nb.getInset());
				c.setBalance(bal);
				customerRepository.save(c);
			} else {
				nb.changeStatus(Status.LOSS);
			}
		}

		t.addCheck(t.getTimeLimit().toLocalDate());
		LocalDateTime tm = t.getTimeLimit().plusDays(7);
		t.setTimeLimit(tm);

		lotteryCatalog.save(t);
		System.out.println("valide wetten:");
		System.out.println(wetten_valid);
		System.out.println(t.getNumberBits());
	}

	public void changeStatusSingleBet(Football f, FootballBet fb, Ergebnis erg){
		if(!fb.getExpiration().isBefore(f.getTimeLimit())){
			if(fb.getTip().equals(erg)){
				fb.changeStatus(Status.WIN);
				Customer c = fb.getCustomer();
				Money bal = c.getBalance().add(fb.getInset());
				c.setBalance(bal);
				customerRepository.save(c);
			} else{
				fb.changeStatus(Status.LOSS);
			}
		} else{
			fb.changeStatus(Status.EXPIRED);

		}
		lotteryCatalog.save(f);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/evalfoot")
	public String evalFootballBets(@RequestParam("pid") ProductIdentifier id, @RequestParam("ergebnis") int number){
		Football f = (Football) lotteryCatalog.findById(id).get();

		if(LocalDateTime.now().isAfter(f.getTimeLimit()) && f.getErgebnis().equals(Ergebnis.LEER)){
			List<FootballBet> wetten = f.getFootballBets();
			List<FootballBet> gruppenwetten = f.getGroupFootballBets();
			//List<FootballBet> wetten_valid = new ArrayList<>();


			Ergebnis erg;
			switch (number){
				case 1:erg = Ergebnis.GASTSIEG;
				break;
				case 2:erg = Ergebnis.HEIMSIEG;
				break;
				case 3:erg = Ergebnis.UNENTSCHIEDEN;
				break;
				default:throw new IllegalArgumentException("number must be 1,2 or 3");
			}

			f.setErgebnis(erg);

			for(FootballBet fb: wetten){
				//ist das jetzt überflüssig, weil die zeitgrenze beim abgeben der wetten eingestellt ist
				changeStatusSingleBet(f,fb,erg);
			}

			evalGroupBets(f,erg);



			lotteryCatalog.save(f);

			return "redirect:/";

		}
		return "noFootEval";

	}
	public void changeStatusGroupBet(Football f, FootballBet fb, Ergebnis erg){
		if(!fb.getExpiration().isBefore(f.getTimeLimit())){
			Group gruppe = customerManagement.findByGroupName(fb.getGroupName());
			Set<Customer> customers = gruppe.getMembers();
			if(fb.getTip().equals(erg)){
				fb.changeStatus(Status.WIN);

				double value = fb.getEinsatz2()/customers.size();
				value = Math.round((value * 100.0)/100.0);
				for(Customer c :customers){
					Money bal = c.getBalance().add(Money.of(value,EURO));

					c.setBalance(bal);
					customerRepository.save(c);
				}

			} else{
				fb.changeStatus(Status.LOSS);
			}
		}else{
			fb.changeStatus(Status.EXPIRED);

		}
		lotteryCatalog.save(f);
	}

	public void evalGroupBets(Football f, Ergebnis erg){
		List<FootballBet> gruppenwetten = f.getGroupFootballBets();

		for(FootballBet fb: gruppenwetten){
			//ist das jetzt überflüssig, weil die zeitgrenze beim abgeben der wetten eingestellt ist
			changeStatusGroupBet(f,fb,erg);
		}
		lotteryCatalog.save(f);
	}
}
