package kickstart.customer;

import org.javamoney.moneta.Money;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.SecureRandom;


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

	public Group createGroup(RegistrationForm form, Customer customer){
		Assert.notNull(form, "Registration form must not be null!");

			int length = 8;
			final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

			SecureRandom random = new SecureRandom();
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < length; i++)
			{
				int randomIndex = random.nextInt(chars.length());
				sb.append(chars.charAt(randomIndex));
			}

			System.out.println(sb);

		var password = Password.UnencryptedPassword.of(sb.toString());
		var userAccount = userAccounts.create(form.getEmail(), password, CUSTOMER_ROLE);
		userAccount.setEmail(form.getEmail());
		userAccount.setFirstname(form.getFirstname());
		userAccount.setLastname(form.getLastname());

		return groups.save(new Group(customer.getUserAccount(),customer));
	}

	public Group deleteGroup(Group group){
		userAccounts.delete(group.getUserAccount());
		groups.delete(group);
		return group;
	}

	public Streamable<Customer> findAllCustomers() {
		return customers.findAll();
	}

	public Streamable<Group> findAllGroups(){
		return groups.findAll();
	}

	public Group addMemberToGroup(Customer customer, Group group, String password){
		if (group.getUserAccount().getPassword().toString().equals(password)){
			userAccounts.save(group.getUserAccount());
			group.add(customer);
			return group;
		}
		throw new NullPointerException("Password doesn't match!");
	}

	public Group removeMemberOfGroup(Customer customer, Group group){
		group.remove(customer);
		return group;
	}


	public Customer charge(Money money,  Customer customer){
		Money balance = customer.getBalance();
		balance.subtract(money);
		return customer;
	}

	public Customer findByCustomerId(long customerId){
		var customer = customers.findById(customerId).orElse(null);
		return customer;
	}

	public Group findByGroupId(long groupId){
		var group = groups.findById(groupId).orElse(null);
		return group;
	}
	public Customer findByUserAccount(UserAccount userAccount){
		var customer = customers.findCustomerByUserAccount(userAccount);
		return customer;
	}

}
