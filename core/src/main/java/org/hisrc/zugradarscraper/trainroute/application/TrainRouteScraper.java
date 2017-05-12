package org.hisrc.zugradarscraper.trainroute.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.model.TrainRoute;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteEdgeFeatureCollection;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.trainroute.service.TrainRouteRetriever;
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

public class TrainRouteScraper {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;
		@Option(name = "-i", aliases = {
				"--input" }, metaVar = "INPUT", usage = "Input CSV file with train ids, trainIds.csv by default")
		private File trainIdsFile = new File("trainIds.csv");
		@Option(name = "-d", aliases = {
				"--edges" }, metaVar = "EDGES", usage = "Outut GeoJSON file with edges, edges.geojson by default")
		private File trainRouteEdgesFile = new File("trainRouteEdges.geojson");
		@Argument(metaVar = "OUTPUT", required = false, index = 0, usage = "Output GeoJSON file with train routes, trainRoutes.geojson by default")
		private File trainRoutesFile = new File("trainRoutes.geojson");

		public boolean isHelp() {
			return help;
		}

		public File getTrainIdsFile() {
			return trainIdsFile;
		}

		public File getTrainRoutesFile() {
			return trainRoutesFile;
		}
		
		public File getTrainRouteEdgesFile() {
			return trainRouteEdgesFile;
		}
	}

	private final Logger LOGGER = LoggerFactory.getLogger(TrainRouteScraper.class);

	public static void main(String[] args) throws IOException {
		final Configuration configuration = new Configuration();
		CmdLineParser parser = new CmdLineParser(configuration);
		try {
			parser.parseArgument(args);
			if (configuration.isHelp()) {
				printUsage(parser);
			} else {
				new TrainRouteScraper().scrapeTrainRoutes(configuration.getTrainIdsFile(),
						configuration.getTrainRoutesFile(),
						configuration.getTrainRouteEdgesFile());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println(
				"Usage: java -cp zugradarscraper.jar " + TrainRouteScraper.class.getName() + "[options...] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}

	private final TrainRouteRetriever trainRouteRetriever = new TrainRouteRetriever();

	public void scrapeTrainRoutes(File trainIdsFile, File trainRoutesFile, File trainRouteEdgesFile) {

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

		try (OutputStream outputStream = new FileOutputStream(trainRoutesFile);
				OutputStream edgesStream = new FileOutputStream(trainRouteEdgesFile);) {
			scrapeTrainRoutes(trainIds, outputStream, edgesStream);
		} catch (IOException ioex) {
			LOGGER.error("Could not export train routes.", ioex);
		}
	}

	public void scrapeTrainRoutes(final List<TrainId> trainIds,
			OutputStream trainRoutesOutputStream,
			OutputStream edgesOutputStream) {
		final List<TrainRouteMultiLineStringFeature> trainRoutes = new ArrayList<>(trainIds.size());

		for (TrainId trainId : trainIds) {
			try {
				final TrainRoute trainRoute = trainRouteRetriever.retrieve(trainId);
				final TrainRouteMultiLineStringFeature feature = new TrainRouteMultiLineStringFeature(trainRoute);
				trainRoutes.add(feature);
			} catch (IOException ioex) {
				LOGGER.error("Could not retrieve train route for  {}.", trainId, ioex);
			}
		}

		final TrainRouteMultiLineStringFeatureCollection trainRouteCollection = new TrainRouteMultiLineStringFeatureCollection(
				trainRoutes);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		try {
			objectMapper.writerFor(TrainRouteMultiLineStringFeatureCollection.class).withDefaultPrettyPrinter()
					.writeValue(trainRoutesOutputStream, trainRouteCollection);
		} catch (IOException ioex) {
			LOGGER.error("Could not export train routes.", ioex);
		}
		
		final TrainRouteEdgeFeatureCollection edgeCollection = new TrainRouteEdgeFeatureCollection(trainRouteCollection);
		try {
			objectMapper.writerFor(TrainRouteEdgeFeatureCollection.class).withDefaultPrettyPrinter()
					.writeValue(edgesOutputStream, edgeCollection);
		} catch (IOException ioex) {
			LOGGER.error("Could not export train route edges.", ioex);
		}
		
	}
}
