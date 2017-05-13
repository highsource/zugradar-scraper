package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.MultiPoint;

public class TimedTrainRouteMultiPointFeatureCollection extends
		FeatureCollection<TimedTrainRouteMultiPointFeature, MultiPoint, double[][], TimedTrainRouteMultiPointFeature.Properties> {

	public TimedTrainRouteMultiPointFeatureCollection(List<TimedTrainRouteMultiPointFeature> features) {
		super(features);
	}

}
