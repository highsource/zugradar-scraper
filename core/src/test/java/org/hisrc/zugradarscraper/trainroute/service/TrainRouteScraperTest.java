package org.hisrc.zugradarscraper.trainroute.service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;

import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.application.TrainRouteScraper;
import org.junit.Test;

public class TrainRouteScraperTest {

	@Test
	public void scrapesTrainRoute() throws FileNotFoundException {

		final TrainRouteScraper trainRouteScraper = new TrainRouteScraper();
		trainRouteScraper.scrapeTrainRoutes(
				Arrays.asList(new TrainId("ICE", "373", "84/184140/18/19/80", LocalDate.of(2017, 5, 2))), System.out,
				System.out	);
	}
}
