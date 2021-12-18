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


		lotteryCatalog.save(new Ticket("name1", LocalDateTime.of(LocalDate.of(2022,1,19),
				LocalTime.of(15,00)), Money.of(7,EURO), ItemType.TICKET));

		lotteryCatalog.save(new Football("name2",LocalDateTime.of(LocalDate.of(2022,1,26),LocalTime.of(17,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("FC Bayern München") ,
				"1.Bundesliga","augsburg", "fcb"));
		lotteryCatalog.save(new Football("name3",LocalDateTime.of(LocalDate.of(2022,1,24), LocalTime.of(17,32)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("TSG Hoffenheim"), new Team("RB Leipzig"),
				"1.Bundesliga","hoffenheim", "rb"));


		lotteryCatalog.save(new Football("name4",LocalDateTime.of(LocalDate.of(2022,1,3), LocalTime.of(20,30)),
				Money.of(12, EURO),ItemType.FOOTBALL,new Team("1.FC Union Berlin"), new Team("RB Leipzig"),
				"1.Bundesliga","union", "rb"));

		lotteryCatalog.save(new Football("name5",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("TSG Hoffenheim"), new Team("Eintracht Frankfurt"),
				"1.Bundesliga","hoffenheim", "eintracht"));

		lotteryCatalog.save(new Football("name6",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(15,30)),
				Money.of(13, EURO),ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("VfL Bochum"),
				"1.Bundesliga","augsburg", "bochum"));

		lotteryCatalog.save(new Football("name7",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(15,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("Bayer Leverkusen 04"), new Team("SpVgg Greuther Fürth"),
				"1.Bundesliga","leverkusen", "fuerth"));

		lotteryCatalog.save(new Football("name8",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(15,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("Arminia Bielefeld"), new Team("1.FC Köln"),
				"1.Bundesliga","bielefeld", "koeln"));

		lotteryCatalog.save(new Football("name9",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(15,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("1.FSV Mainz 05"), new Team("VfL Wolfsburg"),
				"1.Bundesliga","mainz", "wolfsburg"));

		lotteryCatalog.save(new Football("name10",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(18,30)),
				Money.of(20, EURO),ItemType.FOOTBALL,new Team("Borussia Dortmund"), new Team("FC Bayern München"),
				"1.Bundesliga","bvb", "fcb"));

		lotteryCatalog.save(new Football("name11",LocalDateTime.of(LocalDate.of(2022,1,5), LocalTime.of(15,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("VfB Stuttgart"), new Team("Hertha BSC"),
				"1.Bundesliga","stuttgart", "hertha"));

		lotteryCatalog.save(new Football("name12",LocalDateTime.of(LocalDate.of(2022,1,5), LocalTime.of(17,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("Borussia Mönchengladbach"), new Team("SC Freiburg"),
				"1.Bundesliga","gladbach", "freiburg"));

		lotteryCatalog.save(new Football("name13",LocalDateTime.of(LocalDate.of(2022,1,3), LocalTime.of(18,30)),
				Money.of(9, EURO),ItemType.FOOTBALL,new Team("SV Darmstadt 98"), new Team("Fortuna Düssedorf"),
				"2.Bundesliga","darmstadt", "fortuna"));

		lotteryCatalog.save(new Football("name14",LocalDateTime.of(LocalDate.of(2022,1,3), LocalTime.of(18,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Werder Bremen"), new Team("FC Erzgebirge Aue"),
				"2.Bundesliga","bremen", "aue"));

		lotteryCatalog.save(new Football("name15",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("SV Sandhausen"), new Team("SC Paderborn"),
				"2.Bundesliga","sandhausen", "paderborn"));

		lotteryCatalog.save(new Football("name16",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(13,30)),
				Money.of(12, EURO),ItemType.FOOTBALL,new Team("Hansa Rostock"), new Team("FC Ingolstadt 04"),
				"2.Bundesliga","hansa", "ingolstadt"));

		lotteryCatalog.save(new Football("name17",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("1.FC Nürnberg"), new Team("Holstein Kiel"),
				"2.Bundesliga","nuernberg", "holstein"));

		lotteryCatalog.save(new Football("name18",LocalDateTime.of(LocalDate.of(2022,1,4), LocalTime.of(20,30)),
				Money.of(14, EURO),ItemType.FOOTBALL,new Team("St. Pauli"), new Team("FC Schalke 04"),
				"2.Bundesliga","stpauli", "schalke"));

		lotteryCatalog.save(new Football("name19",LocalDateTime.of(LocalDate.of(2022,1,5), LocalTime.of(13,30)),
				Money.of(10, EURO),ItemType.FOOTBALL,new Team("Hannover 96"), new Team("Hamburger SV"),
				"2.Bundesliga","hannover", "hsv"));

		lotteryCatalog.save(new Football("name20",LocalDateTime.of(LocalDate.of(2022,1,5), LocalTime.of(13,30)),
				Money.of(13, EURO),ItemType.FOOTBALL,new Team("Dynamo Dresden"), new Team("Karlsruher SC"),
				"2.Bundesliga","dynamo", "ksc"));

		lotteryCatalog.save(new Football("name21",LocalDateTime.of(LocalDate.of(2022,1,5), LocalTime.of(13,30)),
				Money.of(8, EURO),ItemType.FOOTBALL,new Team("1.FC Heidenheim"), new Team("SSV Jahn Regensburg"),
				"2.Bundesliga","heidenheim", "regensburg"));


	}
}