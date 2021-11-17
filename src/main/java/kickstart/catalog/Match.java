package kickstart.catalog;

import java.util.List;

public class Match {
	private Team host, guest;
	private Status status = Status.OPEN;

	public Match(Team host, Team guest){
		this.host = host;
		this.guest = guest;
	}

	public Status result(){
		if (guest.getScore() == host.getScore()){
			//guest.setStatus(Status.DRAW);
			//host.setStatus(Status.DRAW);
			//return guest.getName() + " drew with " + host.getName();
			return Status.DRAW;
		} else if (guest.getScore() > host.getScore()) {
			//guest.setStatus(Status.WIN);
			//host.setStatus(Status.LOSS);
			// return guest.getName() + " wins. ";
			return Status.LOSS;
		} else {
			//guest.setStatus(Status.LOSS);
			//host.setStatus(Status.WIN);
			//return host.getName() + " wins. ";
			return Status.WIN;
		}
	}


}
