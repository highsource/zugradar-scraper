package org.hisrc.zugradarscraper.service.tests;

import java.io.IOException;
import java.util.Arrays;

import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.service.TrainRouteRetriever;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainRouteRetrieverTest {
	
	private final TrainRouteRetriever sut = new TrainRouteRetriever();

	@Test
	public void retrievesTrainRoute() throws IOException
	{
		final TrainRoute trainRoute = sut.retrieve("84/110009/18/19/80");
		final TrainRouteMultiLineStringFeature feature = new TrainRouteMultiLineStringFeature(trainRoute);
		final TrainRouteMultiLineStringFeatureCollection featureCollection = new TrainRouteMultiLineStringFeatureCollection(Arrays.asList(feature));
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writerFor(TrainRouteMultiLineStringFeatureCollection.class).writeValue(System.out, featureCollection);
	}
}
