package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class FootballBet extends Bet {
	private String tip,guest, host;

	public FootballBet(Item item,  LocalDateTime date, Money einsatz, String tip){
		super(item, date, einsatz);
		this.tip = tip;
		Football f = (Football) item;
		this.host = f.getHost();
		this.guest = f.getGuest();
	}

	public FootballBet() {

	}
	//public void setTip(String tip){
	//	this.tip = tip;
	//}

	public String getTip() {
		return tip;
	}

	@Override
	public String toString(){
		Football f = (Football) getItem();
		return "Heim: " + f.getHost() +"\n Gast: " + f.getGuest() + "\nDatum: " + getDate() + "\nTipp: "+ getTip();
	}

	public String getHost(){
		return host;

	}
	public String getGuest(){
		return guest;

	}
}
