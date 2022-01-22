package kickstart.forum;

import kickstart.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ForumManagementTest extends AbstractIntegrationTest {

	@Autowired
	ForumManagement forumManagement;

	@Autowired
	ForumRepository repository;

	@Test
	void createTheme() {
		String title = "create theme";
		Theme theme = forumManagement.createTheme(title);
		assertNotNull(forumManagement.findByThemeName("create theme"));
	}

	@Test
	void createComment() {
		ForumForm form = new ForumForm("test", "test comment", "test@test.de");
		Theme theme = forumManagement.createTheme("123");
		ForumEntry forumEntry = forumManagement.createComment(theme, form.toNewEntry());
		assertEquals(theme, forumManagement.findByThemeName("123"));
		assertTrue(theme.getForums().contains(forumEntry));
		assertTrue(repository.existsById(forumEntry.getId()));
	}

}

