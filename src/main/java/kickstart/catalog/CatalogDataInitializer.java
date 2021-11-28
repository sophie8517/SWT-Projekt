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
import java.time.LocalDateTime;
import java.time.LocalTime;

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


		lotteryCatalog.save(new Ticket("name1", LocalDateTime.of(LocalDate.of(2021,12,5),
				LocalTime.of(15,00)), Money.of(7,EURO), ItemType.TICKET));

		lotteryCatalog.save(new Football("name5",LocalDateTime.of(LocalDate.of(2021,11,26),LocalTime.of(17,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("FC Bayern München") ,
				"1.Bundesliga","augsburg", "fcb"));
		lotteryCatalog.save(new Football("name6",LocalDateTime.of(LocalDate.of(2021,11,24), LocalTime.of(17,32)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("TSG Hoffenheim"), new Team("RB Leipzig"),
				"1.Bundesliga","hoffenheim", "rb"));
		lotteryCatalog.save(new Football("name7",LocalDateTime.of(LocalDate.of(2021,12,20), LocalTime.of(15,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Arminia Bielefeld"), new Team("VFL Wolfsburg"),
				"1.Bundesliga","bielefeld", "wolfsburg"));
		lotteryCatalog.save(new Football("name8",LocalDateTime.of(LocalDate.of(2021,12,20), LocalTime.of(15,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Bayer Leverkusen 04"), new Team("VFL Bochum"),
				"1.Bundesliga","leverkusen", "bochum"));
		lotteryCatalog.save(new Football("name9",LocalDateTime.of(LocalDate.of(2021,12,20), LocalTime.of(15,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Borussia Mönchengladbach"), new Team("Greuther Fürth"),
				"1.Bundesliga","gladbach", "fuerth"));
		lotteryCatalog.save(new Football("name10",LocalDateTime.of(LocalDate.of(2021,12,20), LocalTime.of(15,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Borussia Dortmund"), new Team("VfB Stuttgart"),
				"1.Bundesliga","bvb", "stuttgart"));
		lotteryCatalog.save(new Football("name11",LocalDateTime.of(LocalDate.of(2021,12,20), LocalTime.of(20,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Union Berlin"), new Team("Hertha BSC"),
				"1.Bundesliga","union", "hertha"));
		lotteryCatalog.save(new Football("name12",LocalDateTime.of(LocalDate.of(2021,12,21), LocalTime.of(21,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("SC Freiburg"), new Team("Eintracht Frankfurt"),
				"1.Bundesliga","freiburg", "eintracht"));
		lotteryCatalog.save(new Football("name13",LocalDateTime.of(LocalDate.of(2021,12,21), LocalTime.of(18,00)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FSV Mainz 05"), new Team("FC Köln"),
				"1.Bundesliga","mainz", "koeln"));
	}
}