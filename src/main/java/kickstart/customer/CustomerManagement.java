package kickstart.customer;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.*;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.Optional;

import static org.salespointframework.core.Currencies.EURO;


@Service
@Transactional
public class CustomerManagement {

	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");

	private GroupRepository groups;
	private CustomerRepository customers;
	private UserAccountManagement userAccounts;


	public CustomerManagement(CustomerRepository customers, GroupRepository groups, UserAccountManagement userAccounts){
		this.customers = customers;
		this.groups = groups;
		this.userAccounts = userAccounts;
	}


	public Customer createCustomer(RegistrationForm form){
		Assert.notNull(form, "Registration form must not be null!");

		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccounts.create(form.getEmail(), password, CUSTOMER_ROLE);
		userAccount.setEmail(form.getEmail());
		userAccount.setFirstname(form.getFirstname());
		userAccount.setLastname(form.getLastname());

		return customers.save(new Customer(userAccount));
	}

	public Group createGroup(String groupName, Customer customer){


		int length = 8;
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		System.out.println(sb);

		var password = sb.toString();

		Group group = new Group(groupName, customer, password);
		customer.addGroup(group);



//		var userAccount = userAccounts.create(form.getEmail(), password, CUSTOMER_ROLE);
//		userAccount.setEmail(form.getEmail());
//		userAccount.setFirstname(form.getFirstname());
//		userAccount.setLastname(form.getLastname());
//		customer.getUserAccount().add(Role.of("LEADER"));
//		customers.save(customer);

		return groups.save(group);
	}

//	public Group deleteGroup(Group group){
//		userAccounts.delete(group.getUserAccount());
//		groups.delete(group);
//		return group;
//	}

	public Streamable<Customer> findAllCustomers() {
		return customers.findAll();
	}

	public Streamable<Group> findAllGroups(){
		return groups.findAll();
	}

	public Group addMemberToGroup(Customer customer, Group group, String password){
		System.out.println(group.getPassword() + " and " + password);
		if(!group.getPassword().equals(password))
			throw new IllegalStateException("password doesn't match!");

		if(group.contains(customer))
			throw new IllegalArgumentException("Customer is already in the Group!");

		group.add(customer);
		customer.addGroup(group);

		return groups.save(group);
	}

	public Group removeMemberOfGroup(Customer customer, Group group){
		if (!group.contains(customer))
			throw new IllegalArgumentException("Customer doesn't exist!");
		group.remove(customer);
		return groups.save(group);
	}


	public void charge(Money money,  Customer customer){
		customer.setBalance(customer.getBalance().add(money));
	}

	public Customer findByCustomerId(long customerId){
		var customer = customers.findById(customerId).orElse(null);
		return customer;
	}

	public Group findByGroupName(String name){
		var group = groups.findById(name).orElse(null);
		return group;
	}
	public Customer findByUserAccount(UserAccount userAccount){
		var customer = customers.findCustomerByUserAccount(userAccount);
		return customer;
	}

	public Optional<UserAccount> findByEmail(String name){ return userAccounts.findByUsername(name); }


	//public boolean changePwd(Optional<UserAccount> userAccount, String oldPassword, String newPassword, String newPassword1){
	public Customer changePwd(Customer customer, RegistrationForm form){

		var password = Password.UnencryptedPassword.of(form.getPassword());
		userAccounts.changePassword(customer.getUserAccount(), password);

		return customers.save(customer);



		//var customer = findByUserAccount(userAccount.get());

//		if(newPassword.equals(newPassword1) ){
//			var newUnencryptedPassword = Password.UnencryptedPassword.of(newPassword);
//			userAccounts.changePassword(customer.getUserAccount(), newUnencryptedPassword);
//			//customers.save(customer);
//			System.out.println("changed");
//			return true;
//		} else {
//			System.out.println("failed");
//			return false;
//		}

		//return customer;
	}

	public void deleteCustomer(Long id){
		userAccounts.delete(customers.findById(id).get().getUserAccount());
		customers.deleteById(id);
		System.out.println("deleted");
	}
}
