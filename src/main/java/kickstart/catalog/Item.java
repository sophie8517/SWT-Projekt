package kickstart.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Item extends Product{


	public static enum ItemType{
		TICKET, FOOTBALL, NONE;
	}

<<<<<<< HEAD

	private LocalDateTime timeLimit; //date and time of the drawing
	//private double price2;
=======
	private LocalDate date;
	private LocalDateTime timeLimit;
	private double price2;
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
	private ItemType type = ItemType.NONE;
	private String name;

	public Item(){}

	public Item(String name, LocalDateTime timeLimit, Money price, ItemType type){
		//this.id = new Long(id);
		super(name, price);
<<<<<<< HEAD
		this.timeLimit = timeLimit;
		//time_limit später anpassen
=======
		this.date = date;
		//time_limit später anpassen
		this.timeLimit = LocalDateTime.of(date, LocalTime.of(15,00));
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
		this.type = type;
		//bits = new ArrayList<Bet>();
	}


	public LocalDateTime getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(LocalDateTime timeLimit) {
		this.timeLimit = timeLimit;
	}

<<<<<<< HEAD
=======
	public LocalDateTime getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(LocalDateTime timeLimit) {
		this.timeLimit = timeLimit;
	}

>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
	public ItemType getType() {
		return type;
	}

	public double getPrice2() {
		return getPrice().getNumber().doubleValue();
	}

}