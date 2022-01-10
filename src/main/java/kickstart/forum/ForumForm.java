package kickstart.forum;

import javax.validation.constraints.NotBlank;

public class ForumForm {

	private final @NotBlank String name;
	private final @NotBlank String text;
	private final @NotBlank String email;

	public ForumForm(String name, String text, String email) {

		this.name = name;
		this.text = text;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

	public String getEmail() {return email;}


	ForumEntry toNewEntry() {
		return new ForumEntry(getName(), getText(), getEmail());
	}
}
