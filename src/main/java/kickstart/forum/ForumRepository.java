package kickstart.forum;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ForumRepository extends CrudRepository<ForumEntry, Long> {


	Streamable<ForumEntry> findByName(String name, Sort sort);
	ForumEntry findForumEntryById(long id);
}
