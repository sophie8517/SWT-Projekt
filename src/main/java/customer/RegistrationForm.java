package customer;

import org.javamoney.moneta.Money;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class RegistrationForm {
	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private final String name;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}") //
	@Min(8)
	@Pattern(regexp = "^[0-9].+", message = "{RegistrationForm.password.NoNumber}") //
	@Pattern(regexp = "^[A-Z].+", message = "{RegistrationForm.password.NoUpperCase}") //
	private final String password;

	/*
	@NotEmpty(message = "{RegistrationForm.balance.NotEmpty}") // s
	private final double balance;

	public RegistrationForm(String name, String password, double balance){
		this.name = name;
		this.password = password;
		this.balance = balance;
	}
	*/

	public RegistrationForm(String name, String password){
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	/*
	public double getBalance() {
		return balance;
	}
	*/
}
