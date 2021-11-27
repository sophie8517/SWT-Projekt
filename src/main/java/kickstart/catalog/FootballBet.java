package kickstart.catalog;

import kickstart.customer.Customer;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class FootballBet extends Bet {
	private Ergebnis tippedStatus;
	//@OneToOne (cascade = CascadeType.ALL)
	//private Team host, guest;

<<<<<<< HEAD
	public FootballBet(Item item, LocalDateTime date, Money einsatz, Customer customer, LocalDateTime expiration,
=======
	public FootballBet(Item item, LocalDateTime date, Money einsatz, Customer customer, LocalDate expiration,
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
					   Ergebnis tippedStatus){
		super(item, date, einsatz, customer, expiration);
		this.tippedStatus = tippedStatus;
		Football f = (Football) item;
		//this.host = f.getHost();
		//this.guest = f.getGuest();
	}

	public FootballBet() {

	}
	//public void setTip(String tip){
	//	this.tip = tip;
	//}

	public Ergebnis getTip() {
		return tippedStatus;
	}

	public void setTippedStatus(Ergebnis tippedStatus) {
		this.tippedStatus = tippedStatus;
	}

	@Override
	public String toString(){
		Football f = (Football) getItem();
		return "Heim: " + f.getHost() +"\n Gast: " + f.getGuest() + "\nDatum: " + getDate() + "\nTipp: "+ getTip() + "\n";
	}


	public Team getHost(){
		Football f = (Football) getItem();
		return f.getHost();

	}
	public Team getGuest(){
		Football f = (Football) getItem();
		return f.getGuest();
	}

}
