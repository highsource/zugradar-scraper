package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TimedTrainRouteMultiLineStringFeatureCollectionTest {

	@Test
	public void test() throws IOException {

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.readerFor(TimedTrainRouteMultiLineStringFeatureCollection.class)
				.readValue(getClass().getResourceAsStream("timedTrainRoutes-2017-05-13.geojson"));

	}
}
