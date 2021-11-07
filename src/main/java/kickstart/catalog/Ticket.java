package kickstart.catalog;


import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket extends Item {

	@OneToMany
	private List<NumberBet> numberBets = new ArrayList<>();

	public Ticket(){}

	public Ticket(int id, LocalDate date, double price){
		super(id, date, price);
		setType("Ticket");
		numberBets = new ArrayList<>();
	}

	public void addBit(NumberBet numberBet){
		numberBets.add(numberBet);
	}


	public List<NumberBet> getNumberBits(){
		return numberBets;
	}

	public void setNumberBits(List<NumberBet> numberBets) {
		this.numberBets = numberBets;
	}
}