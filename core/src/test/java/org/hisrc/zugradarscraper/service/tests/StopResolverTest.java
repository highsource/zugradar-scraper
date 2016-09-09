package org.hisrc.zugradarscraper.service.tests;

import java.math.BigDecimal;

import org.hisrc.zugradarscraper.model.LonLat;
import org.hisrc.zugradarscraper.model.Stop;
import org.hisrc.zugradarscraper.service.StopResolver;
import org.junit.Assert;
import org.junit.Test;

public class StopResolverTest {

	private StopResolver sut = new StopResolver();

	@Test
	public void resolvesStops() {
		final Stop stop = sut.resolveStop("Walldorf(Hess)");
		Assert.assertEquals(stop, new Stop("Walldorf(Hess)", "8006175", "FWF",
				new LonLat(BigDecimal.valueOf(8580811, 6), BigDecimal.valueOf(50001339, 6))));
	}

}
