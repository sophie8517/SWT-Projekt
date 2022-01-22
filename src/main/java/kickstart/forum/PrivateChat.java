package kickstart.forum;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;

import kickstart.customer.*;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

@Entity
public class PrivateChat {

	private @Id @GeneratedValue long id;

	@ManyToMany
	private List<UserAccount> partners = new LinkedList<>();

	@OneToMany
	private List<ForumEntry> chats = new LinkedList<>();

	@ManyToOne
	private UserAccount inviter;


	public PrivateChat(){
	}

	public PrivateChat(UserAccount inviter){
		this.inviter = inviter;
		partners.add(inviter);
	}


	public void addGroupPartner(UserAccount invitee){
		/*
		if (partners.size() >= 2){
			throw new IndexOutOfBoundsException("Private chat is full.");
		}
		*/
		if (partners.contains(invitee)){
			throw new IllegalArgumentException("Chat has been already created.");
		}
		partners.add(invitee);
	}

	public void addForum(ForumEntry forumEntry) {
		chats.add(forumEntry);
	}

	public List<UserAccount> getPartners(){
		return partners;
	}

	public List<ForumEntry> getForums() {
		return chats;
	}

	public UserAccount getInviter() {
		return inviter;
	}

	public boolean contains(UserAccount userAccount) {
		return partners.contains(userAccount);
	}

	public long getId() {
		return id;
	}

	public long getCount(){
		return chats.size();
	}
}
