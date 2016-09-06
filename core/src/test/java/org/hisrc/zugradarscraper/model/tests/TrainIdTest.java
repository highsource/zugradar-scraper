package org.hisrc.zugradarscraper.model.tests;

import org.hisrc.zugradarscraper.model.TrainId;
import org.junit.Assert;
import org.junit.Test;


public class TrainIdTest {
	
	@Test
	public void parsesTrainIds()
	{
		Assert.assertEquals(new TrainId("ICE", "1020", "test"), TrainId.parse("ICE 1020", "test"));
		Assert.assertEquals(new TrainId("CNL", "1020", "test"), TrainId.parse("CNL1020", "test"));
	}

}
