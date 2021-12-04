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
	@ManyToOne
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

	public Customer getLeader() {
		return leader;
	}

	public boolean contains(Customer customer) {
		return customers.contains(customer);
	}

	public boolean isLeader(Customer customer) {
		return customer.equals(leader);
	}

	public void setLeader(Customer leader) {
		if (!contains(leader) || isLeader(leader))
			throw new IllegalArgumentException("Customer doesn't find or is already leader!");
		this.leader = leader;
	}
}
