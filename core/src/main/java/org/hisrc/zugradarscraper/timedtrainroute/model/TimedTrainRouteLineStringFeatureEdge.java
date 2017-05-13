package org.hisrc.zugradarscraper.timedtrainroute.model;

public class TimedTrainRouteLineStringFeatureEdge implements Edge {

	private final TimedTrainRouteLineStringFeature feature;

	public TimedTrainRouteLineStringFeatureEdge(TimedTrainRouteLineStringFeature feature) {
		this.feature = feature;
	}

	public TimedTrainRouteLineStringFeature getFeature() {
		return feature;
	}

}
