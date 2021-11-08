package kickstart.catalog;

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

	//private long id;

	//private int id2;
	private LocalDate date;
	private double price2;
	private ItemType type = ItemType.NONE;
	//private List<Bet> bits;

	public Item(){}

	public Item(LocalDate date, double price){
		//this.id = new Long(id);
		this.date = date;
		this.price2 = price;
		//bits = new ArrayList<Bet>();
	}



	protected void setType(ItemType type){
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getPrice2() {
		return price2;
	}

	public ItemType getType() {
		return type;
	}





	/*
	public void addBet(Bet bit){
		bits.add(bit);
	}



	public List<Bet> getBits(){
		return bits;
	}
	*/

}