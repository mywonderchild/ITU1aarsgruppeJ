package Map;

public class Box {
	public Vector start, stop;

	public Box(Vector start, Vector stop) {
		this.start = start;
		this.stop = stop;
	}

	// Returns the dimensions of the box
	public Vector dimensions() {
		return stop.copy().sub(start).abs();
	}

	// Converts a vector inside the box to absolute coordinates
	public Vector relativeToAbsolute(Vector relative) {
		return relative.copy().mult(dimensions()).add(start);
	}

	// Converts a vector inside the box to relative coordinates
	public Vector absoluteToRelative(Vector absolute) {
		return absolute.copy().sub(start).div(dimensions());
	}

	// Returns the ratio of width and height
	public Vector ratio() {
		Vector dimensions = dimensions();
		if (dimensions.x > dimensions.y)
			return new Vector(1, dimensions.y / dimensions.x);
		else
			return new Vector(dimensions.x / dimensions.y, 1);
	}

	// Translate box from current box to other box
	public Box translate(Box from, Box to) {
		Vector scale = to.dimensions().div(from.dimensions());
		start.mult(scale);
		stop.mult(scale);
		return this;
	}

	// Determines if this box is overlapping another box
	public boolean overlapping(Box box) {
		if (box.start.x > stop.x) return false;
		if (box.stop.x < start.x) return false;
		if (box.start.y > stop.y) return false;
		if (box.stop.y < start.y) return false;
		return true;
	}

	// Returns the center of the box
	public Vector getCenter() {
		return start.copy().add(stop).div(2);
	}

	// Returns box coordinates as two-dimensional array
	public double[][] toArray() {
		return new double[][]{start.toArray(), stop.toArray()};
	}

	// Scales the box keeping the center in place
	public Box scale(double scalar) {
		Vector center = getCenter();
		Vector offset = center.copy().sub(start).mult(scalar);
		start = center.copy().sub(offset);
		stop = center.copy().add(offset);
		return this;
	}

	@Override
	public String toString() {
		return String.format(
			"[%s, %s]",
			start.toString(),
			stop.toString()
		);
	}
}