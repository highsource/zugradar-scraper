package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stop {

	private final String name;
	private final String evaNr;
	private final String ds100;
	private final LonLat coordinates;

	public Stop(String name, String evaNr, String ds100, LonLat coordinates) {
		this.name = name;
		this.evaNr = evaNr;
		this.ds100 = ds100;
		this.coordinates = coordinates;
	}

	public String getName() {
		return name;
	}

	public String getEvaNr() {
		return evaNr;
	}

	public String getDs100() {
		return ds100;
	}

	public LonLat getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return name + " [" + evaNr + "/" + ds100 + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.evaNr, this.ds100, this.coordinates);
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
		final Stop that = (Stop) object;
		return Objects.equals(this.name, that.name) && Objects.equals(this.evaNr, that.evaNr)
				&& Objects.equals(this.ds100, that.ds100) && Objects.equals(this.coordinates, that.coordinates);
	}

}
