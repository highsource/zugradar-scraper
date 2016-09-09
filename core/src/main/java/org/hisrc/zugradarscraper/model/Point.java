package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point extends Geometry<BigDecimal[]> {

	@JsonCreator
	public Point(@JsonProperty("coordinates") BigDecimal[] coordinates) {
		super(coordinates);
		Validate.isTrue(coordinates.length >= 2);
	}
//
//	public static Point averageOf(Collection<Point> points) {
//		Validate.notNull(points);
//		Validate.isTrue(points.size() > 1);
//		double lon = points.stream().map(Point::getCoordinates).collect(Collectors.averagingDouble(c -> c[0]));
//		double lat = points.stream().map(Point::getCoordinates).collect(Collectors.averagingDouble(c -> c[1]));
//		return new Point(new BigDecimal[] { lon, lat });
//	}
}
