package org.hisrc.zugradarscraper.trainroute.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;
import org.hisrc.zugradarscraper.train.model.TrainId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteMultiLineStringFeature
		extends Feature<MultiLineString, double[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeature(TrainRoute trainRoute) {
		super(trainRoute.createMultiLineString(), new TrainRouteMultiLineStringFeature.Properties(
				trainRoute.getTrainId(),
				trainRoute.getSections().stream().collect(Collectors.toList())));
	}

	public static class Properties {

		private final TrainId trainId;
		private final List<TrainRouteSection> sections;

		@JsonCreator
		public Properties(@JsonProperty("trainId") TrainId trainId,

				@JsonProperty("sections") List<TrainRouteSection> sections) {
			this.trainId = trainId;
			this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
		}

		public TrainId getTrainId() {
			return trainId;
		}

		public List<TrainRouteSection> getSections() {
			return sections;
		}
	}
}
