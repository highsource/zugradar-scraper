package org.hisrc.zugradarscraper.service.tests;

import java.util.List;

import org.hisrc.zugradarscraper.service.GtfsService;
import org.junit.Assert;
import org.junit.Test;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.StopTime;
import org.onebusaway.gtfs.model.Trip;

public class GtfsServiceTest {

	private GtfsService sut = new GtfsService();

	@Test
	public void findsRouteByName() {
		final Route route = sut.findRouteByName("ICE 596");
		Assert.assertNotNull(route);
	}
	
	@Test
	public void findsTripByNameAndCurrentDate() {
		final Trip trip = sut.findTripByNameAndCurrentDate("ICE 596");
		Assert.assertNotNull(trip);
	}
	
	@Test
	public void findsStopTimesByRouteNameAndCurrentDate() {
		final List<StopTime> stopTimes = sut.findStopTimesByRouteNameAndCurrentDate("ICE 596");
		Assert.assertEquals(16, stopTimes.size());
		Assert.assertEquals("8000261", stopTimes.get(0).getStop().getId().getId());
	}

}


