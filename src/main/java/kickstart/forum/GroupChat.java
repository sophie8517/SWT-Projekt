package kickstart.forum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class GroupChat {

	private @Id @GeneratedValue long id;
	private String name;

	@OneToMany
	private List<ForumEntry> forums = new LinkedList<>();

	public GroupChat() {}

	public GroupChat(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ForumEntry> getForums() {
		return forums;
	}

	public void addForum(ForumEntry forumEntry) {
		forums.add(forumEntry);
	}

	public void deleteForum(ForumEntry forumEntry) {
		forums.remove(forumEntry);
	}

	@Override
	public String toString() {
		return name;
	}

	public long getCount() {
		return forums.size();
	}


}
