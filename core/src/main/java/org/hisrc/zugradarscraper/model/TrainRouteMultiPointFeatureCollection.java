package org.hisrc.zugradarscraper.model;

import java.util.List;

public class TrainRouteMultiPointFeatureCollection extends
		FeatureCollection<TrainRouteMultiPointFeature, MultiPoint, double[][], TrainRouteMultiPointFeature.Properties> {

	public TrainRouteMultiPointFeatureCollection(List<TrainRouteMultiPointFeature> features) {
		super(features);
	}

}
