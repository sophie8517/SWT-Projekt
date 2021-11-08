package catalog_lottery;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.salespointframework.core.Currencies.EURO;

public class CatalogDataInitializer implements DataInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);
    private LotterieCatalog lotterieKatalog;

    public CatalogDataInitializer(LotterieCatalog lotterieCatalog){
        this.lotterieKatalog =  lotterieCatalog;
    }

    @Override
    public void initialize() {

        if (lotterieKatalog.findAll().iterator().hasNext()) {
            return;
        }

        LOG.info("Creating default catalog entries.");

        lotterieKatalog.save(new Football(LocalDate.of (2022, 1, 1), Money.of(5,EURO), "Spiel 1","Wolfsburg ","Freiburg", "1"));
    }


}
