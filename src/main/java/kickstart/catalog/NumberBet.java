package kickstart.catalog;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class NumberBet extends Bet {

	@ElementCollection
	private List<Integer> numbers;


	public NumberBet(Item item, int id, LocalDateTime date, double einsatz, List<Integer> numbers){
		super(item, id, date, einsatz);
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
