package kickstart.customer;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;

@Entity
public class Customer {
	private @Id @GeneratedValue long id;
	private Money balance;
	@OneToOne
	private UserAccount userAccount;

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
}
