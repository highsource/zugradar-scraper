package org.hisrc.zugradarscraper.model;

import java.util.List;

public class Stops extends FeatureCollection<Stop, Point, double[], Stop.Properties> {

	public Stops(List<Stop> features) {
		super(features);
	}

}
