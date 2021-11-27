package kickstart.catalog;


import kickstart.customer.Customer;
import org.javamoney.moneta.Money;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ticket extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<NumberBet> numberBets = new ArrayList<>();

	@ElementCollection
	private List<Integer> winNumbers = new ArrayList<>();

	private int additionalNumber;




<<<<<<< HEAD
	public Ticket(String name, LocalDateTime timeLimit, Money price, ItemType type){
		super(name, timeLimit, price, type);

		/*
		winNumbers.add(1);
		winNumbers.add(2);
		winNumbers.add(3);
		winNumbers.add(4);
		winNumbers.add(5);
		winNumbers.add(6);

		 */
=======
	public Ticket(String name, LocalDate date, Money price, ItemType type){
		super(name, date, price, type);
		numberBets = new ArrayList<>();
		setTimeLimit(LocalDateTime.of(LocalDate.of(2021,11,28), LocalTime.of(15,00)));
>>>>>>> 44b366b5d9e497327ffebb5e9cd9df9c9218b003
	}
	public Ticket(){}

	public void addBet(NumberBet numberBet){
		numberBets.add(numberBet);
	}

	public void removeBet(NumberBet bet){
		numberBets.remove(bet);
	}


	public List<NumberBet> getNumberBits(){
		return numberBets;
	}

	public List<NumberBet> getNumberBetsbyCustomer(Customer c){
		List<NumberBet> result = new ArrayList<>();

		for(NumberBet nb: numberBets){
			if(nb.getCustomer().equals(c)){
				result.add(nb);
			}
		}
		return result;
	}
	public NumberBet findbyBetId(long id){
		if(numberBets.isEmpty()){
			return null;
		}

		NumberBet result = null;
		for(NumberBet nb: numberBets){
			if(nb.getId() == id){
				result = nb;
				break;
			}
		}
		return result;
	}

	public List<Integer> getWinNumbers() {
		if(winNumbers.isEmpty()){
			winNumbers.add(1);
			winNumbers.add(2);
			winNumbers.add(3);
			winNumbers.add(4);
			winNumbers.add(5);
			winNumbers.add(6);
		}
		return winNumbers;
	}

	public void setWinNumbers(List<Integer> winNumbers) {
		this.winNumbers = winNumbers;
	}

	public int getAdditionalNumber() {
		return additionalNumber;
	}

	public void setAdditionalNumber(int additionalNumber) {
		this.additionalNumber = additionalNumber;
	}
}