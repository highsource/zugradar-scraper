package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;

public class TimedTrainRouteMultiLineStringFeatureCollection extends
		FeatureCollection<TimedTrainRouteMultiLineStringFeature, MultiLineString, double[][][], TimedTrainRouteMultiLineStringFeature.Properties> {

	public TimedTrainRouteMultiLineStringFeatureCollection(List<TimedTrainRouteMultiLineStringFeature> features) {
		super(features);
	}

}
