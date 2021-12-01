package kickstart.catalog;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import java.util.List;

import java.util.List;

public interface LotteryCatalog extends Catalog<Item> {

	public Sort mysort = Sort.by("productIdentifier").ascending();

	public Sort date_sort = Sort.by("timeLimit").ascending();

	public List<Item> findByType(Item.ItemType type, Sort sort);

	public default List<Item> findByType(Item.ItemType type) {
		return findByType(type, date_sort);
	}

}
