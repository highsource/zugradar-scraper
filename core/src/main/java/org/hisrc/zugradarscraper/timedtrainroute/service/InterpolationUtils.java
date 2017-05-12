package org.hisrc.zugradarscraper.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class InterpolationUtils {

	public static List<LonLatAtTime> interpolate(double[][] coordinates, LocalDateTime start, LocalDateTime end,
			long period) {

		double length = 0;
		double[] lengths = new double[coordinates.length];
		lengths[0] = 0;
		for (int index = 0; index < coordinates.length; index++) {
			if (index > 0) {
				double lon0 = coordinates[index - 1][0];
				double lat0 = coordinates[index - 1][1];
				double lon1 = coordinates[index][0];
				double lat1 = coordinates[index][1];
				double dlon = lon1 - lon0;
				double dlat = lat1 - lat0;
				length += Math.sqrt(dlon * dlon + dlat * dlat);
				lengths[index] = length;
			}
		}

		final LocalDateTime zero = start.truncatedTo(ChronoUnit.DAYS);

		final long startTime = ChronoUnit.MILLIS.between(zero, start);
		final long endTime = ChronoUnit.MILLIS.between(zero, end);
		final long duration = endTime - startTime;

		final List<LonLatAtTime> result = new ArrayList<>(coordinates.length);
		

		LonLatAtTime last = new LonLatAtTime(new LonLat(coordinates[0][0], coordinates[0][1]), start);

		for (int index = 1; index < coordinates.length - 1; index++) {
			final long time = startTime + Math.round(duration * lengths[index] / length);
			final LocalDateTime dateTime = zero.plus(time, ChronoUnit.MILLIS);
			final LonLatAtTime current = new LonLatAtTime(new LonLat(coordinates[index][0], coordinates[index][1]), dateTime);
			result.addAll(interpolate(last, current, period));
			result.remove(result.size() - 1);
			last = current;
			
		}
		LonLatAtTime e = new LonLatAtTime(
				new LonLat(coordinates[coordinates.length - 1][0], coordinates[coordinates.length - 1][1]), end);
		result.addAll(interpolate(last, e, period));

		return result;
	}

	public static List<LonLatAtTime> interpolate(LonLatAtTime s, LonLatAtTime e, long period) {
		final LonLat start = s.getCoordinates();
		final LonLat end = e.getCoordinates();

		final LocalDateTime zero = s.getDateTime().truncatedTo(ChronoUnit.DAYS);

		final long startTime = ChronoUnit.MILLIS.between(zero, s.getDateTime());
		final long endTime = ChronoUnit.MILLIS.between(zero, e.getDateTime());

		final long duration = endTime - startTime;

		double fullDeltaLon = end.getLon() - start.getLon();
		double fullDeltaLat = end.getLat() - start.getLat();

		final List<LonLatAtTime> result = new ArrayList<>((int) (duration / period) + 1);

		if (startTime % period != 0) {
			result.add(s);
		}

		for (long t = (startTime
				+ (startTime % period == 0 ? 0 : (period - startTime % period))); t <= endTime; t += period) {

			final long deltaT = t - startTime;
			final double lon = start.getLon() + fullDeltaLon * deltaT / duration;
			final double lat = start.getLat() + fullDeltaLat * deltaT / duration;
			result.add(new LonLatAtTime(new LonLat(lon, lat), zero.plus(t, ChronoUnit.MILLIS)));
		}

		if (endTime % period != 0) {
			result.add(e);
		}

		return result;

	}

}
