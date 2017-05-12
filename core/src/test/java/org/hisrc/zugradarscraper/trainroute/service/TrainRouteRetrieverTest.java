package org.hisrc.zugradarscraper.trainroute.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.model.Stops;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.model.TrainRoute;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteEdgeFeatureCollection;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteSection;
import org.hisrc.zugradarscraper.trainroute.service.TrainRouteRetriever;
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

		final TrainRoute trainRoute = sut.retrieve(new TrainId("ICE", "654", "84/121947/18/19/80", LocalDate.of(2017, 5, 6)));
		final Set<Stop> stopsSet = new LinkedHashSet<>();
		for (TrainRouteSection section : trainRoute.getSections())
		{
			stopsSet.add(section.getDeparture());
			stopsSet.add(section.getArrival());
		}
		final Stops stops = new Stops(new ArrayList<>(stopsSet));
		mapper.writerFor(Stops.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("stops.js")), stops);
		
		final TrainRouteMultiLineStringFeature feature = new TrainRouteMultiLineStringFeature(trainRoute);
		Assert.assertEquals(9, trainRoute.getSections().size());
		final TrainRouteMultiLineStringFeatureCollection features = new TrainRouteMultiLineStringFeatureCollection(
				Arrays.asList(feature));
		mapper.writerFor(TrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("lines.js")), features);
		
		final TrainRouteEdgeFeatureCollection edges = new TrainRouteEdgeFeatureCollection(features);
		mapper.writerFor(TrainRouteEdgeFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("edges.js")), edges);
		
	}
}
