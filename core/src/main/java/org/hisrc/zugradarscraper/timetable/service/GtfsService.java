package org.hisrc.zugradarscraper.timetable.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.onebusaway.csv_entities.CsvInputSource;
import org.onebusaway.csv_entities.ZipFileCsvInputSource;
import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.impl.calendar.CalendarServiceDataFactoryImpl;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;
import org.onebusaway.gtfs.model.calendar.ServiceDate;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs.services.GtfsRelationalDao;
import org.onebusaway.gtfs.services.calendar.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GtfsService {

	private final String DEFAULT_RESOURCE_NAME = "2017.zip";

	private final String DEFAULT_AGENCY = "DPN";

	private final GtfsRelationalDao dao;
	private final CalendarService calendarService;

	public GtfsService() {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCE_NAME)) {
			this.dao = loadDao(is);
			this.calendarService = CalendarServiceDataFactoryImpl.createService(this.dao);
		} catch (IOException ioex) {
			throw new ExceptionInInitializerError(ioex);
		}
	}

	public List<StopTime> findStopTimesByRouteName(String name, LocalDate date) {
		final Trip trip = findTripByRouteName(name, date);
		if (trip == null) {
			return null;
		}
		return this.dao.getStopTimesForTrip(trip);
	}

	public Trip findTripByRouteName(String name, LocalDate date) {
		final Route route = findRouteByName(name);
		if (route == null) {
			return null;
		}
		final ServiceDate serviceDate = createServiceDate(date);
		final Set<AgencyAndId> serviceIds = this.calendarService.getServiceIdsOnDate(serviceDate);
		final List<Trip> trips = this.dao.getTripsForRoute(route);
		return trips.stream().filter(trip -> serviceIds.contains(trip.getServiceId())).findFirst().orElse(null);
	}

	public Route findRouteByName(String name) {
		final Agency agency = this.dao.getAgencyForId(DEFAULT_AGENCY);
		final List<Route> routes = this.dao.getRoutesForAgency(agency);
		return routes.stream().filter(
				route -> Objects.equals(name, route.getLongName()) || Objects.equals(name, route.getShortName()))
				.findFirst().orElse(null);
	}

	private ServiceDate createServiceDate(LocalDate date) {
		final ServiceDate serviceDate = new ServiceDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		return serviceDate;
	}

	private GtfsRelationalDao loadDao(InputStream is) throws IOException {
		Objects.requireNonNull(is, "is must not be null.");
		final File file = File.createTempFile("gtfs", "zip");

		try (OutputStream os = new FileOutputStream(file)) {
			IOUtils.copy(is, os);
		}

		final CsvInputSource csvInputSource = new ZipFileCsvInputSource(new ZipFile(file));

		final GtfsMutableRelationalDao store = new GtfsRelationalDaoImpl();

		final GtfsReader reader = new GtfsReader();
		reader.setInputSource(csvInputSource);
		reader.setEntityStore(store);
		reader.setInternStrings(true);

		for (Class<?> entityClass : reader.getEntityClasses()) {
			reader.readEntities(entityClass);
			store.flush();
		}
		store.close();
		file.delete();
		return store;
	}
}
