package org.hisrc.zugradarscraper.geometry.model;

import java.util.Objects;

public class LineSegment extends LineString {

	private double x0;
	private double y0;
	private double x1;
	private double y1;

	public LineSegment(double x0, double y0, double x1, double y1) {
		super(new double[][] { { x0, y0 }, { x1, y1 } });
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x0, this.y0) + Objects.hash(this.x1, this.y1);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LineSegment other = (LineSegment) obj;

		return (Double.doubleToLongBits(x0) == Double.doubleToLongBits(other.x0)
				&& Double.doubleToLongBits(y0) == Double.doubleToLongBits(other.y0)
				&& Double.doubleToLongBits(x1) == Double.doubleToLongBits(other.x1)
				&& Double.doubleToLongBits(y1) == Double.doubleToLongBits(other.y1))
				|| (Double.doubleToLongBits(x0) == Double.doubleToLongBits(other.x1)
						&& Double.doubleToLongBits(y0) == Double.doubleToLongBits(other.y1)
						&& Double.doubleToLongBits(x1) == Double.doubleToLongBits(other.x0)
						&& Double.doubleToLongBits(y1) == Double.doubleToLongBits(other.y0));
	}

}
