package org.hisrc.zugradarscraper.geometry.model;


import java.util.Arrays;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point extends Geometry<double[]> {

	@JsonCreator
	public Point(@JsonProperty("coordinates") double[] coordinates) {
		super(coordinates);
		Validate.isTrue(coordinates.length >= 2);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(getCoordinates());
	}
}
