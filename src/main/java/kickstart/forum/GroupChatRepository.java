package kickstart.forum;

import org.springframework.data.repository.CrudRepository;

public interface GroupChatRepository extends CrudRepository<GroupChat, Long> {

	@Override
	Iterable<GroupChat> findAll();
	GroupChat findGroupChatByName(String name);

}
