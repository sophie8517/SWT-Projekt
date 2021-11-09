package kickstart.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item extends Product{


	public static enum ItemType{
		TICKET, FOOTBALL, NONE;
	}

	private LocalDate date;
	private Money price2;
	private ItemType type = ItemType.NONE;
	private String name;

	public Item(){}

	public Item(String name, LocalDate date, Money price, ItemType type){
		//this.id = new Long(id);
		super(name, price);
		this.date = date;

		this.type = type;
		//bits = new ArrayList<Bet>();
	}


	protected void setType(ItemType type){
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}


	public ItemType getType() {
		return type;
	}



}