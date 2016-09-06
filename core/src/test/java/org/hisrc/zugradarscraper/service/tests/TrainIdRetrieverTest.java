package org.hisrc.zugradarscraper.service.tests;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hisrc.zugradarscraper.model.TrainId;
import org.hisrc.zugradarscraper.service.TrainIdRetriever;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TrainIdRetrieverTest {

	private final TrainIdRetriever trainIdService = new TrainIdRetriever();
	
	@Test
	public void retrievesTrainIdForDateTime() throws IOException
	{
		final Set<TrainId> trainIds = new HashSet<>();
		trainIdService.retrieveTrainIds(LocalDate.now().atTime(12, 00), trainIds::add);
		Assert.assertFalse(trainIds.isEmpty());
	}

	@Ignore
	@Test
	public void retrievesTrainIdForDates()
	{
		final Set<TrainId> trainIds = new HashSet<>();
		trainIdService.retrieveTrainIds(LocalDate.now(), LocalDate.now().plusDays(1), trainIds::add);
		Assert.assertFalse(trainIds.isEmpty());
	}
}
