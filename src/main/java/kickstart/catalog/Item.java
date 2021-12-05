package kickstart.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Item extends Product{


	public static enum ItemType{
		TICKET, FOOTBALL, NONE;
	}


	private LocalDateTime timeLimit; //date and time of the drawing
	//private double price2;
	private ItemType type = ItemType.NONE;
	private String name;
	private long counter;

	public Item(){}

	public Item(String name, LocalDateTime timeLimit, Money price, ItemType type){
		//this.id = new Long(id);
		super(name, price);
		this.timeLimit = timeLimit;
		//time_limit später anpassen
		this.type = type;
		//bits = new ArrayList<Bet>();
		this.counter = 0;
	}

	public long getCounter() {
		return counter;
	}
	public void upCounter(){
		counter++;
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

	public String getFormatDate(){
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String formatdate = date.format(timeLimit.toLocalDate());
		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
		String formattime = time.format(timeLimit.toLocalTime());
		return formatdate + "  " + formattime;
	}



}