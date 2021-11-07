package kickstart.catalog;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public class Item extends Product{

	@Id
	private long id;

	private int id2;
	private LocalDate date;
	private double price2;
	private String type = "none";
	//private List<Bet> bits;

	public Item(){}

	public Item(int id, LocalDate date, double price){
		this.id2 = id;
		//this.id = new Long(id);
		this.date = date;
		this.price2 = price;
		//bits = new ArrayList<Bet>();
	}

	public int getId2() {
		return id2;
	}

	protected void setType(String type){
		this.type = type;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getPrice2() {
		return price2;
	}

	public String getType() {
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