package kickstart.catalog;

import kickstart.customer.Customer;
import org.javamoney.moneta.Money;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class NumberBet extends Bet {

	@ElementCollection
	private List<Integer> numbers;



	public NumberBet(Item item, LocalDateTime date, Money einsatz, Customer c,LocalDate expiration, List<Integer> numbers){
		super(item, date, einsatz,c, expiration);
		this.numbers = numbers;
	}

	public NumberBet() {

	}

	//@ElementCollection
	public List<Integer> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}


}
