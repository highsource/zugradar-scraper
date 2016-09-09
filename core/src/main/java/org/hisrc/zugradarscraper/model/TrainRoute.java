package org.hisrc.zugradarscraper.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainRoute {

	private final List<TrainRouteSection> sections;

	public TrainRoute(List<TrainRouteSection> sections) {
		this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
	}
	
	public List<TrainRouteSection> getSections() {
		return sections;
	}
	
	@Override
	public String toString() {
		return sections.toString();
	}
}
