package org.hisrc.zugradarscraper.timedtrainroute.service;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hisrc.zugradarscraper.geometry.model.LineString;
import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.timedtrainroute.model.Edge;
import org.hisrc.zugradarscraper.timedtrainroute.model.EndStopVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.StartStopVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.StopAtTimeVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.StopVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteLineStringFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteLineStringFeatureEdge;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteSection.Properties;
import org.hisrc.zugradarscraper.timedtrainroute.model.TransitEdge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AsWeightedDirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimedTrainRouteTreeBuilder {

	private final Logger LOGGER = LoggerFactory.getLogger(TimedTrainRouteTreeBuilder.class);

	public Graph<StopVertex, Edge> build(TimedTrainRouteMultiLineStringFeatureCollection featureCollection) {
		final WeightedGraph<StopVertex, Edge> graph = 
				new AsWeightedDirectedGraph<>(
				new DefaultDirectedGraph<>(new EdgeFactory<StopVertex, Edge>() {
			@Override
			public Edge createEdge(StopVertex arg0, StopVertex arg1) {
				throw new UnsupportedOperationException();
			}
		}), new HashMap<>());

		List<TimedTrainRouteMultiLineStringFeature> features = featureCollection.getFeatures();

		for (TimedTrainRouteMultiLineStringFeature feature : features) {
			build(graph, feature);
		}

		Map<Stop, SortedSet<StopAtTimeVertex>> stopAtTimeVerticesByStop = new HashMap<>();

		final Set<StopVertex> vertices = new HashSet<>(graph.vertexSet());
		vertices.stream().map(vertex -> (StopAtTimeVertex) vertex).forEach(vertex -> {
			stopAtTimeVerticesByStop
					.computeIfAbsent(vertex.getStop(),
							stop -> new TreeSet<>(Comparator.comparing(v -> v.getStopAtTime().getDateTime())))
					.add(vertex);
		});

		stopAtTimeVerticesByStop.entrySet().stream().forEach(entry -> {
			Stop stop = entry.getKey();
			StartStopVertex startStopVertex = new StartStopVertex(stop);
			graph.addVertex(startStopVertex);
			EndStopVertex endStopVertex = new EndStopVertex(stop);
			graph.addVertex(endStopVertex);
			SortedSet<StopAtTimeVertex> stopAtTimeVertices = entry.getValue();
			StopAtTimeVertex lastVertex = null;
			for (StopAtTimeVertex vertex : stopAtTimeVertices) {
				LOGGER.info("{} -> {}", startStopVertex, vertex);
				TransitEdge startEdge = new TransitEdge();
				graph.addEdge(startStopVertex, vertex, startEdge);
				graph.setEdgeWeight(startEdge, 0);
				LOGGER.info("{} -> {}", vertex, endStopVertex);
				TransitEdge endEdge = new TransitEdge();
				graph.addEdge(vertex, endStopVertex, endEdge);
				graph.setEdgeWeight(endEdge, 0);
				if (lastVertex != null) {
					LOGGER.info("{} -> {}", lastVertex, vertex);
					final TransitEdge transitEdge = new TransitEdge();
					graph.addEdge(lastVertex, vertex, transitEdge);
					graph.setEdgeWeight(transitEdge, ChronoUnit.SECONDS.between(lastVertex.getStopAtTime().getDateTime(), vertex.getStopAtTime().getDateTime()));
				}
				lastVertex = vertex;
			}
		});
		
		return graph;
	}

	public void build(WeightedGraph<StopVertex, Edge> graph, TimedTrainRouteMultiLineStringFeature feature) {
		double[][][] coordinates = feature.getGeometry().getCoordinates();
		for (int index = 0; index < coordinates.length; index++) {
			double[][] lineString = coordinates[index];
			Properties section = feature.getProperties().getSections().get(index);
			TimedTrainRouteLineStringFeature edge = new TimedTrainRouteLineStringFeature(new LineString(lineString),
					new TimedTrainRouteLineStringFeature.Properties(feature.getProperties().getTrainId(), section));

			final StopAtTimeVertex departure = new StopAtTimeVertex(section.getDeparture());
			final StopAtTimeVertex arrival = new StopAtTimeVertex(section.getArrival());

			graph.addVertex(departure);
			graph.addVertex(arrival);
			LOGGER.info("{} -> {}", departure, arrival);
			final TimedTrainRouteLineStringFeatureEdge featureEdge = new TimedTrainRouteLineStringFeatureEdge(edge);
			graph.addEdge(departure, arrival, featureEdge);
			graph.setEdgeWeight(featureEdge, ChronoUnit.SECONDS.between(departure.getStopAtTime().getDateTime(), arrival.getStopAtTime().getDateTime()));

		}
	}

	public void build(double[][] lineString, Properties section) {

	}

}
