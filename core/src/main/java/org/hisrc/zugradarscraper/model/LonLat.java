package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.Objects;

public class LonLat {

	private final BigDecimal lon;
	private final BigDecimal lat;

	public LonLat(BigDecimal lon, BigDecimal lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	@Override
	public String toString() {
		return "(" + lon.toString() + ", " + lat.toString() + ")";
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

}
