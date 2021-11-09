package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class NumberBet extends Bet {

	@ElementCollection
	private List<Integer> numbers;


	public NumberBet(Item item, LocalDateTime date, Money einsatz, List<Integer> numbers){
		super(item, date, einsatz);
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
