package org.hisrc.zugradarscraper.train.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;

import org.hisrc.zugradarscraper.train.model.TrainId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainIdRetriever {

	private final Logger LOGGER = LoggerFactory.getLogger(TrainIdRetriever.class);

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static final DateTimeFormatter REQUEST_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
	public static final DateTimeFormatter REQUEST_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	public static final String URL_PATTERN = "http://www.apps-bahn.de/bin/livemap/query-livemap.exe/dny?L=vs_livefahrplan&performLocating=1&performFixedLocating=1&look_requesttime={1}&look_requestdate={2}&livemapRequest=yes&ts={0}";

	public void retrieveTrainIds(LocalDate from, LocalDate to, Consumer<TrainId> trainIdConsumer) {
		final long daysBetween = ChronoUnit.DAYS.between(from, to);
		for (long day = 0; day < daysBetween; day++) {
			final LocalDate date = from.plusDays(day);
			for (int hour = 0; hour < 24; hour++) {
				LOGGER.debug("Processing date {} hour {} ({}%).", date, hour,
						Math.round((100d * (day * 24 + hour)) / (daysBetween * 24)));
				for (int minute = 0; minute < 60; minute++) {
					final LocalDateTime dateTime = date.atTime(hour, minute);
					retrieveTrainIds(dateTime, trainIdConsumer);
				}
			}
		}
	}

	public void retrieveTrainIds(LocalDateTime dateTime, Consumer<TrainId> trainIdConsumer) {
		final LocalDate date = dateTime.toLocalDate();
		final String url = MessageFormat.format(URL_PATTERN, DATE_FORMATTER.format(date),
				REQUEST_TIME_FORMATTER.format(dateTime),
				REQUEST_DATE_FORMATTER.format(dateTime));
		LOGGER.trace("Requesting {}.", url);
		try (InputStream is = new URL(url).openStream()) {
			final JsonArray results = Json.createReader(is).readArray();
			final JsonArray trainsArray = results.getJsonArray(0);
			for (int index = 0; index < trainsArray.size() - 1; index++) {
				final JsonArray trainArray = trainsArray.getJsonArray(index);
				final String classificationAndNumber = trainArray.getString(0);
				final String id = trainArray.getString(3);
				final String startDateString = trainArray.getString(13);
				try {
					final TrainId trainId = TrainId.parse(classificationAndNumber, id, startDateString);
					trainIdConsumer.accept(trainId);
				} catch (IllegalArgumentException iaex) {
					LOGGER.warn("Could not parse {}.", classificationAndNumber, iaex);
				}
			}
		} catch (JsonException | IOException ex) {
			LOGGER.warn("Error requesting train numbers at [{}].", dateTime, ex);
		}
	}
}
