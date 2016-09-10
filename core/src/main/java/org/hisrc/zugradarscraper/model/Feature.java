package org.hisrc.zugradarscraper.model;

import org.apache.commons.lang3.Validate;

public class Feature<G extends Geometry<C>, C, P> {

	private G geometry;
	private P properties;

	public Feature(G geometry, P properties) {
		Validate.notNull(geometry);
		Validate.notNull(properties);
		this.geometry = geometry;
		this.properties = properties;
	}

	public String getType() {
		return "Feature";
	}

	public G getGeometry() {
		return geometry;
	}

	public P getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return getProperties().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Feature other = (Feature) obj;
		if (geometry == null) {
			if (other.geometry != null) {
				return false;
			}
		} else if (!geometry.equals(other.geometry)) {
			return false;
		}
		if (properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!properties.equals(other.properties)) {
			return false;
		}
		return true;
	}

}
