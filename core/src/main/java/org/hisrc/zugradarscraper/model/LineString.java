package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LineString extends Geometry<BigDecimal[][]> {

//	private final double length;

	@JsonCreator
	public LineString(@JsonProperty("coordinates") BigDecimal[][] coordinates) {
		super(coordinates);
		Validate.isTrue(coordinates.length >= 2);
//		this.length = calculateLength(coordinates);
	}

//	private double calculateLength(BigDecimal[][] coordinates) {
//		BigDecimal length = 0;
//		BigDecimal currentLon = coordinates[0][0];
//		BigDecimal currentLat = coordinates[0][1];
//		for (int index = 1; index < coordinates.length; index++) {
//			double nextLon = coordinates[index][0];
//			double nextLat = coordinates[index][1];
//			double dLon = nextLon - currentLon;
//			double dLat = nextLat - currentLat;
//			length += Math.sqrt(dLon * dLon + dLat * dLat);
//			currentLon = nextLon;
//			currentLat = nextLat;
//		}
//		return length;
//	}

//	@JsonIgnore
//	public double getLength() {
//		return length;
//	}

//	public LineString reverse() {
//		final double[][] reversedCoordinates = ArrayUtils.clone(getCoordinates());
//		ArrayUtils.reverse(reversedCoordinates);
//		return new LineString(reversedCoordinates);
//	}

//	@JsonIgnore
//	public BigDecimal[] getFirstPoint() {
//		return getCoordinates()[0];
//	}
//
//	@JsonIgnore
//	public BigDecimal[] getSecondPoint() {
//		return getCoordinates()[1];
//	}
//
//	@JsonIgnore
//	public BigDecimal[] getPreLastPoint() {
//		final BigDecimal[][] coordinates = getCoordinates();
//		return getCoordinates()[coordinates.length - 2];
//	}
//
//	@JsonIgnore
//	public BigDecimal[] getLastPoint() {
//		final BigDecimal[][] coordinates = getCoordinates();
//		return coordinates[coordinates.length - 1];
//	}
}
