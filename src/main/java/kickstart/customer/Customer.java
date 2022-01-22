package kickstart.customer;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.*;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Customer implements Comparable<Customer>{
	private @Id @GeneratedValue long id;
	private Money balance;
	@OneToOne
	private UserAccount userAccount;

	@ManyToMany
	private List<Group> groups = new LinkedList<>();


	public Customer(){}
	public Customer (UserAccount userAccount){
		this.userAccount = userAccount;
		balance = Money.of(0.0, EURO);
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

	public void addGroup(Group group){
		groups.add(group);
	}

	public void removeGroup(Group group) { groups.remove(group); }

	public List<Group> getGroup() {
		return groups;
	}

	@Override
	public String toString() {
		return userAccount.getFirstname() + " " + userAccount.getLastname();
	}

	@Override
	public int compareTo(Customer customer) {
		return this.toString().compareTo(customer.toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}

		if(!(obj instanceof Customer)){
			return false;
		}

		Customer customer = (Customer) obj;

		return this.userAccount.getEmail().equals(customer.getUserAccount().getEmail());
	}
}
