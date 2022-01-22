package kickstart.forum;

import kickstart.customer.Customer;
import kickstart.customer.CustomerManagement;
import kickstart.customer.Group;
import kickstart.customer.GroupRepository;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@Transactional
public class GroupChatManagement {

	private ForumRepository comments;
	private GroupChatRepository groupChats;
//	private GroupRepository groupRepository;


	public GroupChatManagement(ForumRepository comments, GroupChatRepository groupChats) {
		this.comments = comments;
		this.groupChats = groupChats;
	}

	public GroupChat createGroupChat(String title) {
		GroupChat groupChat = new GroupChat(title);
		return groupChats.save(groupChat);
	}

	public ForumEntry createComment(GroupChat groupChat, ForumEntry forum){
		groupChat.addForum(forum);
		return comments.save(forum);
	}

	public Iterable<GroupChat> findAll() {
		return groupChats.findAll();
	}

	public long countGroupChat() {
		return groupChats.count();
	}

	public long countComment() {
		return comments.count();
	}

//	void addGroupChat() {
//		List<Group> groups = groupRepository.findAll().toList();
//		for(Group group : groups) {
//			createGroupChat(group.getGroupName());
//		}
//	}

	public void deleteComment(GroupChat groupChat, ForumEntry forumEntry) {
		if (groupChat.getForums().contains(forumEntry)){
			groupChat.deleteForum(forumEntry);
			comments.delete(forumEntry);
		}
	}

	public GroupChat findByGroupChatName(String name) {
		var groupChat = groupChats.findGroupChatByName(name);
		return groupChat;
	}

	public List<GroupChat> findAllGroupChat(Customer customer) {

		List<GroupChat> groupChats = new LinkedList<>();
		for(Group group: customer.getGroup()) {
			groupChats.add(findByGroupChatName(group.getGroupName()));
		}
		return groupChats;

	}

	public GroupChat findByGroupChatId(long id) {
		var groupChat = groupChats.findById(id).orElse(null);
		return groupChat;
	}


}
