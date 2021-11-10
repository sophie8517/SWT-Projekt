package kickstart.catalog;

import org.javamoney.moneta.Money;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Football extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<FootballBet> footballBets = new ArrayList<>();

	private String host, guest, league;


	public Football(String name, LocalDate date, Money price, ItemType type, String host, String guest, String league){
		super(name, date, price, type);
		this.host = host;
		this.guest = guest;
		this.league = league;
		//footballBets = new ArrayList<>();
	}

	public Football() {
	}

	public void addBet(FootballBet footballBet){
		footballBets.add(footballBet);
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


	public List<FootballBet> getFootballBits() {
		return footballBets;
	}
}