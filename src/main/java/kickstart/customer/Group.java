package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Group {
	private @Id @GeneratedValue long id;
	@OneToOne
	private UserAccount userAccount;
	@OneToMany
	private Set<Customer> customers;
	@OneToOne
	private Customer leader;

	public Group(){}

	public Group(UserAccount userAccount, Customer leader){
		this.userAccount = userAccount;
		this.leader = leader;
		customers =  new HashSet<>();
		customers.add(leader);
	}

	public long getId() {
		return id;
	}

	public void add(Customer customer){
		customers.add(customer);
	}

	public void remove(Customer customer){
		customers.remove(customer);
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public Set<Customer> getMembers(){
		return customers;
	}
}
