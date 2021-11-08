package catalog_lottery;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import java.time.LocalDate;

public class Item extends Product {
	private LocalDate datum;
	private Money preis;
	private String type;

	public Item (LocalDate datum, Money preis, String name){
		super(name, preis);
		this.datum = datum;

	}

	public LocalDate getDatum() {
		return datum;
	}

	public Money getPreis() {
		return preis;
	}

	public String getType() {
		return type;
	}
}
