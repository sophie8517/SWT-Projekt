package kickstart.customer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class RegistrationForm {
	@NotEmpty(message = "{RegistrationForm.firstname.NotEmpty}") //
	private final String firstname;

	@NotEmpty(message = "{RegistrationForm.lastname.NotEmpty}") //
	private final String lastname;

	@NotEmpty(message = "{RegistrationForm.email.NotEmpty}")
	private final String email;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}") //
	//@Min(8)
	//@Pattern(regexp = "^[0-9].+", message = "{RegistrationForm.password.NoNumber}") //
	//@Pattern(regexp = "^[A-Z].+", message = "{RegistrationForm.password.NoUpperCase}") //
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$", message = "{RegistrationForm.password.Invalid}")

	private final String password;

	private final String passwordCheck;

	/*
	@NotEmpty(message = "{RegistrationForm.balance.NotEmpty}") // s
	private final double balance;

	public RegistrationForm(String name, String password, double balance){
		this.name = name;
		this.password = password;
		this.balance = balance;
	}
	*/

	public RegistrationForm(String firstname, String lastname,String email, String password, String passwordCheck){
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.passwordCheck = passwordCheck;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getEmail() { return email;}

	public String getPassword() {
		return password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public boolean check(){
		return password.equals(passwordCheck);
	}
}
