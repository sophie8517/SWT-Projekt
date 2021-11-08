package catalog_lottery;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface LotterieCatalog extends Catalog<catalog_lottery.Item> {
    static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

}
