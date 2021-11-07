package kickstart.catalog;

import org.salespointframework.catalog.Product;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item extends Product{
	private int id2;
	private LocalDate date;
	private double price2;
	private String type = "none";
	//private List<Bit> bits;

	public Item(){}

	public Item(int id, LocalDate date, double price){
		this.id2 = id;
		this.date = date;
		this.price2 = price;
		//bits = new ArrayList<Bit>();
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
	public void addBet(Bit bit){
		bits.add(bit);
	}



	public List<Bit> getBits(){
		return bits;
	}
	*/

}