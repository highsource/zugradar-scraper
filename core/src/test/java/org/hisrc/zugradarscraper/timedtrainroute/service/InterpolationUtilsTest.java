package org.hisrc.zugradarscraper.timedtrainroute.service;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.hisrc.zugradarscraper.geometry.model.LonLat;
import org.hisrc.zugradarscraper.geometry.model.LonLatAtTime;
import org.hisrc.zugradarscraper.timedtrainroute.service.InterpolationUtils;
import org.junit.Assert;
import org.junit.Test;

public class InterpolationUtilsTest {

	@Test
	public void correctlyInterpolates() {
		final LonLatAtTime a0 = new LonLatAtTime(new LonLat(0, 0), LocalDateTime.of(2016, 1, 1, 0, 0, 0));
		final LonLatAtTime a5 = new LonLatAtTime(new LonLat(5, 0.5), LocalDateTime.of(2016, 1, 1, 0, 0, 5));
		final LonLatAtTime a10 = new LonLatAtTime(new LonLat(10, 1), LocalDateTime.of(2016, 1, 1, 0, 0, 10));
		final LonLatAtTime a20 = new LonLatAtTime(new LonLat(20, 2), LocalDateTime.of(2016, 1, 1, 0, 0, 20));
		final LonLatAtTime a25 = new LonLatAtTime(new LonLat(25, 2.5), LocalDateTime.of(2016, 1, 1, 0, 0, 25));
		final LonLatAtTime a30 = new LonLatAtTime(new LonLat(30, 3), LocalDateTime.of(2016, 1, 1, 0, 0, 30));
		final LonLatAtTime a40 = new LonLatAtTime(new LonLat(40, 4), LocalDateTime.of(2016, 1, 1, 0, 0, 40));
		final LonLatAtTime a45 = new LonLatAtTime(new LonLat(45, 4.5), LocalDateTime.of(2016, 1, 1, 0, 0, 45));
		final LonLatAtTime a50 = new LonLatAtTime(new LonLat(50, 5), LocalDateTime.of(2016, 1, 1, 0, 0, 50));
		Assert.assertEquals(Arrays.asList(a5,a10,a20,a25), InterpolationUtils.interpolate(a5, a25, 10000L));
		Assert.assertEquals(Arrays.asList(a0,a10,a20,a30,a40,a50), InterpolationUtils.interpolate(a0, a50, 10000L));
		Assert.assertEquals(Arrays.asList(a5,a10,a20,a30,a40,a45), InterpolationUtils.interpolate(a5, a45, 10000L));
		Assert.assertEquals(Arrays.asList(a5,a10,a20,a25,a30,a40,a45), 
				InterpolationUtils.interpolate(
						new double[][]{
							{5,0.5},
							{25,2.5},
							{45,4.5}
						},
						LocalDateTime.of(2016, 1, 1, 0, 0, 5),
						LocalDateTime.of(2016, 1, 1, 0, 0, 45), 10000L));
		Assert.assertEquals(Arrays.asList(a10,a20,a25,a30,a40), 
				InterpolationUtils.interpolate(
						new double[][]{
							{10,1},
							{25,2.5},
							{40,4}
						},
						LocalDateTime.of(2016, 1, 1, 0, 0, 10),
						LocalDateTime.of(2016, 1, 1, 0, 0, 40), 10000L));

	}
}
