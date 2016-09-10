package org.hisrc.zugradarscraper.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteMultiPointFeature
		extends Feature<MultiPoint, double[][], TrainRouteMultiPointFeature.Properties> {

	public TrainRouteMultiPointFeature(List<Long> time, double[][] coordinates) {
		super(new MultiPoint(coordinates), new TrainRouteMultiPointFeature.Properties(time));
	}

	public static class Properties {
		
		private final List<Long> time;
		
		@JsonCreator
		public Properties(@JsonProperty("time") List<Long> time) {
			this.time = time;
		}
		
		public List<Long> getTime() {
			return time;
		}
	}
}
