package kickstart.customer;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class GroupRegistrationForm {
	@NotEmpty(message = "{RegistrationForm.firstname.NotEmpty}") //
	private final String name;

	/*
	@NotEmpty(message = "{RegistrationForm.balance.NotEmpty}") // s
	private final double balance;

	public RegistrationForm(String name, String password, double balance){
		this.name = name;
		this.password = password;
		this.balance = balance;
	}
	*/

	public GroupRegistrationForm(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
