package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.List;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.LineString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteLineStringFeatureCollection extends
		FeatureCollection<TimedTrainRouteLineStringFeature, LineString, double[][], TimedTrainRouteLineStringFeature.Properties> {

	@JsonCreator
	public TimedTrainRouteLineStringFeatureCollection(
			@JsonProperty("features") List<TimedTrainRouteLineStringFeature> features) {
		super(features);
	}

}
