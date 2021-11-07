package kickstart.catalog;

import kickstart.catalog.Bit;
import kickstart.catalog.Item;
import kickstart.catalog.NumberBit;


import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket extends Item {

	@OneToMany
	private List<NumberBit> numberBits = new ArrayList<>();

	public Ticket(){}

	public Ticket(int id, LocalDate date, double price){
		super(id, date, price);
		setType("Ticket");
		numberBits = new ArrayList<>();
	}

	public void addBit(NumberBit numberBit){
		numberBits.add(numberBit);
	}
	public List<NumberBit> getNumberBits(){
		return numberBits;
	}
}