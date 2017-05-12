package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hisrc.zugradarscraper.geometry.model.LonLatAtTime;
import org.hisrc.zugradarscraper.geometry.model.MultiLineString;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteMultiPointFeature;

public class TimedTrainRoute {

	private final TrainId trainId;

	private final List<TimedTrainRouteSection> sections;

	public TimedTrainRoute(TrainId trainId, List<TimedTrainRouteSection> sections) {
		this.trainId = trainId;
		this.sections = Collections.unmodifiableList(new ArrayList<>(sections));
	}

	public TrainId getTrainId() {
		return trainId;
	}

	public List<TimedTrainRouteSection> getSections() {
		return sections;
	}
	
	public List<LonLatAtTime> interpolate(long period) {
		final List<LonLatAtTime> result = new LinkedList<>();
		for (TimedTrainRouteSection section : getSections()) {
			result.addAll(section.interpolate(period));
		}
		return result;
	}
	
	public TrainRouteMultiPointFeature interpolateTrainRouteMultiPointFeature(long period)
	{
		final List<LonLatAtTime> items = interpolate(period);
		
		final double[][] coordinates = new double[items.size()][];
		final List<Long> time = new ArrayList<>(items.size());
		
		for (int index = 0; index < items.size(); index++)
		{
			final LonLatAtTime item = items.get(index);
			LocalDateTime dateTime = item.getDateTime();
			coordinates[index] = item.getCoordinates().getCoordinates();
			time.add(dateTime.atZone(ZoneId.of("Europe/Paris")).toInstant().toEpochMilli());
		}
		return new TrainRouteMultiPointFeature(time, coordinates);
		
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
