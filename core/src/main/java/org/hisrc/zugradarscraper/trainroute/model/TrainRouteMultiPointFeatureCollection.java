package org.hisrc.zugradarscraper.trainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.MultiPoint;

public class TrainRouteMultiPointFeatureCollection extends
		FeatureCollection<TrainRouteMultiPointFeature, MultiPoint, double[][], TrainRouteMultiPointFeature.Properties> {

	public TrainRouteMultiPointFeatureCollection(List<TrainRouteMultiPointFeature> features) {
		super(features);
	}

}
