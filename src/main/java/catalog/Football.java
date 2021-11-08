package catalog_lottery;

import org.javamoney.moneta.Money;
import videoshop.wette.Fußballwette;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Football extends videoshop.catalog_lottery.Item {
	private String host, gast, liga;


	private List<Fußballwette> fußballwetten;

	public Football(LocalDate datum, Money preis, String name, String host, String gast, String liga) {
		super(datum, preis, name);
		this.host = host;
		this.gast = gast;
		this.liga = liga;
		fußballwetten = new ArrayList<>();
	}

	public void addWette(Fußballwette fußballwette){
		fußballwetten.add(fußballwette);
	}

	public String getHost(){
		return host;
	}

	public String getGast(){
		return gast;
	}

	public String getLiga(){
		return liga;
	}

	public List<Fußballwette> getFußballwetten(){
		return fußballwetten;
	}
}
