package kickstart.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface LotteryCatalog extends Catalog<Item> {

	public Sort mysort = Sort.by("productIdentifier").ascending();

	public Iterable<Item> findByType(Item.ItemType type, Sort sort);

	public default Iterable<Item> findByType(Item.ItemType type) {
		return findByType(type, mysort);
	}

}
