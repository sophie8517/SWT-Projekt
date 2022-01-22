package kickstart.catalog;

import javax.persistence.*;

@Entity
public class Team {
	private @Id @GeneratedValue long id;

	private String name;


	public Team(String name){

		this.name = name;
	}

	public Team(){}

	public String getName(){
		return name;
	}

	/*
	public void setName(String name) {
		this.name = name;
	}

	 */

	@Override
	public String toString() {
		return name;
	}
}
