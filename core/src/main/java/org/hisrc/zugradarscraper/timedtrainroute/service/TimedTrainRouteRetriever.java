package org.hisrc.zugradarscraper.timedtrainroute.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.geometry.model.LonLat;
import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.model.StopAtTime;
import org.hisrc.zugradarscraper.stop.service.StopResolver;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRoute;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteSection;
import org.hisrc.zugradarscraper.timetable.service.GtfsService;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.onebusaway.gtfs.model.StopTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimedTrainRouteRetriever {

	// 84/110009/18/19/80

	private final Logger LOGGER = LoggerFactory.getLogger(TimedTrainRouteRetriever.class);

	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&look_trainid={0}&tpl=chain2json3&performLocating=16&format_xy_n&";

	private StopResolver stopResolver = new StopResolver();
	private GtfsService gtfsService = new GtfsService();

	public TimedTrainRoute retrieve(TrainId trainId) throws IOException {
		final String url = MessageFormat.format(URL_PATTERN, trainId.getId());
		LOGGER.trace("Requesting {}.", url);

		final String routeName = trainId.getClassification() + " " + trainId.getNumber();

		final LocalDate date = trainId.getStartDate();
		final List<StopTime> stopTimes = gtfsService.findStopTimesByRouteName(routeName, date);
		if (stopTimes == null) {
			LOGGER.error("Could not find stip times for {}.", routeName);
			return null;
		}

		final Map<String, StopTime> stopTimesByEvaNr = new HashMap<>();
		{
			final Map<String, AtomicInteger> stopTimeCountByEvaNr = new HashMap<>();

			for (StopTime stopTime : stopTimes) {
				final String evaNr = stopTime.getStop().getId().getId();
				final int count = stopTimeCountByEvaNr.computeIfAbsent(evaNr, key -> new AtomicInteger(0))
						.incrementAndGet();
				stopTimesByEvaNr.put(evaNr + "-" + count, stopTime);
			}
		}

		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(new InputStreamReader(is, "iso8859-1")).readArray();
			final JsonArray coordinatesArray = results.getJsonArray(0);
			final JsonArray stopsArray = results.getJsonArray(1);
			final LonLat[] coordinates = new LonLat[coordinatesArray.size()];
			for (int index = 0; index < coordinatesArray.size(); index++) {
				final JsonArray coordinateArray = coordinatesArray.getJsonArray(index);
				final int lon000000 = coordinateArray.getInt(0);
				final int lat000000 = coordinateArray.getInt(1);
				final double lon = lon000000 / 1000000.0;
				final double lat = lat000000 / 1000000.0;
				final LonLat lonLat = new LonLat(lon, lat);
				coordinates[index] = lonLat;
			}
			final List<TimedTrainRouteSection> sections = new ArrayList<>(stopsArray.size() - 1);
			Stop lastStop = null;
			StopTime lastStopTime = null;
			int lastStopIndex = -1;
			final Map<String, AtomicInteger> stopTimeCountByEvaNr = new HashMap<>();
			for (int index = 0; index < stopsArray.size(); index++) {
				final JsonArray stopArray = stopsArray.getJsonArray(index);
				final int stopIndex = stopArray.getInt(0);
				final String stopName = stopArray.getString(1);
				final Stop stop = stopResolver.resolveStop(stopName);
				if (stop == null) {
					LOGGER.warn("Stop {} could not be found.", stopName);
					continue;
				}
				String evaNr = stop.getEvaNr();
				final int count = stopTimeCountByEvaNr.computeIfAbsent(evaNr, key -> new AtomicInteger(0))
						.incrementAndGet();
				
				final StopTime stopTime = stopTimesByEvaNr.get(evaNr + "-" + count);
				if (stopTime == null) {
					LOGGER.warn("Stop time for stop {} could not be found.", stopName);
				}
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
						final TimedTrainRouteSection section = new TimedTrainRouteSection(lastStopAtTime, stopAtTime,
								new LineString(sectionCoordinates));
						sections.add(section);
					}
					lastStop = stop;
					lastStopIndex = stopIndex;
					lastStopTime = stopTime;
				}
			}
			return new TimedTrainRoute(trainId, sections);
		} catch (JsonException jsonex) {
			throw new IOException(jsonex);
		}
	}
}
