package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteMultiLineStringFeature
		extends Feature<MultiLineString, BigDecimal[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeature(TrainRoute trainRoute) {
		super(trainRoute.createMultiLineString(), new Properties(trainRoute.getId()));
	}

	public static class Properties {

		private final String id;

		@JsonCreator
		public Properties(@JsonProperty("id") String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}
}
