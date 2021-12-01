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
	@PostMapping("/eval/num")
	public String evalNumberBets(@RequestParam("pid") ProductIdentifier id){
		Ticket t = (Ticket) lotteryCatalog.findById(id).get();
		List<NumberBet> wetten = t.getNumberBits();
		List<NumberBet> wetten_valid = new ArrayList<>();

		NumberLottery numlot = new NumberLottery();
		//List<Integer> gewinnzahlen = numlot.getWinNumbers();
		List<Integer> gewinnzahlen = new ArrayList<>();
		gewinnzahlen.add(1);
		gewinnzahlen.add(2);
		gewinnzahlen.add(3);
		gewinnzahlen.add(4);
		gewinnzahlen.add(5);
		gewinnzahlen.add(6);

		for(NumberBet nb: wetten){
			if(!nb.getExpiration().isBefore(LocalDate.now())){
				wetten_valid.add(nb);
			}
		}
		for(NumberBet nb: wetten_valid){
			if(nb.getNumbers().containsAll(gewinnzahlen)){
				nb.changeStatus(Status.WIN);
			} else{
				nb.changeStatus(Status.LOSS);
			}
		}
		lotteryCatalog.save(t);

		return "redirect:/";

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/eval/foot")
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
			if(!fb.getExpiration().isBefore(LocalDate.now())){
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
