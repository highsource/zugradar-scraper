package org.hisrc.zugradarscraper.stop.service;

import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.service.StopResolver;
import org.junit.Assert;
import org.junit.Test;

public class StopResolverTest {

	private StopResolver sut = new StopResolver();

	@Test
	public void resolvesStops() {
		final Stop stop = sut.resolveStop("Walldorf(Hess)");
		Assert.assertEquals(stop, new Stop("Walldorf(Hess)", "8006175", "FWF", new double[] { 8.580811, 50.001339 }));
	}

}
