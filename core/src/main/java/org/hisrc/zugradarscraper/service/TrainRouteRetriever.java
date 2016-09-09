package org.hisrc.zugradarscraper.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.hisrc.zugradarscraper.model.LonLat;
import org.hisrc.zugradarscraper.model.Stop;
import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.model.TrainRouteSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainRouteRetriever {

	// 84/110009/18/19/80

	private final Logger LOGGER = LoggerFactory.getLogger(TrainRouteRetriever.class);

	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&look_trainid={0}&tpl=chain2json3&performLocating=16&format_xy_n&";

	private StopResolver stopResolver = new StopResolver();

	public TrainRoute retrieve(String id) throws IOException {
		final String url = MessageFormat.format(URL_PATTERN, id);
		LOGGER.trace("Requesting {}.", url);
		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(new InputStreamReader(is, "iso8859-1")).readArray();
			final JsonArray coordinatesArray = results.getJsonArray(0);
			final JsonArray stopsArray = results.getJsonArray(1);
			final LonLat[] coordinates = new LonLat[coordinatesArray.size()];
			for (int index = 0; index < coordinatesArray.size() - 1; index++) {
				final JsonArray coordinateArray = coordinatesArray.getJsonArray(index);
				final int lon000000 = coordinateArray.getInt(0);
				final int lat000000 = coordinateArray.getInt(1);
				final BigDecimal lon = BigDecimal.valueOf(lon000000, 6);
				final BigDecimal lat = BigDecimal.valueOf(lat000000, 6);
				final LonLat lonLat = new LonLat(lon, lat);
				coordinates[index] = lonLat;
			}
			final Stop[] stops = new Stop[stopsArray.size()];
			final List<TrainRouteSection> sections = new ArrayList<>(stopsArray.size() - 1);
			for (int index = 0; index < stopsArray.size() - 1; index++) {
				final JsonArray stopArray = stopsArray.getJsonArray(index);
				final int stopIndex = stopArray.getInt(0);
				final String stopName = stopArray.getString(1);
				final Stop stop = stopResolver.resolveStop(stopName);
				LOGGER.info("Stop [{}]: {}, {}", index, stop);
				stops[index] = stop;
				if (index > 0) {
					final int lastStopIndex = stopsArray.getJsonArray(index - 1).getInt(0);
					final Stop lastStop = stops[index - 1];
					final List<LonLat> sectionCoordinates = new ArrayList<>(stopIndex - lastStopIndex + 1);
					for (int coordinatesIndex = lastStopIndex; coordinatesIndex <= stopIndex; coordinatesIndex++) {
						sectionCoordinates.add(coordinates[coordinatesIndex]);
					}
					final TrainRouteSection section = new TrainRouteSection(lastStop, stop, sectionCoordinates);
					sections.add(section);
				}
			}
			return new TrainRoute(id, sections);
		} catch (JsonException jsonex) {
			throw new IOException(jsonex);
		}
	}

}
