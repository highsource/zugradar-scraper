package org.hisrc.zugradarscraper.service.tests;

import org.hisrc.zugradarscraper.service.TrainRouteRetriever;
import org.junit.Test;

public class TrainRouteRetrieverTest {
	
	private final TrainRouteRetriever sut = new TrainRouteRetriever();

	@Test
	public void retrievesTrainRoute()
	{
		sut.retrieve("84/110009/18/19/80");
	}
}
