package kickstart.catalog;


import org.javamoney.moneta.Money;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kickstart.customer.Customer;

@Entity
public class Ticket extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<NumberBet> numberBets = new ArrayList<>();



	public Ticket(String name, LocalDate date, Money price, ItemType type){
		super(name, date, price, type);
		numberBets = new ArrayList<>();
	}
	public Ticket(){}

	public void addBet(NumberBet numberBet){
		numberBets.add(numberBet);
	}


	public List<NumberBet> getNumberBetsbyCustomer(Customer customer){
		List<NumberBet> result = new ArrayList<>();

		for(NumberBet nb: numberBets){
			if(nb.getCustomer().equals(customer)){
				result.add(nb);
			}
		}
		return result;
	}

	public List<NumberBet> getNumberBits(){
		return numberBets;
	}
}