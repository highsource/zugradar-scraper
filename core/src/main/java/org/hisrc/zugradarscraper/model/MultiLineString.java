package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiLineString extends Geometry<BigDecimal[][][]> {

	@JsonCreator
	public MultiLineString(@JsonProperty("coordinates") BigDecimal[][][] coordinates) {
		super(coordinates);
		for (BigDecimal[][] lineString : coordinates) {
			Validate.isTrue(lineString.length >= 2);
			for (BigDecimal[] point : lineString) {
				Validate.isTrue(point.length >= 2);
			}
		}
	}

}
