package kickstart.catalog;


import kickstart.customer.Customer;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ticket extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<NumberBet> numberBets = new ArrayList<>();

	@ElementCollection
	private List<Integer> winNumbers = new ArrayList<>();

	private int additionalNumber;

	@ElementCollection
	private List<LocalDate> checkEvaluation = new ArrayList<>();


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
	public NumberBet findbyBetId(String id){
		if(numberBets.isEmpty()){
			return null;
		}

		NumberBet result = null;
		for(NumberBet nb: numberBets){
			if(nb.getIdstring().equals(id)){
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

	public List<LocalDate> getCheckEvaluation() {
		return checkEvaluation;
	}
	public void addCheck(LocalDate date){
		checkEvaluation.clear();
		checkEvaluation.add(date);
	}
}