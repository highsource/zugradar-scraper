package org.hisrc.zugradarscraper.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainRoute {

	private final String id;

	private final List<TrainRouteSection> sections;

	public TrainRoute(String id, List<TrainRouteSection> sections) {
		this.id = id;
		this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
	}

	public String getId() {
		return id;
	}

	public List<TrainRouteSection> getSections() {
		return sections;
	}

	@Override
	public String toString() {
		return sections.toString();
	}

	public MultiLineString createMultiLineString() {
		final BigDecimal[][][] coordinates = new BigDecimal[getSections().size()][][];
		for (int index = 0; index < this.sections.size(); index++) {
			coordinates[index] = this.sections.get(index).createLineString().getCoordinates();
		}
		return new MultiLineString(coordinates);
	}
}
