package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.geometry.model.LonLatAtTime;
import org.hisrc.zugradarscraper.timedtrainroute.service.InterpolationUtils;
import org.hisrc.zugradarscraper.train.model.TrainId;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimedTrainRouteLineStringFeature
		extends Feature<LineString, double[][], TimedTrainRouteLineStringFeature.Properties> {

	@JsonCreator
	public TimedTrainRouteLineStringFeature(
			@JsonProperty("geometry")
			LineString geometry,
			@JsonProperty("properties")
			TimedTrainRouteLineStringFeature.Properties properties)
	{
		super(geometry, properties);
	}
	
	public TimedTrainRouteMultiPointFeature interpolateTrainRouteMultiPointFeature(long period)
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
		return new TimedTrainRouteMultiPointFeature(getProperties().getSection().getDeparture(), getProperties().getSection().getArrival(), time, coordinates);
		
	}
	
	public List<LonLatAtTime> interpolate(long period) {
		final List<LonLatAtTime> result = new LinkedList<>();
		result.addAll(InterpolationUtils.interpolate(getGeometry().getCoordinates(), 
				getProperties().getSection().getDeparture().getDateTime(), getProperties().getSection().getArrival().getDateTime(), period));
		return result;
	}


	
	public static class Properties {

		private final TrainId trainId;
		private final TimedTrainRouteSection.Properties section;

		@JsonCreator
		public Properties(@JsonProperty("trainId") TrainId trainId,
				@JsonProperty("section") TimedTrainRouteSection.Properties section) {
			this.trainId = trainId;
			this.section = section;
		}

		public TrainId getTrainId() {
			return trainId;
		}
		
		public TimedTrainRouteSection.Properties getSection() {
			return section;
		}
	}	
}
