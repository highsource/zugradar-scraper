package org.hisrc.zugradarscraper.stop.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hisrc.zugradarscraper.geometry.model.LonLat;
import org.hisrc.zugradarscraper.stop.model.Haltestelle;
import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.train.service.TrainIdRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class StopResolver {

	private final Logger LOGGER = LoggerFactory.getLogger(TrainIdRetriever.class);

	private final String DEFAULT_RESOURCE_NAME = "D_Bahnhof_2016_01_alle.csv";

	private final List<Haltestelle> haltestelles;
	private final Map<String, Haltestelle> haltestelleByName;

	public StopResolver() {

		try (InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCE_NAME)) {
			final List<Haltestelle> haltestelles = loadHaltestelles(is);
			this.haltestelles = Collections.unmodifiableList(haltestelles);
			this.haltestelleByName = this.haltestelles.stream()
					.collect(Collectors.toMap(Haltestelle::getName, haltestelle -> haltestelle));
		} catch (IOException ioex) {
			throw new ExceptionInInitializerError(ioex);
		}
	}

	public Stop resolveStop(String stopName) {
		final Haltestelle haltestelle = this.haltestelleByName.get(stopName);
		if (haltestelle == null) {
			LOGGER.warn("Stop name [{}] could not be resolved.", stopName);
			return null;
		}
		return new Stop(haltestelle.getName(), haltestelle.getEvaNr(), haltestelle.getDs100(),
				new double[] { haltestelle.getLaenge(), haltestelle.getBreite() });
	}

	private List<Haltestelle> loadHaltestelles(InputStream is) throws IOException {
		final List<Haltestelle> haltestelles = new LinkedList<>();
		final CsvMapper mapper = new CsvMapper();
		final CsvSchema schema = mapper.schemaFor(Haltestelle.class).withHeader().withColumnSeparator(';');

		final MappingIterator<Haltestelle> haltestellesIterator = mapper.readerFor(Haltestelle.class).with(schema)
				.readValues(new InputStreamReader(is, "UTF-8"));
		while (haltestellesIterator.hasNext()) {
			try {
				haltestelles.add(haltestellesIterator.next());
			} catch (RuntimeException rex) {
				LOGGER.warn("Could not read haltestelle from [{}].", haltestellesIterator.getCurrentLocation(), rex);
			}
		}
		return haltestelles;
	}
}
