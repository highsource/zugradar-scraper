package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteMultiLineStringFeatureCollection extends
		FeatureCollection<TimedTrainRouteMultiLineStringFeature, MultiLineString, double[][][], TimedTrainRouteMultiLineStringFeature.Properties> {

	@JsonCreator
	public TimedTrainRouteMultiLineStringFeatureCollection(
			@JsonProperty("features") List<TimedTrainRouteMultiLineStringFeature> features) {
		super(features);
	}

}
