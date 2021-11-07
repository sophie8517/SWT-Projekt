package kickstart.catalog;

import kickstart.catalog.FootballBit;
import kickstart.catalog.Item;
import kickstart.catalog.NumberBit;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Football extends Item {

	@OneToMany
	private List<FootballBit> footballBits;

	private String host, guest, league;

	public Football(int id, LocalDate date, double price, String host, String guest, String league){
		super(id, date, price);
		this.host = host;
		this.guest = guest;
		this.league = league;
		setType("Football");
		footballBits = new ArrayList<>();
	}

	public Football() {

	}

	public void addBit(FootballBit footballBit){
		footballBits.add(footballBit);
	}

	public String getHost() {
		return host;
	}

	public String getGuest() {
		return guest;
	}

	public String getLeague() {
		return league;
	}

	public List<FootballBit> getFootballBits() {
		return footballBits;
	}
}