package kickstart.catalog;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class NumberBit extends Bit{

	@ElementCollection
	private List<Integer> numbers;


	public NumberBit(Item item, int id, LocalDateTime date, double einsatz, List<Integer> numbers){
		super(item, id, date, einsatz);
		this.numbers = numbers;
	}

	public NumberBit() {

	}

	public List<Integer> getNumbers() {
		return numbers;
	}


}
