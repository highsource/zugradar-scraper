package org.hisrc.zugradarscraper.stop.model;

import java.time.LocalDateTime;

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
}
