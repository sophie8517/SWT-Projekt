package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Group {

	//private @Id @GeneratedValue long id;
//	@OneToOne
//	private UserAccount userAccount;
	@Id
	private String groupName;
	@OneToMany
	private Set<Customer> customers;
	@ManyToOne
	private Customer leader;
	private String pwd;

	public Group(){}

	public Group(String groupName, Customer leader, String pwd){
		//this.userAccount = userAccount;
		this.leader = leader;
		this.groupName = groupName;
		this.pwd = pwd;
		customers =  new HashSet<>();
		customers.add(leader);
	}

//	public long getId() {
//		return id;
//	}

	public String getGroupName() {
		return groupName;
	}


	public void add(Customer customer){
		customers.add(customer);
	}

	public void remove(Customer customer){
		customers.remove(customer);
	}

//	public UserAccount getUserAccount() {
//		return userAccount;
//	}

	public Set<Customer> getMembers(){
		return customers;
	}
}
