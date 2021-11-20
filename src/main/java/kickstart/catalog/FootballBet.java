package kickstart.catalog;

import kickstart.customer.Customer;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FootballBet extends Bet {
	private Status tippedStatus;
	@OneToOne (cascade = CascadeType.ALL)
	private Team host, guest;

	public FootballBet(Item item, LocalDateTime date, Money einsatz, Customer customer, Status tippedStatus){
		super(item, date, einsatz, customer);
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
		return "Heim: " + f.getHost() +"\n Gast: " + f.getGuest() + "\nDatum: " + getDate() + "\nTipp: "+ getTip() + "\n";
	}

	public Team getHost(){
		return host;

	}
	public Team getGuest(){
		return guest;
	}


}
