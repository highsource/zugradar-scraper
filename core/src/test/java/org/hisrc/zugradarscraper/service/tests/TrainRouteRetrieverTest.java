package org.hisrc.zugradarscraper.service.tests;

import java.io.IOException;

import org.hisrc.zugradarscraper.model.TrainRoute;
import org.hisrc.zugradarscraper.service.TrainRouteRetriever;
import org.junit.Test;

public class TrainRouteRetrieverTest {
	
	private final TrainRouteRetriever sut = new TrainRouteRetriever();

	@Test
	public void retrievesTrainRoute() throws IOException
	{
		final TrainRoute trainRoute = sut.retrieve("84/110009/18/19/80");
		System.out.println(trainRoute);
		
	}
}
