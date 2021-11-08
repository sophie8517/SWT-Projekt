package kickstart.catalog;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Football extends Item {

	@OneToMany
	private List<FootballBet> footballBets;

	private String host, guest, league;

	public Football(LocalDate date, double price, String host, String guest, String league){
		super(date, price);
		this.host = host;
		this.guest = guest;
		this.league = league;
		setType(ItemType.FOOTBALL);
		footballBets = new ArrayList<>();
	}

	public Football() {

	}

	public void addBet(FootballBet footballBet){
		footballBets.add(footballBet);
	}

	public String getHost() {
		return host;
	}

	//public void setHost(String host) {
	//	this.host = host;
	//}

	public String getGuest() {
		return guest;
	}

	//public void setGuest(String guest) {
	//	this.guest = guest;
	//}

	public String getLeague() {
		return league;
	}

	//public void setLeague(String league) {
	//	this.league = league;
	//}


	public List<FootballBet> getFootballBits() {
		return footballBets;
	}

	//public void setFootballBits(List<FootballBet> footballBits) {
	//	this.footballBets = footballBits;
	//}
}