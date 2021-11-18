package kickstart.catalog;

import kickstart.customer.Customer;
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

	private String host, guest, league, logo_host, logo_guest;


	public Football(String name, LocalDate date, Money price, ItemType type, String host, String guest, String league, String logo_host, String logo_guest){
		super(name, date, price, type);
		this.host = host;
		this.guest = guest;
		this.league = league;
		this.logo_host = logo_host;
		this.logo_guest = logo_guest;

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

	public String getLogo_host() {
		return logo_host;
	}

	public String getLogo_guest() {
		return logo_guest;
	}

	public List<FootballBet> getFootballBets() {
		return footballBets;
	}

	public List<FootballBet> getFootballBetsbyCustomer(Customer c){
		List<FootballBet> result = new ArrayList<>();

		for(FootballBet fb: footballBets){
			if(fb.getCustomer().equals(c)){
				result.add(fb);
			}
		}
		return result;
	}
}