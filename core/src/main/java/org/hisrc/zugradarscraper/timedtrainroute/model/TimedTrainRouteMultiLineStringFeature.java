package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;
import org.hisrc.zugradarscraper.train.model.TrainId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteMultiLineStringFeature
		extends Feature<MultiLineString, double[][][], TimedTrainRouteMultiLineStringFeature.Properties> {

	public TimedTrainRouteMultiLineStringFeature(TimedTrainRoute trainRoute) {
		super(trainRoute.createMultiLineString(), new TimedTrainRouteMultiLineStringFeature.Properties(
				trainRoute.getTrainId(),
				trainRoute.getSections().stream().map(TimedTrainRouteSection::getProperties).collect(Collectors.toList())));
	}

	public static class Properties {

		private final TrainId trainId;
		private final List<TimedTrainRouteSection.Properties> sections;

		@JsonCreator
		public Properties(@JsonProperty("trainId") TrainId trainId,

				@JsonProperty("sections") List<TimedTrainRouteSection.Properties> sections) {
			this.trainId = trainId;
			this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
		}

		public TrainId getTrainId() {
			return trainId;
		}

		public List<TimedTrainRouteSection.Properties> getSections() {
			return sections;
		}
	}
}
