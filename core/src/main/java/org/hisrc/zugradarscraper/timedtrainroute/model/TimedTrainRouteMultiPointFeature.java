package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.MultiPoint;
import org.hisrc.zugradarscraper.stop.model.StopAtTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteMultiPointFeature
		extends Feature<MultiPoint, double[][], TimedTrainRouteMultiPointFeature.Properties> {

	public TimedTrainRouteMultiPointFeature(StopAtTime departure,
			StopAtTime arrival, List<Long> time, double[][] coordinates) {
		super(new MultiPoint(coordinates), new TimedTrainRouteMultiPointFeature.Properties(departure, arrival, time));
	}

	public static class Properties {
		
		private final List<Long> time;
		
		private final StopAtTime departure;
		private final StopAtTime arrival;

		@JsonCreator
		public Properties(@JsonProperty("departure") StopAtTime departure,
				@JsonProperty("arrival") StopAtTime arrival, @JsonProperty("time") List<Long> time) {
			this.departure = departure;
			this.arrival = arrival;
			this.time = time;
		}

		public StopAtTime getDeparture() {
			return departure;
		}

		public StopAtTime getArrival() {
			return arrival;
		}
		
		public List<Long> getTime() {
			return time;
		}
	}
}
