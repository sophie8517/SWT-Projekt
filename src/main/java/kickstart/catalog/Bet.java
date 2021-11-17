package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
public class Bet implements Serializable {

	private static final long serialVersionUID = -7114101035786254953L;

	private LocalDateTime date;
	private Money inset;

	@ManyToOne
	private Item item;

	private Status status;

	@Id@GeneratedValue
	private Long id;


	public Bet(Item item, LocalDateTime date, Money inset){
		this.item = item;
		this.date = date;
		this.inset = inset;
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


	public Money getInset() {
		return inset;
	}


	public Long getId() {
		return id;
	}

	public void setInset(Money money){ this.inset = money; }

}
