package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

@Entity
@Table(name = "BETGROUP")
public class Group {

	@Id
	private String groupName;

	@ManyToMany
	private Set<Customer> customers = new TreeSet<>();
	@ManyToOne
	private Customer leader;

	private String password;

	public Group(){}

	public Group(String groupName, Customer leader, String password){
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

	public Set<Customer> getMembers(){
		return customers;
	}

	public String getPassword() {
		return password;
	}

	public void setLeader(Customer leader) { this.leader = leader; }

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
