package kickstart.customer;

import com.fasterxml.classmate.util.ClassStack;
import kickstart.catalog.FootballBet;
import kickstart.catalog.NumberBet;
import kickstart.catalog.Status;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public class Customer implements Comparable<Customer> {
	private @Id @GeneratedValue long id;
	private Money balance;
	@OneToOne
	private UserAccount userAccount;

	@ManyToMany
	private List<Group> groups = new ArrayList<>();
	public List<Group> getGroup() {
		return groups;
	}


	public void addGroup(Group group){
		groups.add(group);
	}

	public Customer(){}
	public Customer (UserAccount userAccount){
		this.userAccount = userAccount;
		balance = Money.of(0.0, EURO);
	}
	@OneToMany(cascade = CascadeType.ALL)
	private List<FootballBet> footballBetList = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<NumberBet> numberBetList = new ArrayList<>();


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

	public List<FootballBet> getAllFootballBetList() {
		return footballBetList;
	}

	public List<NumberBet> getAllNumberBetList() {
		return numberBetList;
	}

	public void addFootballBet(FootballBet fb){
		footballBetList.add(fb);
	}

	public void addNumberBet(NumberBet nb){
		numberBetList.add(nb);
	}

	public void removeFootballBets(FootballBet fb) {footballBetList.remove(fb); }

	public void removeNumberBets(NumberBet nb) {numberBetList.remove(nb); }


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;

		if (!(obj instanceof Customer)) return false;

		Customer customer = (Customer) obj;

		return this.userAccount.getEmail().equals(customer.getUserAccount().getEmail());
	}

	@Override
	public String toString() {
		return userAccount.getFirstname() + " " + userAccount.getLastname();
	}

	@Override
	public int compareTo(Customer customer) {
		return this.toString().compareTo(customer.toString());
	}
}
