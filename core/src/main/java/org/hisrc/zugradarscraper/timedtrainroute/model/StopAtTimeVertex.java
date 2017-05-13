package org.hisrc.zugradarscraper.timedtrainroute.model;

import java.util.Objects;

import org.hisrc.zugradarscraper.stop.model.Stop;
import org.hisrc.zugradarscraper.stop.model.StopAtTime;

public class StopAtTimeVertex implements StopVertex {

	private final StopAtTime stopAtTime;

	public StopAtTimeVertex(StopAtTime stopAtTime) {
		Objects.requireNonNull(stopAtTime, "stopAtTime must not be null");
		this.stopAtTime = stopAtTime;
	}

	@Override
	public Stop getStop() {
		return stopAtTime.getStop();
	}

	public StopAtTime getStopAtTime() {
		return stopAtTime;
	}

	@Override
	public String toString() {
		return stopAtTime.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.stopAtTime);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		final StopAtTimeVertex that = (StopAtTimeVertex) object;
		return Objects.equals(this.stopAtTime, that.stopAtTime);
	}

}
