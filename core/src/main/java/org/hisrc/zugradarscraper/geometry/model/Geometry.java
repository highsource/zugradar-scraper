package org.hisrc.zugradarscraper.geometry.model;

import java.util.Arrays;

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

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(new Object[] { this.coordinates });
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
		Geometry that = (Geometry) object;
		return Arrays.deepEquals(new Object[] { this.coordinates }, new Object[] { that.coordinates });
	}

}
