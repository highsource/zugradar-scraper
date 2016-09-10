package org.hisrc.zugradarscraper.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Stop extends Feature<Point, double[], Stop.Properties>{

	public Stop(String name, String evaNr, String ds100, double[] coordinates) {
		super(new Point(coordinates), new Properties(name, evaNr, ds100));
	}

	public String getName() {
		return getProperties().getName();
	}

	public String getEvaNr() {
		return getProperties().getEvaNr();
	}

	public String getDs100() {
		return getProperties().getDs100();
	}

	@Override
	public String toString() {
		return getProperties().toString() + " " + getGeometry();
	}

	public static class Properties {
		private final String name;
		private final String evaNr;
		private final String ds100;

		@JsonCreator
		public Properties(@JsonProperty("name") String name, @JsonProperty("evaNr") String evaNr,
				@JsonProperty("evads100") String ds100) {
			this.name = name;
			this.evaNr = evaNr;
			this.ds100 = ds100;
		}

		public String getName() {
			return name;
		}

		public String getEvaNr() {
			return evaNr;
		}

		public String getDs100() {
			return ds100;
		}

		@Override
		public String toString() {
			return name + " [" + evaNr + "/" + ds100 + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.evaNr, this.ds100);
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
			final Properties that = (Properties) object;
			return Objects.equals(this.name, that.name) && Objects.equals(this.evaNr, that.evaNr)
					&& Objects.equals(this.ds100, that.ds100);
		}

	}

}
