package org.hisrc.zugradarscraper.trainroute.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisrc.zugradarscraper.geometry.model.MultiLineString;
import org.hisrc.zugradarscraper.train.model.TrainId;

public class TrainRoute {

	private final TrainId trainId;

	private final List<TrainRouteSection> sections;

	public TrainRoute(TrainId trainId, List<TrainRouteSection> sections) {
		this.trainId = trainId;
		this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
	}

	public TrainId getTrainId() {
		return trainId;
	}

	public List<TrainRouteSection> getSections() {
		return sections;
	}

	@Override
	public String toString() {
		return sections.toString();
	}

	public MultiLineString createMultiLineString() {
		final double[][][] coordinates = new double[getSections().size()][][];
		for (int index = 0; index < this.sections.size(); index++) {
			coordinates[index] = this.sections.get(index).getGeometry().getCoordinates();
		}
		return new MultiLineString(coordinates);
	}
}
