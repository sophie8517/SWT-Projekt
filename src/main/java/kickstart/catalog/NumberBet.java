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

	private LocalDate expiration = LocalDate.of(2000,1,1);


	public NumberBet(Item item, LocalDateTime date, Money inset, Customer customer, LocalDate expiration,
					 List<Integer> numbers){
		super(item, date, inset,customer, expiration);
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

	@Override
	public String toString() {
		return "NumberBet{" +
				"numbers=" + numbers +
				'}';
	}

	public LocalDate getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDate expiration) {
		this.expiration = expiration;
	}
}
