package Tests;

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

	// Constructor

	@Test
	public void constructor() {
		// Vector has been created correctly
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	// Set

	@Test
	public void set() {
		vector.set(2, 3);
		// Vector has been modified
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	// Copy

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

	// Addition

	@Test
	public void positiveAdd() {
		vector.add(new Vector(1, 1));
		// Vector has been added correctly
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	@Test
	public void negativeAdd() {
		vector.add(new Vector(-1, -1));
		// Vector has been added correctly
		assertEquals(0, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void zeroAdd() {
		vector.add(new Vector(0, 0));
		// Vector has been added correctly
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	// Subtraction

	@Test
	public void positiveSubtract() {
		vector.sub(new Vector(1, 1));
		// Vector has been subtracted correctly
		assertEquals(0, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void negativeSubtract() {
		vector.sub(new Vector(-1, -1));
		// Vector has been subtracted correctly
		assertEquals(2, vector.x, delta);
		assertEquals(3, vector.y, delta);
	}

	@Test
	public void zeroSubtract() {
		vector.sub(new Vector(0, 0));
		// Vector has been subtracted correctly
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	// Multiplication

	@Test
	public void upscaleMultiplication() {
		vector.mult(2);
		// Vector has been multiplied correctly
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	@Test
	public void downscaleMultiplication() {
		vector.mult(0.5);
		// Vector has been multiplied correctly
		assertEquals(0.5, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void noscaleMultiplication() {
		vector.mult(1);
		// Vector has been multiplied correctly
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	// Division

	@Test
	public void upscaleDivision() {
		vector.div(0.5);
		// Vector has been divided correctly
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	@Test
	public void downscaleDivision() {
		vector.div(2);
		// Vector has been divided correctly
		assertEquals(0.5, vector.x, delta);
		assertEquals(1, vector.y, delta);
	}

	@Test
	public void noscaleDivision() {
		vector.div(1);
		// Vector has been divided correctly
		assertEquals(1, vector.x, delta);
		assertEquals(2, vector.y, delta);
	}

	// Distance

	@Test
	public void positiveDistance() {
		// Distance has been calculated correctly
		assertEquals(Math.sqrt(2), vector.dist(new Vector(2, 3)), delta);
	}

	@Test
	public void negativeDistance() {
		// Distance has been calculated correctly
		assertEquals(Math.sqrt(2), vector.dist(new Vector(0, 1)), delta);
	}

	// Translate

	@Test
	public void translate() {
		Box from = new Box(new Vector(0, 0), new Vector(2, 4));
		Box to = new Box(new Vector(0, 0), new Vector(4, 8));
		vector.translate(from, to);
		assertEquals(2, vector.x, delta);
		assertEquals(4, vector.y, delta);
	}

	// Mirror

	@Test
	public void mirrorY() {
		Box box = new Box(new Vector(0, 0), new Vector(1, 3));
		vector.mirrorY(box);
		assertEquals(1, vector.y, delta);
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