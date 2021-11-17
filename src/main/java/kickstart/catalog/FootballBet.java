package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class FootballBet extends Bet {
	private Status tippedStatus;
	@OneToOne
	private Team guest, host;


	public FootballBet(Item item,  LocalDateTime date, Money einsatz, Status tippedStatus){
		super(item, date, einsatz);
		this.tippedStatus = tippedStatus;
		Football f = (Football) item;
		this.host = f.getHost();
		this.guest = f.getGuest();
	}

	public FootballBet() {

	}
	//public void setTip(String tip){
	//	this.tip = tip;
	//}

	public Status getTip() {
		return tippedStatus;
	}

	@Override
	public String toString(){
		Football f = (Football) getItem();
		return "Heim: " + f.getHost() +"\n Gast: " + f.getGuest() + "\nDatum: " + getDate() + "\nTipp: "+ getTip();
	}

	public Team getHost(){
		return host;

	}
	public Team getGuest(){
		return guest;
	}

}
