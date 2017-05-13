package org.hisrc.zugradarscraper.stop.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StopAtTime {

	private final Stop stop;
	private final LocalDateTime dateTime;

	@JsonCreator
	public StopAtTime(@JsonProperty("stop") Stop stop, @JsonProperty("dateTime") LocalDateTime dateTime) {
		this.stop = stop;
		this.dateTime = dateTime;
	}

	public Stop getStop() {
		return stop;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	@Override
	public String toString() {
		return this.stop + "@" +  this.dateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.stop, this.dateTime);
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
		final StopAtTime that = (StopAtTime) object;
		return Objects.equals(this.stop, that.stop) && Objects.equals(this.dateTime, that.dateTime);
	}

}
