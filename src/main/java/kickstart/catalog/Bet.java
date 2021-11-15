package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
public class Bet implements Serializable {

	private static final long serialVersionUID = -7114101035786254953L;

	private LocalDateTime date;
	private Money einsatz;
	private double einsatz2;

	@ManyToOne
	private Item item;

	private Status status;

	@Id@GeneratedValue
	private Long id;


	public Bet(Item item, LocalDateTime date, Money einsatz){
		this.item = item;
		this.date = date;
		this.einsatz = einsatz;
		this.status = Status.OPEN;

	}

	public Bet() {

	}

	public void changeStatus(Status newStatus){
		this.status = newStatus;
	}

	public Status getStatus() {
		return status;
	}


	public Item getItem() {
		return item;
	}

	public LocalDateTime getDate() {
		return date;
	}


	public Money getEinsatz() {
		return einsatz;
	}

	public double getEinsatz2(){
		return einsatz.getNumber().doubleValue();
	}

	public void setEinsatz(Money einsatz) {
		this.einsatz = einsatz;
	}

	public Long getId() {
		return id;
	}

}
