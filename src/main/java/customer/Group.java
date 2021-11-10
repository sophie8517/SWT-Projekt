package customer;

import org.salespointframework.useraccount.UserAccount;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Group {
	private long id;
	private UserAccount userAccount;
	private Set<Customer> customers;
	private Customer leader;

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
