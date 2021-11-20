package kickstart.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Customer {

	private Integer id;
	private String lastName;
	private String email;
	private Integer gender; //0:woman 1:man

	public Customer(Integer id, String lastName, String email, Integer gender) {
		this.id = id;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
	}
}
