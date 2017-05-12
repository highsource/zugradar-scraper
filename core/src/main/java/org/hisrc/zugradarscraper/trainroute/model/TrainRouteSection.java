package org.hisrc.zugradarscraper.trainroute.model;

import java.util.Objects;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.stop.model.Stop;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteSection extends Feature<LineString, double[][], TrainRouteSection.Properties> {

	public TrainRouteSection(Stop departure, Stop arrival, LineString geometry) {
		super(geometry, new Properties(departure, arrival));
	}
	
	@JsonIgnore
	public Stop getDeparture() {
		return getProperties().getDeparture();
	}

	@JsonIgnore
	public Stop getArrival() {
		return getProperties().getArrival();
	}

	@Override
	public String toString() {
		return getDeparture() + " -> " + getArrival() + " (" + this.getGeometry().getCoordinates().length + " points)";
	}
	
	public static class Properties {
		private final Stop departure;
		private final Stop arrival;

		@JsonCreator
		public Properties(@JsonProperty("departure") Stop departure,
				@JsonProperty("arrival") Stop arrival) {
			this.departure = departure;
			this.arrival = arrival;
		}

		public Stop getDeparture() {
			return departure;
		}

		public Stop getArrival() {
			return arrival;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.departure, this.arrival);
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null) {
				return false;
			}
			if (getClass() != object.getClass()) {
				return false;
			}
			final TrainRouteSection.Properties that = (TrainRouteSection.Properties) object;
			return Objects.equals(this.departure, that.departure) && Objects.equals(this.arrival, that.arrival);
		}
	}

}
