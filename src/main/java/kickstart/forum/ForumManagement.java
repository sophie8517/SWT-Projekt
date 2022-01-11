package kickstart.forum;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ForumManagement {

	private ForumRepository comments;

	public ForumManagement(ForumRepository comments) {
		this.comments = comments;
	}

	public ForumEntry createComment(ForumEntry forum) {
		return comments.save(forum);
	}

	public Iterable<ForumEntry> findAll() {
		return comments.findAll();
	}

	public long count() {
		return comments.count();
	}

	public void delete(ForumEntry forumEntry) {
		comments.delete(forumEntry);
	}
}
