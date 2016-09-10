package org.hisrc.zugradarscraper.model;


import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiPoint extends Geometry<double[][]> {

//	private final double length;

	@JsonCreator
	public MultiPoint(@JsonProperty("coordinates") double[][] coordinates) {
		super(coordinates);
		Validate.isTrue(coordinates.length >= 2);
//		this.length = calculateLength(coordinates);
	}

//	private double calculateLength(double[][] coordinates) {
//		double length = 0;
//		double currentLon = coordinates[0][0];
//		double currentLat = coordinates[0][1];
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
//	public double[] getFirstPoint() {
//		return getCoordinates()[0];
//	}
//
//	@JsonIgnore
//	public double[] getSecondPoint() {
//		return getCoordinates()[1];
//	}
//
//	@JsonIgnore
//	public double[] getPreLastPoint() {
//		final double[][] coordinates = getCoordinates();
//		return getCoordinates()[coordinates.length - 2];
//	}
//
//	@JsonIgnore
//	public double[] getLastPoint() {
//		final double[][] coordinates = getCoordinates();
//		return coordinates[coordinates.length - 1];
//	}
}
