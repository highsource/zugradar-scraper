package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.Objects;

import org.hisrc.zugradarscraper.model.TrainRouteSection.Properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteSection extends Feature<LineString, BigDecimal[][], Properties>{
	
	public TrainRouteSection(StopAtTime departure, StopAtTime arrival, LineString geometry) {
		super(geometry, new Properties(departure, arrival));
	}

	@JsonIgnore
	public StopAtTime getDeparture() {
		return getProperties().getDeparture();
	}

	@JsonIgnore
	public StopAtTime getArrival() {
		return getProperties().getArrival();
	}
	

	@Override
	public String toString() {
		return getDeparture() + " -> " + getArrival() + " (" + this.getGeometry().getCoordinates().length + " points)";
	}

	
	public static class Properties {
		private final StopAtTime departure;
		private final StopAtTime arrival;
		
		@JsonCreator
		public Properties(
				 @JsonProperty("departure")
				StopAtTime departure,
				@JsonProperty("arrival")
				StopAtTime arrival) {
			this.departure = departure;
			this.arrival = arrival;
		}

		public StopAtTime getDeparture() {
			return departure;
		}
		
		public StopAtTime getArrival() {
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
			return Objects.equals(this.departure, that.departure) && Objects.equals(this.arrival, that.arrival)
					;
		}
	}
	
}
