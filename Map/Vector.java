package Map;

public class Vector {

	public double x, y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Set the x and y component
	public Vector set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	// Get a new copy
	public Vector copy() {
		return new Vector(x, y);
	}

	// Add vector
	public Vector add(Vector v) {
		x += v.x;
		y += v.y;
		return this;
	}

	// Subtract vector
	public Vector sub(Vector v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	// Multiply by scalar
	public Vector mult(double s) {
		x *= s;
		y *= s;
		return this;
	}

	// Multiply by vector
	public Vector mult(Vector v) {
		x *= v.x;
		y *= v.y;
		return this;
	}

	// Divide by scalar
	public Vector div(double s) {
		x /= s;
		y /= s;
		return this;
	}

	// Divide by vector
	public Vector div(Vector v) {
		x /= v.x;
		y /= v.y;
		return this;
	}

	// Calculate distance to vector
	public double dist(Vector v) {
		double dx = x - v.x;
		double dy = y - v.y;
		return (double) Math.sqrt(dx * dx + dy * dy);
	}

	// Translate vector from current box to other box
	public Vector translate(Box from, Box to) {
		Vector scale = to.dimensions().div(from.dimensions());
		return mult(scale);
	}

	// Determine if vector is inside box
	public boolean isInside(Box box) {
		if (x >= box.start.x && x < box.stop.x)
			if (y >= box.start.y && y < box.stop.y)
				return true;
		return false;
	}

	// Mirror on the y axis in relation to box
	public Vector mirrorY(Box box) {
		y = box.dimensions().y - y;
		return this;
	}

	// Convert to absolute values
	public Vector abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}

	// To array
	public double[] toArray() {
		return new double[]{x, y};
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f]", x, y);
	}
}