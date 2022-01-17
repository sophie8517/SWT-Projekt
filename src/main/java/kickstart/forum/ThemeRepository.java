package kickstart.forum;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface ThemeRepository extends CrudRepository<Theme, Long> {
	@Override
	Iterable<Theme> findAll();
	Theme findThemeByName(String name);
}