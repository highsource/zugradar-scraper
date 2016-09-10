package org.hisrc.zugradarscraper.model;

import org.apache.commons.lang3.Validate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Point.class, name = "Point"), @Type(value = LineString.class, name = "LineString"),
		@Type(value = MultiPoint.class, name = "MultiPoint"),
		@Type(value = MultiLineString.class, name = "MultiLineString") })
public abstract class Geometry<C> {

	private final C coordinates;

	public Geometry(C coordinates) {
		Validate.notNull(coordinates);
		this.coordinates = coordinates;
	}

	public C getCoordinates() {
		return coordinates;
	}
}
