package org.hisrc.zugradarscraper.application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.hisrc.zugradarscraper.args4j.spi.LocalDateOptionHandler;
import org.hisrc.zugradarscraper.model.TrainId;
import org.hisrc.zugradarscraper.service.TrainIdRetriever;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class TrainIdScraper {

	public static class Configuration {

		@Option(name = "-?", aliases = { "-help", "--help" }, help = true, usage = "Prints this help message")
		private boolean help;
		@Option(name = "-s", aliases = {
				"--start-date" }, metaVar = "START_DATE", usage = "Start date (yyyy-MM-dd) for scraping, inclusive, current date by default", handler = LocalDateOptionHandler.class)
		private LocalDate from = LocalDate.now();
		@Option(name = "-e", aliases = {
				"--end-date" }, metaVar = "END_DATE", usage = "End date for scraping (yyyy-MM-dd), exclusive, next day after start date by default", handler = LocalDateOptionHandler.class)
		private LocalDate to = null;
		@Option(name = "-i", aliases = {
				"--input" }, metaVar = "INPUT", usage = "Input CSV file, same as output file by default")
		private File input = null;
		@Argument(metaVar = "OUTPUT", required = false, index = 0, usage = "Output CSV file, trainIds.csv by default")
		private File output = new File("trainIds.csv");

		public boolean isHelp() {
			return help;
		}

		public LocalDate getFrom() {
			return from;
		}

		public LocalDate getTo() {
			return to != null ? to : getFrom().plusDays(1);
		}

		public File getInput() {
			return input != null ? input : getOutput();
		}

		public File getOutput() {
			return output;
		}

	}

	private final Logger LOGGER = LoggerFactory.getLogger(TrainIdScraper.class);

	public static void main(String[] args) throws IOException {
		final Configuration configuration = new Configuration();
		CmdLineParser parser = new CmdLineParser(configuration);
		try {
			parser.parseArgument(args);
			if (configuration.isHelp()) {
				printUsage(parser);
			} else {
				new TrainIdScraper().scrapeTrainIds(configuration.getInput(), configuration.getOutput(),
						configuration.getFrom(), configuration.getTo());
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			printUsage(parser);
		}
	}

	private static void printUsage(CmdLineParser parser) {
		System.out.println(
				"Usage: java -cp zugradarscraper.jar " + TrainIdScraper.class.getName() + "[options...] [OUTPUT]");
		parser.printUsage(System.out);
		System.out.println();
	}

	private final TrainIdRetriever trainIdRetriever = new TrainIdRetriever();

	public void scrapeTrainIds() {
		scrapeTrainIds(new File("trainIds.csv"));
	}

	public void scrapeTrainIds(File file) {
		scrapeTrainIds(file, LocalDate.now(), LocalDate.now().plusDays(1));
	}

	public void scrapeTrainIds(LocalDate from, LocalDate to) {
		scrapeTrainIds(new File("trainIds.csv"), from, to);
	}

	public void scrapeTrainIds(File file, LocalDate from, LocalDate to) {
		scrapeTrainIds(file, file, from, to);
	}

	public void scrapeTrainIds(File input, File output, LocalDate from, LocalDate to) {

		final List<TrainId> existingTrainIds = new LinkedList<>();
		final CsvMapper mapper = new CsvMapper();
		final CsvSchema schema = mapper.schemaFor(TrainId.class).withHeader();

		if (input.exists()) {
			LOGGER.debug("Reading known train ids from [{}].", input);
			try {
				final MappingIterator<TrainId> trainIds = mapper.readerFor(TrainId.class).with(schema)
						.readValues(input);
				while (trainIds.hasNext()) {
					try {
						existingTrainIds.add(trainIds.next());
					} catch (RuntimeException rex) {
						LOGGER.warn("Could not read train id from [{}].", trainIds.getCurrentLocation(), rex);
					}
				}
			} catch (IOException ioex) {
				LOGGER.warn("Could not read train ids from [{}].", input, ioex);
			}
		} else {
			LOGGER.debug("Skipping reading known train ids from [{}] as it does not exist.", input);
		}

		final Set<TrainId> knownTrainIds = new HashSet<>();

		final SequenceWriter trainIdWriter;
		try {
			trainIdWriter = mapper.writerFor(TrainId.class).with(schema).writeValues(output);
			final Consumer<TrainId> trainIdConsumer = trainId -> {
				if (knownTrainIds.add(trainId)) {
					try {
						trainIdWriter.write(trainId);
					} catch (IOException ioex) {
						LOGGER.error("Could not write train id to [{}].", output, ioex);
					}
				}
			};
			existingTrainIds.forEach(trainIdConsumer);
			trainIdRetriever.retrieveTrainIds(from, to, trainIdConsumer);
		} catch (IOException ioex) {
			LOGGER.error("Could not write train ids to [{}].", output, ioex);
		}
	}
}
