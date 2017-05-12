package org.hisrc.zugradarscraper.trainroute.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisrc.zugradarscraper.feature.model.FeatureCollection;
import org.hisrc.zugradarscraper.geometry.model.LineSegment;
import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.train.model.TrainId;

public class TrainRouteEdgeFeatureCollection
		extends FeatureCollection<TrainRouteEdgeFeature, LineString, double[][], TrainRouteEdgeFeature.Properties> {

	public TrainRouteEdgeFeatureCollection(List<TrainRouteEdgeFeature> features) {
		super(features);
	}

	public TrainRouteEdgeFeatureCollection(TrainRouteMultiLineStringFeatureCollection features) {
		super(createTrainRouteEdges(features));
	}

	private static List<TrainRouteEdgeFeature> createTrainRouteEdges(
			TrainRouteMultiLineStringFeatureCollection features) {

		final Map<LineSegment, TrainRouteEdgeFeature> edgesMap = new HashMap<>();

		features.getFeatures().forEach(feature -> {
			final TrainId trainId = feature.getProperties().getTrainId();

			for (double[][] lineString : feature.getGeometry().getCoordinates()) {
				if (lineString.length > 2) {
					double[] lastPoint = null;
					for (double[] currentPoint : lineString) {
						if (lastPoint != null) {
							final double[] previousPoint = lastPoint;
							final LineSegment segment = new LineSegment(lastPoint[0], lastPoint[1], currentPoint[0],
									currentPoint[1]);
							edgesMap.computeIfAbsent(segment,
									s -> new TrainRouteEdgeFeature(
											new LineString(new double[][] { previousPoint, currentPoint })))
									.getProperties().addTrainId(trainId);
						}
						lastPoint = currentPoint;
					}
				}
			}
		});
		return new ArrayList<>(edgesMap.values());
	}

}
