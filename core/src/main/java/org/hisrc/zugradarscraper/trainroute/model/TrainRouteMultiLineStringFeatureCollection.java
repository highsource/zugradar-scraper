package org.hisrc.zugradarscraper.trainroute.model;

import java.util.Arrays;
import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteMultiLineStringFeatureCollection extends
		FeatureCollection<TrainRouteMultiLineStringFeature, MultiLineString, double[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeatureCollection(
			List<TrainRouteMultiLineStringFeature> features) {
		super(features);
	}

}
