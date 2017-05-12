package org.hisrc.zugradarscraper.trainroute.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.geometry.model.LonLat;
import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.service.StopResolver;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.model.TrainRoute;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainRouteRetriever {

	private final Logger LOGGER = LoggerFactory.getLogger(TrainRouteRetriever.class);

	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&look_trainid={0}&tpl=chain2json3&performLocating=16&format_xy_n&";

	private StopResolver stopResolver = new StopResolver();

	public TrainRoute retrieve(TrainId trainId) throws IOException {
		final String url = MessageFormat.format(URL_PATTERN, trainId.getId());
		LOGGER.trace("Requesting {}.", url);

		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(new InputStreamReader(is, "iso8859-1")).readArray();
			final JsonArray coordinatesArray = results.getJsonArray(0);
			final JsonArray stopsArray = results.getJsonArray(1);
			final LonLat[] coordinates = new LonLat[coordinatesArray.size()];
			for (int index = 0; index < coordinatesArray.size(); index++) {
				final JsonArray coordinateArray = coordinatesArray.getJsonArray(index);
				final int lon000000 = coordinateArray.getInt(0);
				final int lat000000 = coordinateArray.getInt(1);
				final double lon = lon000000/1000000.0;
				final double lat = lat000000/1000000.0;
				final LonLat lonLat = new LonLat(lon, lat);
				coordinates[index] = lonLat;
			}
			final List<TrainRouteSection> sections = new ArrayList<>(stopsArray.size() - 1);
			Stop lastStop = null;
			int lastStopIndex = -1;
			for (int index = 0; index < stopsArray.size(); index++) {
				final JsonArray stopArray = stopsArray.getJsonArray(index);
				final int stopIndex = stopArray.getInt(0);
				final String stopName = stopArray.getString(1);
				final Stop stop = stopResolver.resolveStop(stopName);
				if (stop == null) {
					LOGGER.warn("Stop {} could not be found.", stopName);
				} else  {
					LOGGER.info("Stop {}", stop);
					if (lastStop != null) {
						final double[][] sectionCoordinates = new double[stopIndex - lastStopIndex + 1][];
						for (int coordinatesIndex = lastStopIndex; coordinatesIndex <= stopIndex; coordinatesIndex++) {
							sectionCoordinates[coordinatesIndex - lastStopIndex] = coordinates[coordinatesIndex]
									.getCoordinates();
						}
						final TrainRouteSection section = new TrainRouteSection(lastStop, stop,
								new LineString(sectionCoordinates));
						sections.add(section);
					}
					lastStop = stop;
					lastStopIndex = stopIndex;
				}
			}
			return new TrainRoute(trainId, sections);
		} catch (JsonException jsonex) {
			throw new IOException(jsonex);
		}
	}
}
