package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import kickstart.customer.Customer;

@Entity
public class NumberBet extends Bet {

	@ElementCollection
	private List<Integer> numbers;
	private int additionalNum;


	public NumberBet(Item item, LocalDateTime date, Money inset, Customer customer, LocalDateTime expiration,
					 List<Integer> numbers, int additionalNum){
		super(item, date, inset,customer, expiration);
		this.numbers = numbers;
		this.additionalNum = additionalNum;
	}

	public NumberBet() {

	}

	//@ElementCollection
	public List<Integer> getNumbers() {
		return numbers;
	}

	/*
	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}

	 */

	public int getAdditionalNum() {
		return additionalNum;
	}

	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}

	public void setAdditionalNum(int additionalNum) {
		this.additionalNum = additionalNum;
	}

	@Override
	public String toString() {
		return "NumberBet{" +
				"numbers=" + numbers +
				'}';
	}

}
