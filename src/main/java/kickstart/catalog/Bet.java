package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kickstart.customer.Customer;


@Entity
public class Bet implements Serializable {

	private static final long serialVersionUID = -7114101035786254953L;

	private LocalDateTime date; //date when the user placed the bet
	private Money inset;
	private double inset2;

	@ManyToOne
	private Item item;

	private Status status;

	@Id@GeneratedValue
	private Long id;

	@ManyToOne
	private Customer customer;

	private LocalDateTime expiration; //time until when the bet is valid


	public Bet(Item item, LocalDateTime date, Money einsatz, Customer customer, LocalDateTime expiration){
		this.item = item;
		this.date = date;
		this.inset = einsatz;
		this.status = Status.OPEN;
		this.customer = customer;
		this.expiration = expiration;

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


	public Money getInset() {
		return inset;
	}

	public double getEinsatz2(){
		return inset.getNumber().doubleValue();
	}

	public Long getId() {
		return id;
	}

	public void setInset(Money money){ this.inset = money; }

	public Customer getCustomer() {	return customer;}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	/*
	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}

	 */
}
