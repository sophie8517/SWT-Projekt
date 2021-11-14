package lottery.customer;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;

@Entity
public class Customer {
	private @Id @GeneratedValue long id;
	private Money balance;
	@OneToOne
	private UserAccount userAccount;
	@Transient
	private Profile profile;

	public Customer(){}

	public Customer(UserAccount userAccount){
		this.userAccount = userAccount;
	}

	public long getId() {
		return id;
	}

	public void setBalance(Money balance) {
		this.balance = balance;
	}

	public Money getBalance() {
		return balance;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setProfile(Profile profile) { this.profile = profile; }

	public Profile getProfile() { return profile; }
}
