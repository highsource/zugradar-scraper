package org.hisrc.zugradarscraper.service.tests;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import org.hisrc.zugradarscraper.model.TrainId;
import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.service.TrainRouteRetriever;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TrainRouteRetrieverTest {

	private final TrainRouteRetriever sut = new TrainRouteRetriever();

	@Test
	public void retrievesTrainRoute() throws IOException {
		final TrainRoute trainRoute = sut.retrieve(new TrainId("ICE", "596", "84/110009/18/19/80"), LocalDate.now());
		final TrainRouteMultiLineStringFeature feature = new TrainRouteMultiLineStringFeature(trainRoute);
		Assert.assertEquals(11, trainRoute.getSections().size());
		final TrainRouteMultiLineStringFeatureCollection featureCollection = new TrainRouteMultiLineStringFeatureCollection(
				Arrays.asList(feature));
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.writerFor(TrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter().writeValue(System.out, featureCollection);
	}
}
