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

		Ticket t = new Ticket("name1", LocalDateTime.of(LocalDate.of(2022,2,6),
				LocalTime.of(15,0)), Money.of(7,EURO), ItemType.TICKET);
		lotteryCatalog.save(t);



		lotteryCatalog.save(new Football("name2",LocalDateTime.of(LocalDate.of(2021,11,26),LocalTime.of(17,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("FC Bayern München") ,
				"1.Bundesliga","augsburg", "fcb"));
		lotteryCatalog.save(new Football("name3",LocalDateTime.of(LocalDate.of(2022,1,23), LocalTime.of(15,30)),
				Money.of(15, EURO),ItemType.FOOTBALL,new Team("RB Leipzig"), new Team("VfL Wolfsburg"),
				"1.Bundesliga","rb", "wolfsburg"));


		lotteryCatalog.save(new Football("name4",LocalDateTime.of(LocalDate.of(2022,1,22), LocalTime.of(20,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("FC Erzgebirge Aue"), new Team("FC Schalke 04"),
				"2.Bundesliga","aue", "schalke"));

		//----

		lotteryCatalog.save(new Football("name5",LocalDateTime.of(LocalDate.of(2022,2,5), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("1. FSV Mainz 05"), new Team("TSG Hoffenheim"),
				"1.Bundesliga","mainz", "hoffenheim"));

		lotteryCatalog.save(new Football("name6",LocalDateTime.of(LocalDate.of(2022,2,6), LocalTime.of(15,30)),
				Money.of(13, EURO),ItemType.FOOTBALL,new Team("Borussia Dortmund"), new Team("Bayer Leverkusen 04"),
				"1.Bundesliga","bvb", "leverkusen"));

		lotteryCatalog.save(new Football("name7",LocalDateTime.of(LocalDate.of(2022,2,5), LocalTime.of(18,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("FC Bayern München"), new Team("RB Leipzig"),
				"1.Bundesliga","fcb", "rb"));

		lotteryCatalog.save(new Football("name8",LocalDateTime.of(LocalDate.of(2022,2,6), LocalTime.of(17,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("VfL Wolfsburg"), new Team("SpVgg Greuther Fürth"),
				"1.Bundesliga","wolfsburg", "fuerth"));

		lotteryCatalog.save(new Football("name9",LocalDateTime.of(LocalDate.of(2022,2,12), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("SC Freiburg"), new Team("1. FSV Mainz 05"),
				"1.Bundesliga","freiburg", "mainz"));

		lotteryCatalog.save(new Football("name10",LocalDateTime.of(LocalDate.of(2022,2,13), LocalTime.of(17,30)),
				Money.of(20, EURO),ItemType.FOOTBALL,new Team("TSG Hoffenheim"), new Team("Arminia Bielefeld"),
				"1.Bundesliga","hoffenheim", "bielefeld"));

		lotteryCatalog.save(new Football("name11",LocalDateTime.of(LocalDate.of(2022,2,12), LocalTime.of(15,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("VfL Bochum"), new Team("FC Bayern München"),
				"1.Bundesliga","bochum", "fcb"));


		lotteryCatalog.save(new Football("name12",LocalDateTime.of(LocalDate.of(2022,2,12), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Borussia Mönchengladbach"), new Team("FC Augsburg"),
				"1.Bundesliga","gladbach", "augsburg"));



		lotteryCatalog.save(new Football("name13",LocalDateTime.of(LocalDate.of(2022,1,22), LocalTime.of(13,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("Hansa Rostock"), new Team("FC Heidenheim"),
				"2.Bundesliga","hansa", "heidenheim"));

		lotteryCatalog.save(new Football("name14",LocalDateTime.of(LocalDate.of(2022,1,21), LocalTime.of(18,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Hamburger SV"), new Team("St. Pauli"),
				"2.Bundesliga","hsv", "stpauli"));

		lotteryCatalog.save(new Football("name15",LocalDateTime.of(LocalDate.of(2022,2,6), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("SV Darmstadt 98"), new Team("Hamburger SV"),
				"2.Bundesliga","darmstadt", "hsv"));

		lotteryCatalog.save(new Football("name16",LocalDateTime.of(LocalDate.of(2022,2,6), LocalTime.of(13,30)),
				Money.of(12, EURO),ItemType.FOOTBALL,new Team("Holstein Kiel"), new Team("Fortuna Düsseldorf"),
				"2.Bundesliga","holstein", "fortuna"));

		lotteryCatalog.save(new Football("name17",LocalDateTime.of(LocalDate.of(2022,2,6), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("Dynamo Dresden"), new Team("Hansa Rostock"),
				"2.Bundesliga","dynamo", "hansa"));

		lotteryCatalog.save(new Football("name18",LocalDateTime.of(LocalDate.of(2022,2,8), LocalTime.of(18,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Karlsruher SC"), new Team("SV Sandhausen"),
				"2.Bundesliga","ksc", "sandhausen"));

		lotteryCatalog.save(new Football("name19",LocalDateTime.of(LocalDate.of(2022,2,13), LocalTime.of(13,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("Hannover 96"), new Team("SV Darmstadt 98"),
				"2.Bundesliga","hannover", "darmstadt"));


		lotteryCatalog.save(new Football("name20",LocalDateTime.of(LocalDate.of(2022,2,12), LocalTime.of(20,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("SSV Jahn Regensburg"), new Team("St. Pauli"),
				"2.Bundesliga","regensburg", "stpauli"));

		/*
		lotteryCatalog.save(new Football("name21",LocalDateTime.of(LocalDate.of(2022,1,8), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("FC Heidenheim"), new Team("SSV Jahn Regensburg"),
				"2.Bundesliga","heidenheim", "regensburg"));

		 */

	}
}
