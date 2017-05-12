package org.hisrc.zugradarscraper.geometry.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class LonLatAtTime {

	private final LonLat coordinates;
	private final LocalDateTime dateTime;

	public LonLatAtTime(LonLat coordinates, LocalDateTime dateTime) {
		this.coordinates = coordinates;
		this.dateTime = dateTime;
	}

	public LonLat getCoordinates() {
		return coordinates;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	

	@Override
	public String toString() {
		return coordinates + "@" + dateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordinates, this.dateTime);
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
		final LonLatAtTime that = (LonLatAtTime) object;
		return Objects.equals(this.coordinates, that.coordinates) && Objects.equals(this.dateTime, that.dateTime);
	}

}
