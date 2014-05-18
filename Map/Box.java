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

	public boolean overlapping(Vector start, Vector end) {
		Vector ne = this.start.copy(); ne.x = this.stop.x;
		Vector sw = this.start.copy(); sw.y = this.stop.y;

		return start.isInside(this) || end.isInside(this) ||
			lineoverlapping(start, end, ne, this.start) || 	// top
			lineoverlapping(start, end, ne, this.stop) ||	// right
			lineoverlapping(start, end, sw, this.stop) ||	// bottom
			lineoverlapping(start, end, sw, this.start);	// left
	}

	private static boolean lineoverlapping(Vector start1, Vector end1, Vector start2, Vector end2) {
		// source: http://stackoverflow.com/a/5514619
		// algorithm verified by test in testBox class

		double q = (start1.y - start2.y) * (end2.x - start2.x) - (start1.x - start2.x) * (end2.y - start2.y);
		double d = (end1.x - start1.x) * (end2.y - start2.y) - (end1.y - start1.y) * (end2.x - start2.x);

		if(d == 0) return false;

		double r = q / d;

		q = (start1.y - start2.y) * (end1.x - start1.x) - (start1.x - start2.x) * (end1.y - start1.y);
		double s = q / d;

		if(r < 0 || r > 1 || s < 0 || s > 1) {
			return false;
		}

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

	// Makes start vector top-left corner and stop vector bottom-right corner
	public Box properCorners() {
		if(start.x > stop.x)
			flipX();
		if(start.y > stop.y)
			flipY();
		return this;
	}
	
	// Flips the x-axis of the box
	public Box flipX() {
		start = start.copy(); // Prevents possibly unwanted changes
		stop = stop.copy();   // to vector objects.
		double temp = start.x;
		start.x = stop.x;
		stop.x = temp;
		return this;
	}
	
	// Flips the y-axis of the box
	public Box flipY() {
		start = start.copy(); // Prevents possibly unwanted changes
		stop = stop.copy();   // to vector objects.
		double temp = start.y;
		start.y = stop.y;
		stop.y = temp;
		return this;
	}

	// Grows the box in all directions
	public Box grow(double amount) {
		start.x -= amount;
		start.y -= amount;
		stop.x += amount;
		stop.y += amount;
		return this;
	}

	// Copies the box and its start and stop vectors
	public Box copy() {
		return new Box(start.copy(), stop.copy());
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