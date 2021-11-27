package kickstart.catalog;

import org.javamoney.moneta.Money;



import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import kickstart.customer.Customer;

@Entity
public class Football extends Item {

	@OneToMany(cascade = CascadeType.ALL)
	private List<FootballBet> footballBets;
	@OneToOne (cascade = CascadeType.ALL)
	private Team host, guest;
	private String league, logoHost, logoGuest;
	//private int score;
	private Ergebnis ergebnis = Ergebnis.LEER;

	//private boolean check = true;


	public Football(String name, LocalDateTime date, Money price, ItemType type, Team host, Team guest, String league,
					String logoHost, String logoGuest){
		super(name, date, price, type);
		this.host = host;
		this.guest = guest;
		this.league = league;
		this.logoHost = logoHost;
		this.logoGuest = logoGuest;
		footballBets = new ArrayList<>();


	}

	public Football() {
	}

	public void addBet(FootballBet footballBet){
		footballBets.add(footballBet);
	}

	public void removeBet(FootballBet footballBet){
		footballBets.remove(footballBet);
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

	public String getLogoHost() {
		return logoHost;
	}

	public String getLogoGuest() {
		return logoGuest;
	}


	/*
	public boolean getCheck(){
		boolean t = host.getName().equals("FC Bayern München") || guest.getName().equals("FC Bayern München");
		return t;
	}

	 */

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

	public Ergebnis getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(Ergebnis ergebnis) {
		this.ergebnis = ergebnis;
	}

	public FootballBet findbyBetId(long id){
		if(footballBets.isEmpty()){
			return null;
		}

		FootballBet result = null;
		for(FootballBet fb: footballBets){
			if(fb.getId() == id){
				result = fb;
				break;
			}
		}
		return result;
	}
}
