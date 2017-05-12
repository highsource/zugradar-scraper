package org.hisrc.zugradarscraper.trainroute.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hisrc.zugradarscraper.feature.model.Feature;
import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.train.model.TrainId;
import org.hisrc.zugradarscraper.trainroute.model.TrainRouteEdgeFeature.Properties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrainRouteEdgeFeature extends Feature<LineString, double[][], Properties>{
	
	public TrainRouteEdgeFeature(LineString geometry) {
		super(geometry, new Properties(Collections.emptyList()));
	}

	public static final class Properties {
		private List<TrainId> trainIds;
		private List<TrainId> unmodifiableTrainIds; 

		@JsonCreator
		public Properties(@JsonProperty("trainId") List<TrainId> trainIds) {
			this.trainIds = trainIds == null ? new ArrayList<>() : new ArrayList<>(trainIds);
			this.unmodifiableTrainIds = Collections.unmodifiableList(this.trainIds);
		}
		
		public List<TrainId> getTrainIds() {
			return unmodifiableTrainIds;
		}
		
		public void addTrainId(TrainId trainId) {
			this.trainIds.add(trainId);
		}
		
		public int getCount()
		{
			return unmodifiableTrainIds.size();
		}
	}
}
