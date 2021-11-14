package lottery.customer;

import javax.validation.constraints.NotEmpty;

public class Profile {
	@NotEmpty(message = "{Profile.firstname.NotEmpty}")
	private String firstname;
	@NotEmpty(message = "{Profile.lastname.NotEmpty}")
	private String lastname;
	@NotEmpty(message = "{Profile.phone.NotEmpty}")
	private String phone;
	@NotEmpty(message = "{Profile.email.NotEmpty}")
	private String email;

	public Profile() {}

	public Profile(String firstname, String lastname, String phone, String email) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Profile{" +
				"firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
