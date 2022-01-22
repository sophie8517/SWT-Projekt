package kickstart.forum;

import kickstart.customer.Customer;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface PrivateChatRepository extends CrudRepository<PrivateChat, Long> {
	@Override
	Iterable<PrivateChat> findAll();
    //PrivateChat findPrivateChat(UserAccount userAccount);

}
