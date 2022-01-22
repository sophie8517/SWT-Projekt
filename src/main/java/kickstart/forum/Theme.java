package kickstart.forum;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Theme {

	private @Id @GeneratedValue long id;
	private String name;

	@ManyToOne
	private PrivateChat privateChat;

	@OneToMany
	private List<ForumEntry> forums = new LinkedList<>();

	public Theme() {}

	public Theme(String name) {
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