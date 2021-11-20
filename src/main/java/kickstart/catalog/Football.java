package kickstart.catalog;

import org.javamoney.moneta.Money;



import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kickstart.customer.Customer;

@Entity
public class Football extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<FootballBet> footballBets;
	@OneToOne (cascade = CascadeType.ALL)
	private Team host, guest;
	private String league, logo_host, logo_guest;
	private int score;


	public Football(String name, LocalDate date, Money price, ItemType type, Team host, Team guest, String league, String logo_host, String logo_guest){
		super(name, date, price, type);
		this.host = host;
		this.guest = guest;
		this.league = league;
		this.logo_host = logo_host;
		this.logo_guest = logo_guest;
		footballBets = new ArrayList<>();
	}

	public Football() {
	}

	public void addBet(FootballBet footballBet){
		footballBets.add(footballBet);
	}

	public Team getHost() {
		return host;
	}

	public Team getGuest() {
		return guest;
	}

	public String getLeague() {
		return league;
	}

	public String getLogo_host() {
		return logo_host;
	}

	public String getLogo_guest() {
		return logo_guest;
	}

	public List<FootballBet> getFootballBets() {
		return footballBets;
	}

	public List<FootballBet> getFootballBetsbyCustomer(Customer c) {
		List<FootballBet> result = new ArrayList<>();

		for (FootballBet fb : footballBets) {
			if (fb.getCustomer().equals(c)) {
				result.add(fb);
			}
		}
		return result;
	}
}
