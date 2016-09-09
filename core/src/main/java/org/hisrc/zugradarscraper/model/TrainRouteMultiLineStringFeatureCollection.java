package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.List;

public class TrainRouteMultiLineStringFeatureCollection extends
		FeatureCollection<TrainRouteMultiLineStringFeature, MultiLineString, BigDecimal[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeatureCollection(List<TrainRouteMultiLineStringFeature> features) {
		super(features);
	}

}
