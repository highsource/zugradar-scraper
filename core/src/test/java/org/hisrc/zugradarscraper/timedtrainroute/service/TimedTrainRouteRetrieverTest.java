package org.hisrc.zugradarscraper.timedtrainroute.service;

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
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRoute;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiPointFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiPointFeatureCollection;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteSection;
import org.hisrc.zugradarscraper.timedtrainroute.service.TimedTrainRouteRetriever;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TimedTrainRouteRetrieverTest {

	private final TimedTrainRouteRetriever sut = new TimedTrainRouteRetriever();

	@Test
	public void retrievesTrainRoute() throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		final TimedTrainRoute trainRoute = sut.retrieve(new TrainId("ICE", "654", "84/121947/18/19/80", LocalDate.of(2017, 5, 6)));
		final Set<Stop> stopsSet = new LinkedHashSet<>();
		for (TimedTrainRouteSection section : trainRoute.getSections())
		{
			stopsSet.add(section.getDeparture().getStop());
			stopsSet.add(section.getArrival().getStop());
		}
		final Stops stops = new Stops(new ArrayList<>(stopsSet));
		mapper.writerFor(Stops.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("stops.js")), stops);
		
		final TimedTrainRouteMultiLineStringFeature feature = new TimedTrainRouteMultiLineStringFeature(trainRoute);
		Assert.assertEquals(7, trainRoute.getSections().size());
		final TimedTrainRouteMultiLineStringFeatureCollection featureCollection = new TimedTrainRouteMultiLineStringFeatureCollection(
				Arrays.asList(feature));
		mapper.writerFor(TimedTrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("lines.js")), featureCollection);
		
		final TimedTrainRouteMultiPointFeature multiPointFeature = trainRoute.interpolateTrainRouteMultiPointFeature(10000);
		final TimedTrainRouteMultiPointFeatureCollection multiPointFeatureCollection = new TimedTrainRouteMultiPointFeatureCollection(
				Arrays.asList(multiPointFeature));
		mapper.writerFor(TimedTrainRouteMultiPointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("tracks.js")), multiPointFeatureCollection);
		
	}
	
	@Test
	public void retrievesTrainRoute1018() throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		final TimedTrainRoute trainRoute = sut.retrieve(new TrainId("ICE", "1018", "84/114334/18/19/80", LocalDate.of(2017, 5, 1)));
		final Set<Stop> stopsSet = new LinkedHashSet<>();
		for (TimedTrainRouteSection section : trainRoute.getSections())
		{
			stopsSet.add(section.getDeparture().getStop());
			stopsSet.add(section.getArrival().getStop());
		}
		final Stops stops = new Stops(new ArrayList<>(stopsSet));
		mapper.writerFor(Stops.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("stops.js")), stops);
		
		final TimedTrainRouteMultiLineStringFeature feature = new TimedTrainRouteMultiLineStringFeature(trainRoute);
		Assert.assertEquals(24, trainRoute.getSections().size());
		final TimedTrainRouteMultiLineStringFeatureCollection featureCollection = new TimedTrainRouteMultiLineStringFeatureCollection(
				Arrays.asList(feature));
		mapper.writerFor(TimedTrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("lines.js")), featureCollection);
		
		final TimedTrainRouteMultiPointFeature multiPointFeature = trainRoute.interpolateTrainRouteMultiPointFeature(10000);
		final TimedTrainRouteMultiPointFeatureCollection multiPointFeatureCollection = new TimedTrainRouteMultiPointFeatureCollection(
				Arrays.asList(multiPointFeature));
		mapper.writerFor(TimedTrainRouteMultiPointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("tracks.js")), multiPointFeatureCollection);
		
	}
	
}
