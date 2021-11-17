package kickstart.customer;

public class Profile {
	public String firstname;
	private String lastname;
	private String phone;
	private String email;

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
