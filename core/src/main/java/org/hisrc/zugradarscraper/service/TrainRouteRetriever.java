package org.hisrc.zugradarscraper.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.MessageFormat;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.hisrc.zugradarscraper.model.LonLat;
import org.hisrc.zugradarscraper.model.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainRouteRetriever {

//	84/110009/18/19/80
	
	private final Logger LOGGER = LoggerFactory.getLogger(TrainRouteRetriever.class);
	
	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&look_trainid={0}&tpl=chain2json3&performLocating=16&format_xy_n&";

	private StopResolver stopResolver = new StopResolver();
	
	public void retrieve(String id)
	{
		final String url = MessageFormat.format(URL_PATTERN, id);
		LOGGER.trace("Requesting {}.", url);
		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(is).readArray();
			final JsonArray coordinatesArray = results.getJsonArray(0);
			final JsonArray stopsArray = results.getJsonArray(1);
			final LonLat[] coordinates = new LonLat[coordinatesArray.size()];
			for (int index = 0; index < coordinatesArray.size() - 1; index++) {
				final JsonArray coordinateArray = coordinatesArray.getJsonArray(index);
				final int lon000000 = coordinateArray.getInt(0);
				final int lat000000 = coordinateArray.getInt(1);
				final BigDecimal lon = BigDecimal.valueOf(lon000000,6);
				final BigDecimal lat = BigDecimal.valueOf(lat000000,6);
				final LonLat lonLat = new LonLat(lon, lat);
				coordinates[index] = lonLat;
			}
			final Stop[] stops = new Stop[stopsArray.size()];
			for (int index = 0; index < stopsArray.size() - 1; index++) {
				final JsonArray stopArray = stopsArray.getJsonArray(index);
				final int stopIndex = stopArray.getInt(0);
				final String stopName = stopArray.getString(1);
				final LonLat stopCoordinates = coordinates[stopIndex];
				final Stop stop = stopResolver.resolveStop(stopName);
				LOGGER.info("Stop [{}]: {}, {}", index, stop);
			}
			
		} catch (JsonException | IOException ex) {
			LOGGER.warn("Error requesting train route [{}].", id, ex);
		}
	}
	
}
