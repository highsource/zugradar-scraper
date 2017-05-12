package org.hisrc.zugradarscraper.geometry.model;

import java.util.Objects;

public class LonLat {

	private final double lon;
	private final double lat;

	public LonLat(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	@Override
	public String toString() {
		return "(" + lon + ", " + lat + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.lon, this.lat);
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
		LonLat that = (LonLat) object;
		return Objects.equals(this.lon, that.lon) && Objects.equals(this.lat, that.lat);
	}

	public double[] getCoordinates() {
		return new double[]{this.lon, this.lat};
	}

}
