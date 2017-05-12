package org.hisrc.zugradarscraper.train.model;

import java.time.LocalDate;

import org.hisrc.zugradarscraper.train.model.TrainId;
import org.junit.Assert;
import org.junit.Test;


public class TrainIdTest {
	
	@Test
	public void parsesTrainIds()
	{
		Assert.assertEquals(new TrainId("ICE", "1020", "test", LocalDate.of(2017, 5, 7)), TrainId.parse("ICE 1020", "test", "07.05.17"));
		Assert.assertEquals(new TrainId("CNL", "1020", "test", LocalDate.of(2017, 1, 8)), TrainId.parse("CNL1020", "test", "08.01.17"));
	}

}
