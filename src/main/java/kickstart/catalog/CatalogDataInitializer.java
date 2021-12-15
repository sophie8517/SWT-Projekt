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


		lotteryCatalog.save(new Ticket("name1", LocalDateTime.of(LocalDate.of(2021,12,19),
				LocalTime.of(15,00)), Money.of(7,EURO), ItemType.TICKET));

		lotteryCatalog.save(new Football("name2",LocalDateTime.of(LocalDate.of(2021,11,26),LocalTime.of(17,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("FC Bayern München") ,
				"1.Bundesliga","augsburg", "fcb"));
		lotteryCatalog.save(new Football("name3",LocalDateTime.of(LocalDate.of(2021,12,17), LocalTime.of(20,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Bayern München"), new Team("VfL Wolfsburg"),
				"1.Bundesliga","fcb", "wolfsburg"));


		lotteryCatalog.save(new Football("name4",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(15,30)),
				Money.of(12, EURO),ItemType.FOOTBALL,new Team("VfL Bochum"), new Team("1.FC Union Berlin"),
				"1.Bundesliga","bochum", "union"));

		lotteryCatalog.save(new Football("name5",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Eintracht Frankfurt"), new Team("1. FSV Mainz 05"),
				"1.Bundesliga","eintracht", "mainz"));

		lotteryCatalog.save(new Football("name6",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(15,30)),
				Money.of(13, EURO),ItemType.FOOTBALL,new Team("TSG Hoffenheim"), new Team("Borussia Mönchengladbach"),
				"1.Bundesliga","hoffenheim", "gladbach"));

		lotteryCatalog.save(new Football("name7",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(15,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("SpVgg Greuther Fürth"), new Team("FC Augsburg"),
				"1.Bundesliga","fuerth", "augsburg"));

		lotteryCatalog.save(new Football("name8",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(15,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("RB Leipzig"), new Team("Arminia Bielefeld"),
				"1.Bundesliga","rb", "bielefeld"));

		lotteryCatalog.save(new Football("name9",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(18,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Hertha BSC"), new Team("Borussia Dortmund"),
				"1.Bundesliga","hertha", "bvb"));

		lotteryCatalog.save(new Football("name10",LocalDateTime.of(LocalDate.of(2021,12,19), LocalTime.of(15,30)),
				Money.of(20, EURO),ItemType.FOOTBALL,new Team("SC Freiburg"), new Team("Bayer 04 Leverkusen"),
				"1.Bundesliga","freiburg", "leverkusen"));

		lotteryCatalog.save(new Football("name11",LocalDateTime.of(LocalDate.of(2021,12,19), LocalTime.of(17,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("1. FC Köln"), new Team("VfB Stuttgart"),
				"1.Bundesliga","koeln", "stuttgart"));

		//----2.Liga-------------

		lotteryCatalog.save(new Football("name13",LocalDateTime.of(LocalDate.of(2021,12,17), LocalTime.of(18,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("Holstein Kiel"), new Team("St. Pauli"),
				"2.Bundesliga","holstein", "stpauli"));

		lotteryCatalog.save(new Football("name14",LocalDateTime.of(LocalDate.of(2021,12,17), LocalTime.of(18,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Fortuna Düsseldorf"), new Team("SV Sandhausen"),
				"2.Bundesliga","fortuna", "sandhausen"));

		lotteryCatalog.save(new Football("name15",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("FC Erzgebirge Aue"), new Team("1. FC Nürnberg"),
				"2.Bundesliga","aue", "nuernberg"));

		lotteryCatalog.save(new Football("name16",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(13,30)),
				Money.of(12, EURO),ItemType.FOOTBALL,new Team("FC Ingolstadt 04"), new Team("Dynamo Dresden"),
				"2.Bundesliga","ingolstadt", "dynamo"));

		lotteryCatalog.save(new Football("name17",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("SC Paderborn"), new Team("1.FC Heidenheim"),
				"2.Bundesliga","paderborn", "heidenheim"));

		lotteryCatalog.save(new Football("name18",LocalDateTime.of(LocalDate.of(2021,12,18), LocalTime.of(20,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("Hamburger SV"), new Team("FC Schalke 04"),
				"2.Bundesliga","hsv", "schalke"));

		lotteryCatalog.save(new Football("name19",LocalDateTime.of(LocalDate.of(2021,12,19), LocalTime.of(13,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Karlsruher SC"), new Team("Hansa Rostock"),
				"2.Bundesliga","ksc", "hansa"));

		lotteryCatalog.save(new Football("name20",LocalDateTime.of(LocalDate.of(2021,12,19), LocalTime.of(13,30)),
				Money.of(13, EURO),ItemType.FOOTBALL,new Team("SSV Jahn Regensburg"), new Team("SV Darmstadt 98"),
				"2.Bundesliga","regensburg", "darmstadt"));

		lotteryCatalog.save(new Football("name21",LocalDateTime.of(LocalDate.of(2021,12,19), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("Hannover 96"), new Team("Werder Bremen"),
				"2.Bundesliga","hannover", "bremen"));


	}
}