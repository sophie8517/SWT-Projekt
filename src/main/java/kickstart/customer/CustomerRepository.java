package kickstart.customer;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Override
	Streamable<Customer> findAll();

	Customer findCustomerByUserAccount(UserAccount userAccount);
}
