package org.hisrc.zugradarscraper.train.model;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"number", "classification", "startDate", "id"})
public class TrainId {
	
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");

	private final String classification;
	private final String number;
	private final String id;
	@JsonFormat(pattern="yyyy-MM-dd")
	private final LocalDate startDate;

	public TrainId(
			@JsonProperty("classification")
			String classification,
			@JsonProperty("number")
			String number,
			@JsonProperty("id")
			String id,
			@JsonProperty("startDate")
			LocalDate startDate) {
		this.classification = classification;
		this.number = number;
		this.id = id;
		this.startDate = startDate;
	}

	public String getClassification() {
		return classification;
	}

	public String getNumber() {
		return number;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TrainId [" + classification + " " + number + "/=" + startDate + " - " + id + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.classification, this.number, this.id, this.startDate);
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
		return Objects.equals(this.classification, that.classification)
				&& Objects.equals(this.number, that.number)
				&& Objects.equals(this.id, that.id)
				&& Objects.equals(this.startDate, that.startDate);
	}

	private static final String NUMBER_REGEX = "^(\\D*)(\\d.*)$";
	private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER_REGEX);

	public static TrainId parse(String classificationAndNumber, String id, String startDateString) {
		final Matcher matcher = NUMBER_PATTERN.matcher(classificationAndNumber);
		if (!matcher.find()) {
			throw new IllegalArgumentException(
					MessageFormat.format("Invalid train number [{0}].", classificationAndNumber));
		}
		final String classification = matcher.group(1).trim();
		final String number = matcher.group(2);
		
		final LocalDate startDate = LocalDate.parse(startDateString, DATE_FORMATTER); 
		
		return new TrainId(classification, number, id, startDate);
	}
}
