package kickstart.catalog;

import kickstart.catalog.LotteryCatalog;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private LotteryCatalog lotteryCatalog;

	public CatalogDataInitializer(LotteryCatalog lotteryCatalog){
		this.lotteryCatalog = lotteryCatalog;
	}

	@Override
	public void initialize(){
		if (lotteryCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default lotterycatalog entries.");


		lotteryCatalog.save(new Ticket(LocalDate.of(2021,11,5), 7.00));
		lotteryCatalog.save(new Football(LocalDate.of(2021,11,7),8.00,"FC Bayern München", "BVB", "1.Bundesliga"));
		lotteryCatalog.save(new Football(LocalDate.of(2021,11,20),5.00,"Hansa Rostock", "FC Erzgebirge", "2.Bundesliga"));
		lotteryCatalog.save(new Football(LocalDate.of(2021,12,11),8.00,"RB Leipzig", "Borussia Mönchengladbach", "1.Bundesliga"));

	}
}