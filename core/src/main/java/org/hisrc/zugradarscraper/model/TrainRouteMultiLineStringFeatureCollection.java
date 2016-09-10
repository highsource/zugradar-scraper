package org.hisrc.zugradarscraper.model;

import java.util.List;

public class TrainRouteMultiLineStringFeatureCollection extends
		FeatureCollection<TrainRouteMultiLineStringFeature, MultiLineString, double[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeatureCollection(List<TrainRouteMultiLineStringFeature> features) {
		super(features);
	}

}
