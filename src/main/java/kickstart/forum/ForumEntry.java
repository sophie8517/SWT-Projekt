package kickstart.forum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import kickstart.customer.Customer;
import org.springframework.util.Assert;

@Entity
public class ForumEntry {

	private @Id @GeneratedValue Long id;
	private final String name, /*theme ,*/text, email;
	private final LocalDateTime date;
	private int likedCount, unlikedCount;

	@OneToMany
	private Set<Customer> likedSet = new HashSet<>();

	@OneToMany
	private Set<Customer> unlikedSet = new HashSet<>();

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
		likedCount = 0;
		unlikedCount = 0;
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

	public int getLikedCount() {
		return likedCount;
	}

	public int getUnlikedCount() {
		return unlikedCount;
	}

	public void like(Customer customer) {
		likedSet.add(customer);
		likedCount++;
	}

	public void unlike(Customer customer) {
		unlikedSet.add(customer);
		unlikedCount++;
	}

	public boolean likedContains(Customer customer) {
		return likedSet.contains(customer);
	}

	public boolean unlikedContains(Customer customer) {
		return unlikedSet.contains(customer);
	}

	public void removeLike(Customer customer) {
		likedSet.remove(customer);
		likedCount--;
	}

	public void removeUnlike(Customer customer) {
		unlikedSet.remove(customer);
		unlikedCount--;
	}

	//public String getTheme() { return theme; }
}
