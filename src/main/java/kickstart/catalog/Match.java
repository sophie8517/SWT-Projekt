package kickstart.catalog;

import java.util.List;

public class Match {
	private Team host, guest;
	private Ergebnis status = Ergebnis.LEER;

	public Match(Team host, Team guest){
		this.host = host;
		this.guest = guest;
	}

	public Ergebnis result(){
		if (guest.getScore() == host.getScore()){
			//guest.setStatus(Status.DRAW);
			//host.setStatus(Status.DRAW);
			//return guest.getName() + " drew with " + host.getName();
			return Ergebnis.UNENTSCHIEDEN;
		} else if (guest.getScore() > host.getScore()) {
			//guest.setStatus(Status.WIN);
			//host.setStatus(Status.LOSS);
			// return guest.getName() + " wins. ";
			return Ergebnis.GASTSIEG;
		} else {
			//guest.setStatus(Status.LOSS);
			//host.setStatus(Status.WIN);
			//return host.getName() + " wins. ";
			return Ergebnis.HEIMSIEG;
		}
	}


}
