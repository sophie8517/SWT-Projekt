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

	private LocalDate date;
	private LocalDateTime timeLimit;
	private double price2;
	private ItemType type = ItemType.NONE;
	private String name;

	public Item(){}

	public Item(String name, LocalDate date, Money price, ItemType type){
		//this.id = new Long(id);
		super(name, price);
		this.date = date;
		//time_limit später anpassen
		this.timeLimit = LocalDateTime.of(date, LocalTime.of(15,00));
		this.type = type;
		//bits = new ArrayList<Bet>();
	}


	protected void setType(ItemType type){
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalDateTime getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(LocalDateTime timeLimit) {
		this.timeLimit = timeLimit;
	}

	public ItemType getType() {
		return type;
	}

	public double getPrice2() {
		return getPrice().getNumber().doubleValue();
	}
}