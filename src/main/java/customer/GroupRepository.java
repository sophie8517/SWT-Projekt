package customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface GroupRepository extends CrudRepository<Group, Long> {
	@Override
	Streamable<Group> findAll();
}
