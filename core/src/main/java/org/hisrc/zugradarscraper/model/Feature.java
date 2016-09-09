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
}
