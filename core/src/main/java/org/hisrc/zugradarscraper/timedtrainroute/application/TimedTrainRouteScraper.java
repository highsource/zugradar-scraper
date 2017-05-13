package org.hisrc.zugradarscraper.timedtrainroute.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRoute;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.timedtrainroute.service.TimedTrainRouteRetriever;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TimedTrainRouteScraper {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;
		@Option(name = "-i", aliases = {
				"--input" }, metaVar = "INPUT", usage = "Input CSV file with train ids, trainIds.csv by default")
		private File trainIdsFile = new File("trainIds.csv");
		@Argument(metaVar = "OUTPUT", required = false, index = 0, usage = "Output GeoJSON file with train routes, trainRoutes.geojson by default")
		private File timedTrainRoutesFile = new File("timedTrainRoutes.geojson");

		public boolean isHelp() {
			return help;
		}

		public File getTrainIdsFile() {
			return trainIdsFile;
		}

		public File getTimedTrainRoutesFile() {
			return timedTrainRoutesFile;
		}
	}

	private final Logger LOGGER = LoggerFactory.getLogger(TimedTrainRouteScraper.class);

	public static void main(String[] args) throws IOException {
		final Configuration configuration = new Configuration();
		CmdLineParser parser = new CmdLineParser(configuration);
		try {
			parser.parseArgument(args);
			if (configuration.isHelp()) {
				printUsage(parser);
			} else {
				new TimedTrainRouteScraper().scrapeTrainRoutes(configuration.getTrainIdsFile(),
						configuration.getTimedTrainRoutesFile());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println("Usage: java -cp zugradarscraper.jar " + TimedTrainRouteScraper.class.getName()
				+ "[options...] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}

	private final TimedTrainRouteRetriever trainRouteRetriever = new TimedTrainRouteRetriever();

	public void scrapeTrainRoutes(File trainIdsFile, File timedTrainRoutesFile) {

		final List<TrainId> trainIds = new LinkedList<>();
		final CsvMapper mapper = new CsvMapper();
		mapper.registerModule(new JavaTimeModule());
		final CsvSchema schema = mapper.schemaFor(TrainId.class).withHeader();

		LOGGER.debug("Reading known train ids from [{}].", trainIdsFile);
		try {
			final MappingIterator<TrainId> trainIdIterator = mapper.readerFor(TrainId.class).with(schema)
					.readValues(trainIdsFile);
			while (trainIdIterator.hasNext()) {
				try {
					trainIds.add(trainIdIterator.next());
				} catch (RuntimeException rex) {
					LOGGER.warn("Could not read train id from [{}].", trainIdIterator.getCurrentLocation(), rex);
				}
			}
		} catch (IOException ioex) {
			LOGGER.warn("Could not read train ids from [{}].", trainIdsFile, ioex);
		}

		try (OutputStream outputStream = new FileOutputStream(timedTrainRoutesFile);) {
			scrapeTimedTrainRoutes(trainIds, outputStream);
		} catch (IOException ioex) {
			LOGGER.error("Could not export timed train routes.", ioex);
		}
	}

	public void scrapeTimedTrainRoutes(final List<TrainId> trainIds, OutputStream trainRoutesOutputStream) {
		final List<TimedTrainRouteMultiLineStringFeature> trainRoutes = new ArrayList<>(trainIds.size());

		for (TrainId trainId : trainIds) {
			try {
				final TimedTrainRoute trainRoute = trainRouteRetriever.retrieve(trainId);
				if (trainRoute != null) {
					final TimedTrainRouteMultiLineStringFeature feature = new TimedTrainRouteMultiLineStringFeature(
							trainRoute);
					trainRoutes.add(feature);
				}
			} catch (IOException ioex) {
				LOGGER.error("Could not retrieve timed train route for  {}.", trainId, ioex);
			}
		}

		final TimedTrainRouteMultiLineStringFeatureCollection trainRouteCollection = new TimedTrainRouteMultiLineStringFeatureCollection(
				trainRoutes);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		try {
			objectMapper.writerFor(TimedTrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter()
					.writeValue(trainRoutesOutputStream, trainRouteCollection);
		} catch (IOException ioex) {
			LOGGER.error("Could not export timed train routes.", ioex);
		}
	}
}
