package kickstart.customer;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CustomerDao {

	private static Map<Integer, Customer> customers = null;

	static {
		customers = new HashMap<Integer, Customer>();

		customers.put(1001, new Customer(1001, "Jeff","jeff@gmail.com" ,1));
		customers.put(1002, new Customer(1002, "Rayan","rayan@gmail.com" ,1));
		customers.put(1003, new Customer(1003, "Alex","alex@gmail.com" ,1));
		customers.put(1004, new Customer(1004, "Kuechenmeister","kuechenmeister@gmail.com" ,1));
		customers.put(1005, new Customer(1005, "Lucas","lucas@gmail.com" ,1));

	}

	public Collection<Customer> getAll(){
		return customers.values();
	}

}
