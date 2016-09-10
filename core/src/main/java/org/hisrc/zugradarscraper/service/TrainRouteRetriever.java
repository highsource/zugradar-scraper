package org.hisrc.zugradarscraper.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.hisrc.zugradarscraper.model.LineString;
import org.hisrc.zugradarscraper.model.LonLat;
import org.hisrc.zugradarscraper.model.Stop;
import org.hisrc.zugradarscraper.model.StopAtTime;
import org.hisrc.zugradarscraper.model.TrainId;
import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.model.TrainRouteSection;
import org.onebusaway.gtfs.model.StopTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainRouteRetriever {

	// 84/110009/18/19/80

	private final Logger LOGGER = LoggerFactory.getLogger(TrainRouteRetriever.class);

	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&look_trainid={0}&tpl=chain2json3&performLocating=16&format_xy_n&";

	private StopResolver stopResolver = new StopResolver();
	private GtfsService gtfsService = new GtfsService();

	public TrainRoute retrieve(TrainId trainId, LocalDate date) throws IOException {
		final String url = MessageFormat.format(URL_PATTERN, trainId.getId());
		LOGGER.trace("Requesting {}.", url);

		final String routeName = trainId.getClassification() + " " + trainId.getNumber();

		final List<StopTime> stopTimes = gtfsService.findStopTimesByRouteName(routeName, date);

		final Map<String, StopTime> stopTimesByEvaNr = stopTimes.stream()
				.collect(Collectors.toMap(stopTime -> stopTime.getStop().getId().getId(), stopTime -> stopTime));

		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(new InputStreamReader(is, "iso8859-1")).readArray();
			final JsonArray coordinatesArray = results.getJsonArray(0);
			final JsonArray stopsArray = results.getJsonArray(1);
			final LonLat[] coordinates = new LonLat[coordinatesArray.size()];
			for (int index = 0; index < coordinatesArray.size(); index++) {
				final JsonArray coordinateArray = coordinatesArray.getJsonArray(index);
				final int lon000000 = coordinateArray.getInt(0);
				final int lat000000 = coordinateArray.getInt(1);
				final double lon = lon000000/1000000;
				final double lat = lat000000/1000000;
				final LonLat lonLat = new LonLat(lon, lat);
				coordinates[index] = lonLat;
			}
			final List<TrainRouteSection> sections = new ArrayList<>(stopsArray.size() - 1);
			Stop lastStop = null;
			StopTime lastStopTime = null;
			int lastStopIndex = -1;
			for (int index = 0; index < stopsArray.size(); index++) {
				final JsonArray stopArray = stopsArray.getJsonArray(index);
				final int stopIndex = stopArray.getInt(0);
				final String stopName = stopArray.getString(1);
				final Stop stop = stopResolver.resolveStop(stopName);
				final StopTime stopTime = stopTimesByEvaNr.get(stop.getEvaNr());
				if (stopTime != null) {
					final LocalDateTime dateTime = date.atStartOfDay().plusSeconds(stopTime.getArrivalTime());
					final StopAtTime stopAtTime = new StopAtTime(stop, dateTime);
					LOGGER.info("{}@{}", stop, dateTime);
					if (lastStop != null) {
						final LocalDateTime lastDateTime = date.atStartOfDay()
								.plusSeconds(lastStopTime.getDepartureTime());
						final StopAtTime lastStopAtTime = new StopAtTime(lastStop, lastDateTime);

						final double[][] sectionCoordinates = new double[stopIndex - lastStopIndex + 1][];
						for (int coordinatesIndex = lastStopIndex; coordinatesIndex <= stopIndex; coordinatesIndex++) {
							sectionCoordinates[coordinatesIndex - lastStopIndex] = coordinates[coordinatesIndex]
									.getCoordinates();
						}
						final TrainRouteSection section = new TrainRouteSection(lastStopAtTime, stopAtTime,
								new LineString(sectionCoordinates));
						sections.add(section);
					}
					lastStop = stop;
					lastStopIndex = stopIndex;
					lastStopTime = stopTime;
				}
			}
			return new TrainRoute(trainId, sections);
		} catch (JsonException jsonex) {
			throw new IOException(jsonex);
		}
	}
}
