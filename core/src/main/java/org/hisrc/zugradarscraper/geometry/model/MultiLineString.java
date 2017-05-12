package org.hisrc.zugradarscraper.geometry.model;


import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiLineString extends Geometry<double[][][]> {

	@JsonCreator
	public MultiLineString(@JsonProperty("coordinates") double[][][] coordinates) {
		super(coordinates);
		for (double[][] lineString : coordinates) {
			Validate.isTrue(lineString.length >= 2);
			for (double[] point : lineString) {
				Validate.isTrue(point.length >= 2);
			}
		}
	}

}
