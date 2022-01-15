package kickstart.forum;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class ForumEntry {

	private @Id @GeneratedValue Long id;
	private final String name, /*theme ,*/text, email;
	private final LocalDateTime date;

	public ForumEntry(String name, String text, String email) {

		Assert.hasText(name, "Name must not be null or empty!");
		//Assert.hasText(theme, "Theme must not be null or empty!");
		Assert.hasText(text, "Text must not be null or empty!");
		Assert.hasText(email, "Email must not be null or empty!");

		this.name = name;
		this.text = text;
		//this.theme = theme;
		this.email = email;
		this.date = LocalDateTime.now();
	}

	@SuppressWarnings("unused")
	public ForumEntry() {
		this.name = null;
		this.text = null;
		//this.theme = null;
		this.email = null;
		this.date = null;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getText() {
		return text;
	}

	public String getEmail() {return email;}

	//public String getTheme() { return theme; }
}