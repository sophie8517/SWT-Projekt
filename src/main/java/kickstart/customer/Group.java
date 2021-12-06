package kickstart.customer;

import org.hibernate.annotations.CollectionType;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "BETGROUP")
public class Group {

	//private @Id @GeneratedValue long id;
//	@OneToOne
//	private UserAccount userAccount;
	@Id
	private String groupName;

	@Transient
	private Set<Customer> customers = new TreeSet<>();
	@Transient
	private Customer leader;
	private String password;

	public Group(){}

	public Group(String groupName, Customer leader, String password){
		//this.userAccount = userAccount;
		this.leader = leader;
		this.groupName = groupName;
		this.password = password;
		customers.add(leader);
	}


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

	public String getPassword() {
		return password;
	}

	public Customer getLeader() {
		return leader;
	}

	public boolean contains(Customer customer) {
		return customers.contains(customer);
	}

	@Override
	public String toString() {
		return this.groupName;
	}
}
