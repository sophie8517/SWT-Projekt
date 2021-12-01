package kickstart.customer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class GroupRegistrationForm {
	@NotEmpty(message = "{RegistrationForm.firstname.NotEmpty}") //
	private final String groupName;

	public GroupRegistrationForm(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}
}
