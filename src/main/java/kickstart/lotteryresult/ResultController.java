package kickstart.lotteryresult;

import kickstart.catalog.*;
import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
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

@Controller
public class ResultController {

	private LotteryCatalog lotteryCatalog;
	private CustomerRepository customerRepository;


	public ResultController(LotteryCatalog lotteryCatalog, CustomerRepository customerRepository){
		this.lotteryCatalog = lotteryCatalog;
		this.customerRepository = customerRepository;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/evalnum")
	public String evalNumberBets(@RequestParam("pid") ProductIdentifier id, @RequestParam("today")LocalDateTime todaytime){
		LocalDate today = todaytime.toLocalDate();

		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		if(todaytime.isAfter(t.getTimeLimit().minusSeconds(1)) && todaytime.isBefore(t.getTimeLimit().plusMinutes(30))) {


			List<NumberBet> wetten = t.getNumberBits();
			List<NumberBet> wetten_valid = new ArrayList<>();

			NumberLottery numlot = new NumberLottery();
			List<Integer> gewinnzahlen = numlot.generate_nums();
			int zusatzzahl = numlot.generateAdditionalNumber();
			t.setWinNumbers(gewinnzahlen);
			t.setAdditionalNumber(zusatzzahl);
		/*
		List<Integer> gewinnzahlen = new ArrayList<>();
		gewinnzahlen.add(1);
		gewinnzahlen.add(2);
		gewinnzahlen.add(3);
		gewinnzahlen.add(4);
		gewinnzahlen.add(5);
		gewinnzahlen.add(6);

		 */

			for (NumberBet nb : wetten) {
				if (!nb.getExpiration().isBefore(t.getTimeLimit()) && !nb.getDate().toLocalDate().equals(today) && nb.getStatus().equals(Status.OPEN)) {

						wetten_valid.add(nb);
				}else{
					nb.changeStatus(Status.EXPIRED);
				}
			}
			for (NumberBet nb : wetten_valid) {
				if (nb.getNumbers().containsAll(gewinnzahlen) && nb.getAdditionalNum() == zusatzzahl) {
					nb.changeStatus(Status.WIN);
					Customer c = nb.getCustomer();
					Money bal = c.getBalance().add(nb.getInset());
					c.setBalance(bal);
					customerRepository.save(c);
				} else {
					nb.changeStatus(Status.LOSS);
				}
			}

			lotteryCatalog.save(t);

			return "redirect:/";
		}
		return "keineZiehung.html"; //noch ändern zu noch keine Auswertung möglich

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/evalfoot")
	String evalFootballBets(@RequestParam("pid") ProductIdentifier id, @RequestParam("ergebnis") int number){
		Football f = (Football) lotteryCatalog.findById(id).get();
		List<FootballBet> wetten = f.getFootballBets();
		List<FootballBet> wetten_valid = new ArrayList<>();


		Ergebnis erg;
		if(number == 1){

			erg = Ergebnis.GASTSIEG;
		} else if(number == 2){

			erg = Ergebnis.HEIMSIEG;
		} else{

			erg = Ergebnis.UNENTSCHIEDEN;
		}
		f.setErgebnis(erg);

		for(FootballBet fb: wetten){
			//ist das jetzt überflüssig, weil die zeitgrenze beim abgeben der wetten eingestellt ist
			if(!fb.getExpiration().isBefore(fb.getItem().getTimeLimit())){
				wetten_valid.add(fb);
			} else{
				fb.changeStatus(Status.EXPIRED);

			}
		}
		for(FootballBet fb: wetten_valid){
			if(fb.getTip().equals(erg)){
				fb.changeStatus(Status.WIN);
				Customer c = fb.getCustomer();
				Money bal = c.getBalance().add(fb.getInset());
				c.setBalance(bal);
				customerRepository.save(c);
			} else{
				fb.changeStatus(Status.LOSS);
			}

		}


		lotteryCatalog.save(f);

		return "redirect:/";
	}
}
