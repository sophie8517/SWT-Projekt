package catalog_lottery;

import videoshop.wette.Zahlenwette;

import java.time.LocalDate;
import java.util.List;

public class Tippschein extends videoshop.catalog_lottery.Item {
	public Tippschein(LocalDate datum, double preis) {
		super(datum, preis);
	}

	public void addWette(Zahlenwette zahlenwette){

	}

	public List<Zahlenwette> getWetten() {

	}

}
