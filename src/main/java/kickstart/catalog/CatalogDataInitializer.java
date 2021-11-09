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
		lotteryCatalog.save(new Football("name2", LocalDate.of(2021,11,7),Money.of(8, EURO),ItemType.FOOTBALL,"FC Bayern München", "BVB", "1.Bundesliga"));
		lotteryCatalog.save(new Football("name3",LocalDate.of(2021,11,20),Money.of(8, EURO),ItemType.FOOTBALL,"Hansa Rostock", "FC Erzgebirge", "2.Bundesliga"));
		lotteryCatalog.save(new Football("name4",LocalDate.of(2021,12,11),Money.of(10, EURO),ItemType.FOOTBALL,"RB Leipzig", "Borussia Mönchengladbach", "1.Bundesliga"));

	}
}