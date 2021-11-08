package kickstart.catalog;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class FootballBet extends Bet {
	private String tip;

	public FootballBet(Item item, int id, LocalDateTime date, double einsatz, String tip){
		super(item, id, date, einsatz);
		this.tip = tip;
	}

	public FootballBet() {

	}
	//public void setTip(String tip){
	//	this.tip = tip;
	//}

	public String getTip() {
		return tip;
	}
}
