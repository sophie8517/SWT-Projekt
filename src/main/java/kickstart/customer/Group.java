package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Group {
	private String groupName;
	private @Id @GeneratedValue long id;
	@OneToOne
	private UserAccount userAccount;
	@OneToMany
	private Set<Customer> members;
	@OneToOne
	private Customer leader;

	public Group(){}

	public Group(UserAccount userAccount, Customer leader, String groupName){
		this.userAccount = userAccount;
		this.leader = leader;
		this.groupName = groupName;
		members =  new HashSet<>();
		members.add(leader);
	}

	public long getId() {
		return id;
	}

	public void add(Customer customer){
		members.add(customer);
	}

	public void remove(Customer customer){
		members.remove(customer);
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public Set<Customer> getMembers(){
		return members;
	}

	public Customer getLeader() {
		return leader;
	}

	public void setLeader(Customer leader) {
		this.leader = leader;
	}

	public String getPassword(){
		return getUserAccount().getPassword().toString();
	}
}
