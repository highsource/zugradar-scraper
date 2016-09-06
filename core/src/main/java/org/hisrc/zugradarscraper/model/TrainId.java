package org.hisrc.zugradarscraper.model;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"number", "classification", "id"})
public class TrainId {

	private final String classification;
	private final String number;
	private final String id;

	public TrainId(
			@JsonProperty("classification")
			String classification,
			@JsonProperty("number")
			String number,
			@JsonProperty("id")
			String id) {
		this.classification = classification;
		this.number = number;
		this.id = id;
	}

	public String getClassification() {
		return classification;
	}

	public String getNumber() {
		return number;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TrainId [classification=" + classification + ", number=" + number + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.classification, this.number, this.id);
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
		TrainId that = (TrainId) object;
		return Objects.equals(this.classification, that.classification) && Objects.equals(this.number, that.number)
				&& Objects.equals(this.id, that.id);
	}

	private static final String NUMBER_REGEX = "^(\\D*)(\\d.*)$";
	private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

	public static TrainId parse(String classificationAndNumber, String id) {
		final Matcher matcher = NUMBER_PATTERN.matcher(classificationAndNumber);
		if (!matcher.find()) {
			throw new IllegalArgumentException(
					MessageFormat.format("Invalid train number [{0}].", classificationAndNumber));
		}
		final String classification = matcher.group(1).trim();
		final String number = matcher.group(2);
		return new TrainId(classification, number, id);
	}
}
