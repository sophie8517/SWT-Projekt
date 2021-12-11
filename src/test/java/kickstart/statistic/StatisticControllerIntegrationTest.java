package kickstart.statistic;


import kickstart.AbstractIntegrationTest;
import kickstart.catalog.FootballBet;
import kickstart.catalog.Item;
import kickstart.catalog.LotteryCatalog;
import kickstart.catalog.Ticket;
import kickstart.customer.*;
import kickstart.statistic.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.fail;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticControllerIntegrationTest extends AbstractIntegrationTest{

	@Autowired
	StatisticController statisticController;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerManagement customerManagement;

	private Customer customer;

	@BeforeEach
	void setUp() {
		customer = customerRepository.findAll().get().findFirst().get();
		customer = customerManagement.findByUserAccount(customer.getUserAccount());
	}

	@Test
	public void StatisticControllerIntegrationTestStatistic(){
		Model model = new ExtendedModelMap();
		String returnedView = statisticController.statistic(model);
		assertThat(returnedView).isEqualTo("statistic");
	}

	@Test
	public void StatisticControllerIntegrationToBetPage(){
		Model model = new ExtendedModelMap();
		String returnedView = statisticController.toBetPage(model, customer.getId());
		assertThat(returnedView).isEqualTo("statistic_bets");
	}
}
