package org.hisrc.zugradarscraper.stop.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.Point;

public class Stops extends FeatureCollection<Stop, Point, double[], Stop.Properties> {

	public Stops(List<Stop> features) {
		super(features);
	}

}
