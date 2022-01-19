package kickstart.forum;

import kickstart.customer.Customer;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ForumManagement {

	private ForumRepository comments;
	private ThemeRepository themes;

	public ForumManagement(ForumRepository comments, ThemeRepository themes) {
		this.comments = comments;
		this.themes = themes;
	}

	public Theme createTheme(String title) {
		Theme theme = new Theme(title);
		return themes.save(theme);
	}
	public ForumEntry createComment(Theme theme, ForumEntry forum) {
		theme.addForum(forum);
		return comments.save(forum);
	}

	public ForumEntry likeComment(Customer customer, ForumEntry forum) {
		forum.like(customer);
		return comments.save(forum);
	}

	public ForumEntry unlikeComment(Customer customer, ForumEntry forum) {
		forum.unlike(customer);
		return comments.save(forum);
	}

	public Iterable<Theme> findAll() {
		return themes.findAll();
	}

	public long countTheme() {
		return themes.count();
	}

	public long countComment() {
		return comments.count();
	}

	public void deleteComment(Theme theme, ForumEntry forumEntry) {

		if(theme.getForums().contains(forumEntry)) {
			theme.deleteForum(forumEntry);
			comments.delete(forumEntry);
		}
	}

	public void deleteTheme(Theme theme) {
		themes.delete(theme);
	}

	public Theme findByThemeName(String name) {
		var theme = themes.findThemeByName(name);
		return theme;
	}

	public Theme findByThemeId(long id) {
		var theme =  themes.findById(id).orElse(null);
		return theme;
	}

	public ForumEntry findForumById(long id) {
		var forum = comments.findForumEntryById(id);
		return forum;
	}
}
