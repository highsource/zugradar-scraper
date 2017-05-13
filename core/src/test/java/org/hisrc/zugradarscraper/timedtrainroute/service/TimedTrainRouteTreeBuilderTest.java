package org.hisrc.zugradarscraper.timedtrainroute.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.model.StopAtTime;
import org.hisrc.zugradarscraper.timedtrainroute.model.Edge;
import org.hisrc.zugradarscraper.timedtrainroute.model.StartStopVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.StopVertex;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteLineStringFeature;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteLineStringFeatureEdge;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiLineStringFeatureCollection;
import org.hisrc.zugradarscraper.timedtrainroute.model.TimedTrainRouteMultiPointFeatureCollection;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TimedTrainRouteTreeBuilderTest {
	private final Logger LOGGER = LoggerFactory.getLogger(TimedTrainRouteTreeBuilder.class);

	private TimedTrainRouteTreeBuilder sut = new TimedTrainRouteTreeBuilder();

	@Test
	public void test() throws IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TimedTrainRouteMultiLineStringFeatureCollection featureCollection = objectMapper
				.readerFor(TimedTrainRouteMultiLineStringFeatureCollection.class)
				.readValue(getClass().getResourceAsStream("timedTrainRoutes-2017-05-13.geojson"));
		Graph<StopVertex, Edge> graph = sut.build(featureCollection);
		
		Set<StopVertex> vertexSet = graph.vertexSet();

		Map<String, StartStopVertex> startStopVertexMap = vertexSet.stream()
				.filter(vertex -> vertex instanceof StartStopVertex).map(vertex -> (StartStopVertex) vertex)
				.collect(Collectors.toMap(vertex -> vertex.getStop().getEvaNr(), Function.identity()));

//		StopVertex start = startStopVertexMap.get("8011160");
		StopVertex start = startStopVertexMap.get("8000105");
		SingleSourcePaths<StopVertex, Edge> paths = new DijkstraShortestPath<>(graph).getPaths(start);

		final Map<Stop, TimedTrainRouteLineStringFeature> features = new HashMap<>();

		for (StopVertex sink : vertexSet) {
			GraphPath<StopVertex, Edge> path = paths.getPath(sink);
			if (path != null) {
				System.out.println(path.getVertexList());
				path.getEdgeList().stream().filter(edge -> edge instanceof TimedTrainRouteLineStringFeatureEdge)
						.map(edge -> (TimedTrainRouteLineStringFeatureEdge) edge)
						.map(TimedTrainRouteLineStringFeatureEdge::getFeature)
						.forEach(f -> {
							StopAtTime arrival = f.getProperties().getSection().getArrival();
							Stop arrivalStop = arrival.getStop();
							features.merge(arrivalStop, f, (u, v) -> u.getProperties().getSection().getArrival().getDateTime().isBefore(v.getProperties().getSection().getArrival().getDateTime()) ? u : v);
						});
			}
		}
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
		
		TimedTrainRouteMultiPointFeatureCollection fc = new TimedTrainRouteMultiPointFeatureCollection(
				features.values().stream().map(f -> f.interpolateTrainRouteMultiPointFeature(10000)).collect(Collectors.toList()));
		mapper.writerFor(TimedTrainRouteMultiPointFeatureCollection.class).withDefaultPrettyPrinter().writeValue(new FileOutputStream(new File("timedTrainRouteMultiPointFeatureCollection.geojson")), fc);

	}

}
