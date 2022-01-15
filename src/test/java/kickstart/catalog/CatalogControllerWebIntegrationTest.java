package kickstart.catalog;

import kickstart.customer.Customer;
import kickstart.customer.CustomerRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerWebIntegrationTest {



	@Autowired MockMvc mvc;
	@Autowired CatalogController controller;

	@Autowired
	private LotteryCatalog lotteryCatalog;


	private Football alt,neu;

	@BeforeEach
	void setUp(){
		alt = new Football("2name2", LocalDateTime.now().minusHours(3),Money.of(10, EURO), Item.ItemType.FOOTBALL,new Team("FC Augsburg"), new Team("FC Bayern München") ,
				"1.Bundesliga","augsburg", "fcb");
		neu = new Football("3name3",LocalDateTime.now().plusDays(6),Money.of(10, EURO), Item.ItemType.FOOTBALL,new Team("Borussia Dortmund"), new Team("FC Bayern München") ,
				"1.Bundesliga","bvb", "fcb");

		lotteryCatalog.save(alt);
		lotteryCatalog.save(neu);
	}

	@Test
	void zahlennotterieMvcIntegrationTest() throws Exception {
		mvc.perform(get("/zahlenlotterie")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("ticketcatalog", is(not(emptyIterable()))));
	}

	@Test
	void footballMvcIntegrationTest() throws Exception {
		mvc.perform(get("/football")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("footballcatalog", is(not(emptyIterable()))));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void footballadminMvcIntegrationTest() throws Exception {
		mvc.perform(get("/footballadmin")). //
				andExpect(status().isOk()).//
				andExpect(model().attribute("footballcatalog", is(not(emptyIterable()))));
	}

	@AfterEach
	void breakDown(){
		lotteryCatalog.delete(alt);
		lotteryCatalog.delete(neu);
	}

}
