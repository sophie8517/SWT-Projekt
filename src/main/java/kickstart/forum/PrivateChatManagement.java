package kickstart.forum;

import kickstart.customer.Customer;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class PrivateChatManagement {

	private ForumRepository comments;
	private ThemeRepository themes;
	private PrivateChatRepository privateChats;

	public PrivateChatManagement(ForumRepository comments, PrivateChatRepository privateChatRepository) {
		this.comments = comments;
		this.privateChats = privateChatRepository;
	}

	public PrivateChat createPrivateChat(UserAccount invitee, UserAccount inviter) {
		PrivateChat privateChat = new PrivateChat(inviter);
		privateChat.addGroupPartner(invitee);
		return privateChats.save(privateChat);
	}

	public ForumEntry createMessage(PrivateChat privateChat, ForumEntry forum) {
		privateChat.addForum(forum);
		return comments.save(forum);
	}

	public Iterable<PrivateChat> findAll() {
		return privateChats.findAll();
	}

	public List<PrivateChat> findAllByUserAccount(UserAccount userAccount) {
		List<PrivateChat> privateChats = new LinkedList<>();
		Iterator<PrivateChat> it = findAll().iterator();
		while(it.hasNext()) {
			PrivateChat privateChat = it.next();
			if (privateChat.contains(userAccount)) {
				privateChats.add(privateChat);
			}
		}

		return privateChats;
	}

	public long countTheme() {
		return privateChats.count();
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

	public void deletePrivateChat(PrivateChat privateChat) {
		privateChats.delete(privateChat);
	}

	public PrivateChat findByThemePartnerUsername(Long id) {
		var privateChat = privateChats.findById(id).orElse(null);
		return privateChat;
	}

	public PrivateChat findByPrivateChatId(long id) {
		var privateChat =  privateChats.findById(id).orElse(null);
		return privateChat;
	}
}