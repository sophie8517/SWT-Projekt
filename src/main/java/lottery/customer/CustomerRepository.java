package lottery.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Override
	Streamable<Customer> findAll();
}
