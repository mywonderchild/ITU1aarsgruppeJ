package Tests;

import static org.junit.Assert.*;
import org.junit.*;

import Map.Box;
import Map.Vector;

public class TestBox {

	Box box;
	double delta = 1e-3;

	@Before
	public void setup() {
		box = new Box(
			new Vector(1,2),
			new Vector(3,4)
		);
	}

	// Constructor

	@Test
	public void constructor() {
		assertEquals(box.start.x, 1, delta);
		assertEquals(box.start.y, 2, delta);
		assertEquals(box.stop.x, 3, delta);
		assertEquals(box.stop.y, 4, delta);
	}

	// Dimensions

	@Test
	public void dimensions() {
		Vector dimensions = box.dimensions();
		assertEquals(dimensions.x, 2, delta);
		assertEquals(dimensions.y, 2, delta);
	}

	// Relative to absolute

	@Test
	public void relativeToAbsolute() {
		Vector relative = new Vector(0.5, 0.5);
		Vector absolute = box.relativeToAbsolute(relative);
		assertEquals(absolute.x, 2, delta);
		assertEquals(absolute.y, 3, delta);
	}

	// Ratio

	@Test
	public void ratioSquare() {
		Vector ratio = box.ratio();
		assertEquals(ratio.x, 1, delta);
		assertEquals(ratio.y, 1, delta);
	}

	@Test
	public void ratioWide() {
		box = new Box(new Vector(0, 0), new Vector(2, 1));
		Vector ratio = box.ratio();
		assertEquals(ratio.x, 1, delta);
		assertEquals(ratio.y, 0.5, delta);
	}

	@Test
	public void ratioNarrow() {
		box = new Box(new Vector(0, 0), new Vector(1, 2));
		Vector ratio = box.ratio();
		assertEquals(ratio.x, 0.5, delta);
		assertEquals(ratio.y, 1, delta);
	}

	// Scale
	@Test
	public void scale() {
		box.scale(2);
		assertEquals(box.start.x, 0, delta);
		assertEquals(box.start.y, 1, delta);
		assertEquals(box.stop.x, 4, delta);
		assertEquals(box.stop.y, 5, delta);
	}
}