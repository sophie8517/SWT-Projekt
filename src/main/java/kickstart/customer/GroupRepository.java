package kickstart.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface GroupRepository extends CrudRepository<Group, String> {
	@Override
	Streamable<Group> findAll();
}
