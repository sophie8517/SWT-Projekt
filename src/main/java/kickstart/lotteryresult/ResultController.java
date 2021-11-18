package kickstart.lotteryresult;

import kickstart.catalog.LotteryCatalog;
import kickstart.catalog.NumberBet;
import kickstart.catalog.Status;
import kickstart.catalog.Ticket;
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
		//List<Integer> gewinnzahlen = numlot.generate_nums();
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
			}
			else{
				nb.changeStatus(Status.LOSS);
			}
		}
		lotteryCatalog.save(t);

		return "redirect:/";

	}
}
