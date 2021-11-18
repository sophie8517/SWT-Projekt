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

	private LocalDate expiration = LocalDate.of(2000,1,1);



	public NumberBet(Item item, LocalDateTime date, Money einsatz, Customer c, List<Integer> numbers){
		super(item, date, einsatz,c);
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

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}
}
