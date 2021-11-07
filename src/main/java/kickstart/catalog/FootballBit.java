package kickstart.catalog;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class FootballBit extends Bit{
	private String tip;

	public FootballBit(Item item, int id, LocalDateTime date, double einsatz, String tip){
		super(item, id, date, einsatz);
		this.tip = tip;
	}

	public FootballBit() {

	}
	public void setTip(String tip){
		this.tip = tip;
	}

	public String getTip() {
		return tip;
	}
}
