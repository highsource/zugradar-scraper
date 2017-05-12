package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;
import java.util.Objects;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.geometry.model.LonLatAtTime;
import org.hisrc.zugradarscraper.stop.model.StopAtTime;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteSection.Properties;
import org.hisrc.zugradarscraper.timedtrainroute.service.InterpolationUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteSection extends Feature<LineString, double[][], Properties> {

	public TimedTrainRouteSection(StopAtTime departure, StopAtTime arrival, LineString geometry) {
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

	public List<LonLatAtTime> interpolate(long period){
		return InterpolationUtils.interpolate(getGeometry().getCoordinates(), getDeparture().getDateTime(), getArrival().getDateTime(), period);
	}
	
	public static class Properties {
		private final StopAtTime departure;
		private final StopAtTime arrival;

		@JsonCreator
		public Properties(@JsonProperty("departure") StopAtTime departure,
				@JsonProperty("arrival") StopAtTime arrival) {
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
			final TimedTrainRouteSection.Properties that = (TimedTrainRouteSection.Properties) object;
			return Objects.equals(this.departure, that.departure) && Objects.equals(this.arrival, that.arrival);
		}
	}

}
