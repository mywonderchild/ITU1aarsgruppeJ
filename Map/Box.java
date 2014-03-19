package Map;

public class Box {
	public Vector start, stop;

	public Box(Vector start, Vector stop) {
		this.start = start;
		this.stop = stop;
	}

	public Vector dimensions() {
		return stop.copy().sub(start);
	}

	public Vector relativeToAbsolute(Vector relative) {
		return relative.copy().mult(dimensions()).add(start);
	}

	public Vector ratio() {
		Vector dimensions = dimensions();
		if (dimensions.x > dimensions.y)
			return new Vector(1, dimensions.y / dimensions.x);
		else
			return new Vector(dimensions.x / dimensions.y, 1);
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