package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TrainRouteSection {

	private final Stop departure;
	private final Stop arrival;
	private final List<LonLat> coordinates;

	public TrainRouteSection(Stop departure, Stop arrival, List<LonLat> coordinates) {
		this.departure = departure;
		this.arrival = arrival;
		this.coordinates = Collections.unmodifiableList(new ArrayList<>(coordinates));
	}

	public Stop getDeparture() {
		return departure;
	}

	public Stop getArrival() {
		return arrival;
	}

	public List<LonLat> getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return getDeparture() + " -> " + getArrival() + " (" + this.coordinates.size() + " points)";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.departure, this.arrival, this.coordinates);
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
		final TrainRouteSection that = (TrainRouteSection) object;
		return Objects.equals(this.departure, that.departure) && Objects.equals(this.arrival, that.arrival)
				&& Objects.equals(this.coordinates, that.coordinates);
	}

	public LineString createLineString() {
		final BigDecimal[][] coordinates = new BigDecimal[this.coordinates.size()][];
		for (int index = 0; index < this.coordinates.size(); index++) {
			coordinates[index] = this.coordinates.get(index).getCoordinates();
		}
		return new LineString(coordinates);
	}

}
