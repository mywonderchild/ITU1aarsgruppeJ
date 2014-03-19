package Map;

public class Box {
	public Vector start, stop;

	public Box(Vector start, Vector stop) {
		this.start = start;
		this.stop = stop;
	}

	public Vector dimensions() {
		return stop.copy().sub(start).abs();
	}

	public Vector relativeToAbsolute(Vector relative) {
		return relative.copy().mult(dimensions()).add(start);
	}

	public Vector absoluteToRelative(Vector absolute) {
		return absolute.copy().sub(start).div(dimensions());
	}

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

	public Vector getCenter() {
		return start.copy().add(stop).div(2);
	}

	public double[][] toArray() {
		return new double[][]{start.toArray(), stop.toArray()};
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