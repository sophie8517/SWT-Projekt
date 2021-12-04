package kickstart.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerWebIntegrationTest {

	@Autowired MockMvc mvc;
	@Autowired CatalogController controller;

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

}
