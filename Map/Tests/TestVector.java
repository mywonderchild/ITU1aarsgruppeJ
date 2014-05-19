package Map.Tests;

import static org.junit.Assert.*;
import org.junit.*;

import Map.Vector;
import Map.Box;

public class TestVector {

	Vector vector;
	double delta = 1e-3;

	@Before
	public void setup() {
		vector = new Vector(1, 2);
	}

	@Test
	public void constructor() {
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	@Test
	public void set() {
		vector.set(2, 3);
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	@Test
	public void copy() {
		Vector copy = vector.copy().set(2, 3);
		// Original hasn't been modified
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
		// Copy has been created correctly
		assertEquals(2, copy.x, delta);
		assertEquals(3, copy.y, delta);
	}

	@Test
	public void positiveAdd() {
		vector.add(new Vector(1, 1));
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	@Test
	public void negativeAdd() {
		vector.add(new Vector(-1, -1));
		assertEquals(0, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void zeroAdd() {
		vector.add(new Vector(0, 0));
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	@Test
	public void positiveSubtract() {
		vector.sub(new Vector(1, 1));
		assertEquals(0, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void negativeSubtract() {
		vector.sub(new Vector(-1, -1));
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	@Test
	public void zeroSubtract() {
		vector.sub(new Vector(0, 0));
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	@Test
	public void upscaleMultiplication() {
		vector.mult(2);
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	@Test
	public void downscaleMultiplication() {
		vector.mult(0.5);
		assertEquals(0.5, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void noscaleMultiplication() {
		vector.mult(1);
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	@Test
	public void upscaleDivision() {
		vector.div(0.5);
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	@Test
	public void downscaleDivision() {
		vector.div(2);
		assertEquals(0.5, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void noscaleDivision() {
		vector.div(1);
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	@Test
	public void positiveDistance() {
		assertEquals(Math.sqrt(2), vector.dist(new Vector(2, 3)), delta);
	}

	@Test
	public void negativeDistance() {
		assertEquals(Math.sqrt(2), vector.dist(new Vector(0, 1)), delta);
	}

	@Test
	public void zeroDistance() {
		assertEquals(0, vector.dist(new Vector(1, 2)), delta);
	}

	@Test
	public void translate() {
		Box from = new Box(new Vector(0, 0), new Vector(2, 4));
		Box to = new Box(new Vector(0, 0), new Vector(4, 8));
		vector.translate(from, to);
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	@Test
	public void isInside() {
		Box container = new Box(new Vector(0, 0), new Vector(1, 1));
		Vector[] inside = new Vector[] {
			new Vector(0, 0),
			new Vector(0.5, 0.5),
			new Vector(1, 1)
		};
		Vector[] outside = new Vector[] {
			new Vector(0, 1.01),
			new Vector(1.01, 0),
			new Vector(1.01, 1.01)
		};
		for (Vector vector : inside) assertTrue(vector.isInside(container));
		for (Vector vector : outside) assertFalse(vector.isInside(container));
	}

	@Test
	public void mirrorY() {
		Box box = new Box(new Vector(0, 0), new Vector(1, 3));
		vector.mirrorY(box);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void abs() {
		Vector vector = new Vector(-1, -1);
		vector.abs();
		assertEquals(1, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	// DOT

	// CROSS 

	// MAG

	@Test
	public void norm() {
		Vector vector = new Vector(10, 0);
		vector.norm();
		assertEquals(1, vector.x, delta);
		assertEquals(0, vector.y, delta);
		assertEquals(1, vector.mag(), delta);
	}

	@Test
	public void angleBetween() {
		Vector straight = new Vector(1, 2);
		Vector right = new Vector(2, -1);
		Vector opposite = new Vector(-1, -2);
		Vector left = new Vector(-2, 1);
		assertEquals(0, vector.angle(straight), delta);
		assertEquals(-Math.PI / 2, vector.angle(right), delta);
		assertEquals(Math.PI, vector.angle(opposite), delta);
		assertEquals(Math.PI / 2, vector.angle(left), delta);
	}

	@Test
	public void equals() {
		Vector v1 = new Vector(7, 21);
		Vector v2 = new Vector(7, 21);
		Vector v3 = new Vector(21, 7);
		Vector v4 = new Vector(0, -90);
		assertEquals(true, v1.equals(v2));
		assertEquals(true, v2.equals(v1));
		assertEquals(false, v1.equals(v3));
		assertEquals(false, v1.equals(v4));
	}
}