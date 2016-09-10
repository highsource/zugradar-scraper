package org.hisrc.zugradarscraper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteMultiLineStringFeature
		extends Feature<MultiLineString, double[][][], TrainRouteMultiLineStringFeature.Properties> {

	public TrainRouteMultiLineStringFeature(TrainRoute trainRoute) {
		super(trainRoute.createMultiLineString(), new TrainRouteMultiLineStringFeature.Properties(
				trainRoute.getTrainId(),
				trainRoute.getSections().stream().map(TrainRouteSection::getProperties).collect(Collectors.toList())));
	}

	public static class Properties {

		private final TrainId trainId;
		private final List<TrainRouteSection.Properties> sections;

		@JsonCreator
		public Properties(@JsonProperty("trainId") TrainId trainId,

				@JsonProperty("sections") List<TrainRouteSection.Properties> sections) {
			this.trainId = trainId;
			this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
		}

		public TrainId getTrainId() {
			return trainId;
		}

		public List<TrainRouteSection.Properties> getSections() {
			return sections;
		}
	}
}
