package kickstart.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item extends Product{


	public static enum ItemType{
		TICKET, FOOTBALL, NONE;
	}

	private LocalDate date;
	private LocalDateTime time_limit;
	private double price2;
	private ItemType type = ItemType.NONE;
	private String name;

	public Item(){}

	public Item(String name, LocalDate date, Money price, ItemType type){
		//this.id = new Long(id);
		super(name, price);
		this.date = date;
		//time_limit später anpassen
		this.time_limit = LocalDateTime.of(date, LocalTime.of(15,00));
		this.type = type;
		//bits = new ArrayList<Bet>();
	}


	protected void setType(ItemType type){
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalDateTime getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(LocalDateTime time_limit) {
		this.time_limit = time_limit;
	}

	public ItemType getType() {
		return type;
	}

	public double getPrice2() {
		return getPrice().getNumber().doubleValue();
	}
}