package org.hisrc.zugradarscraper.service.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hisrc.zugradarscraper.model.Stop;
import org.hisrc.zugradarscraper.model.Stops;
import org.hisrc.zugradarscraper.model.TrainId;
import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.model.TrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.model.TrainRouteMultiPointFeature;
import org.hisrc.zugradarscraper.model.TrainRouteMultiPointFeatureCollection;
import org.hisrc.zugradarscraper.model.TrainRouteSection;
import org.hisrc.zugradarscraper.service.TrainRouteRetriever;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TrainRouteRetrieverTest {

	private final TrainRouteRetriever sut = new TrainRouteRetriever();

	@Test
	public void retrievesTrainRoute() throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		final TrainRoute trainRoute = sut.retrieve(new TrainId("ICE", "596", "84/110009/18/19/80"), LocalDate.of(2016,9,1));
		final Set<Stop> stopsSet = new LinkedHashSet<>();
		for (TrainRouteSection section : trainRoute.getSections())
		{
			stopsSet.add(section.getDeparture().getStop());
			stopsSet.add(section.getArrival().getStop());
		}
		final Stops stops = new Stops(new ArrayList<>(stopsSet));
		mapper.writerFor(Stops.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("stops.js")), stops);
		
		final TrainRouteMultiLineStringFeature feature = new TrainRouteMultiLineStringFeature(trainRoute);
		Assert.assertEquals(11, trainRoute.getSections().size());
		final TrainRouteMultiLineStringFeatureCollection featureCollection = new TrainRouteMultiLineStringFeatureCollection(
				Arrays.asList(feature));
		mapper.writerFor(TrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("lines.js")), featureCollection);
		
		final TrainRouteMultiPointFeature multiPointFeature = trainRoute.interpolateTrainRouteMultiPointFeature(10000);
		final TrainRouteMultiPointFeatureCollection multiPointFeatureCollection = new TrainRouteMultiPointFeatureCollection(
				Arrays.asList(multiPointFeature));
		mapper.writerFor(TrainRouteMultiPointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("tracks.js")), multiPointFeatureCollection);
		
	}
}
