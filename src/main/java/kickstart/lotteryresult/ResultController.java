package kickstart.lotteryresult;

import kickstart.catalog.*;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResultController {

	private LotteryCatalog lotteryCatalog;


	public ResultController(LotteryCatalog lotteryCatalog){
		this.lotteryCatalog = lotteryCatalog;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/evalnum")
	public String evalNumberBets(@RequestParam("pid") ProductIdentifier id){
		LocalDate today = LocalDate.now();
		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		if(today.equals(t.getTimeLimit().toLocalDate())) {


			List<NumberBet> wetten = t.getNumberBits();
			List<NumberBet> wetten_valid = new ArrayList<>();

			NumberLottery numlot = new NumberLottery();
			List<Integer> gewinnzahlen = numlot.getWinNumbers();
			int zusatzzahl = numlot.getAdditionalN();
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
				if (!nb.getExpiration().isBefore(t.getTimeLimit()) && !nb.getDate().toLocalDate().equals(today)) {

						wetten_valid.add(nb);
				}else{
					nb.changeStatus(Status.EXPIRED);
				}
			}
			for (NumberBet nb : wetten_valid) {
				if (nb.getNumbers().containsAll(gewinnzahlen) && nb.getAdditionalNum() == zusatzzahl) {
					nb.changeStatus(Status.WIN);
				} else {
					nb.changeStatus(Status.LOSS);
				}
			}
			lotteryCatalog.save(t);

			return "redirect:/";
		}
		return "time_up.html"; //noch ändern zu noch keine Auswertung möglich

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
			} else{
				fb.changeStatus(Status.LOSS);
			}

		}


		lotteryCatalog.save(f);

		return "redirect:/";
	}
}
