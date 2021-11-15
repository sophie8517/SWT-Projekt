package kickstart.catalog;

import kickstart.catalog.LotteryCatalog;
import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.salespointframework.core.Currencies.*;

import kickstart.catalog.Item.ItemType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


import java.time.LocalDate;

@Component
class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private final LotteryCatalog lotteryCatalog;

	public CatalogDataInitializer(LotteryCatalog lotteryCatalog){

		Assert.notNull(lotteryCatalog, "Catalog must not be null!");

		this.lotteryCatalog = lotteryCatalog;
	}

	@Override
	public void initialize(){

		if (lotteryCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default lotteryCatalog entries.");


		lotteryCatalog.save(new Ticket("name1",LocalDate.of(2021,11,5), Money.of(7,EURO), ItemType.TICKET));

		lotteryCatalog.save(new Football("name5",LocalDate.of(2021,11,19),
				Money.of(12.50, EURO),ItemType.FOOTBALL,"FC Augsburg", "FC Bayern München",
				"1.Bundesliga","augsburg", "fcb"));
		lotteryCatalog.save(new Football("name6",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"TSG Hoffenheim", "RB Leipzig",
				"1.Bundesliga","hoffenheim", "rb"));
		lotteryCatalog.save(new Football("name7",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"Arminia Bielefeld", "VFL Wolfsburg",
				"1.Bundesliga","bielefeld", "wolfsburg"));
		lotteryCatalog.save(new Football("name8",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"Bayer Leverkusen 04", "VFL Bochum",
				"1.Bundesliga","leverkusen", "bochum"));
		lotteryCatalog.save(new Football("name9",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"Borussia Mönchengladbach", "Greuther Fürth",
				"1.Bundesliga","gladbach", "fuerth"));
		lotteryCatalog.save(new Football("name10",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"Borussia Dortmund", "VfB Stuttgart",
				"1.Bundesliga","bvb", "stuttgart"));
		lotteryCatalog.save(new Football("name11",LocalDate.of(2021,11,20),
				Money.of(10, EURO),ItemType.FOOTBALL,"1. FC Union Berlin", "Hertha BSC",
				"1.Bundesliga","union", "hertha"));
		lotteryCatalog.save(new Football("name12",LocalDate.of(2021,11,21),
				Money.of(10, EURO),ItemType.FOOTBALL,"SC Freiburg", "Eintracht Frankfurt",
				"1.Bundesliga","freiburg", "eintracht"));
		lotteryCatalog.save(new Football("name13",LocalDate.of(2021,11,21),
				Money.of(10, EURO),ItemType.FOOTBALL,"1. FSV Mainz 05", "1. FC Köln",
				"1.Bundesliga","mainz", "koeln"));
	}
}