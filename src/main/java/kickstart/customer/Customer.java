package kickstart.customer;

import kickstart.catalog.FootballBet;
import kickstart.catalog.NumberBet;
import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
	private @Id @GeneratedValue long id;
	private Money balance;
	@OneToOne
	private UserAccount userAccount;


	@OneToMany(cascade = CascadeType.ALL)
	private List<FootballBet> footballBetList = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<NumberBet> numberBetList = new ArrayList<>();

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

	public List<FootballBet> getFootballBetList() {
		return footballBetList;
	}

	public List<NumberBet> getNumberBetList() {
		return numberBetList;
	}

	public void addFootballBet(FootballBet fb){
		footballBetList.add(fb);
	}

	public void addNumberBet(NumberBet nb){
		numberBetList.add(nb);
	}
}
