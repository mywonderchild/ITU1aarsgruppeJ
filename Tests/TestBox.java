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

	@Test
	public void flipX() {
		Box box = new Box(
			new Vector(11, 0),
			new Vector(-3, 0)
		);
		box.flipX();
		assertEquals(box.start.x, -3, delta);
		assertEquals(box.stop.x, 11, delta);
	}

	@Test
	public void flipY() {
		Box box = new Box(
			new Vector(0, 11),
			new Vector(0, -3)
		);
		box.flipY();
		assertEquals(box.start.y, -3, delta);
		assertEquals(box.stop.y, 11, delta);
	}

	@Test
	public void properCorners() {
		Box box = new Box(
			new Vector(51, -10),
			new Vector(0, 80)
		);
		box.properCorners();
		assertEquals(box.start.x, 0, delta);
		assertEquals(box.stop.x, 51, delta);
		assertEquals(box.start.y, -10, delta);
		assertEquals(box.stop.y, 80, delta);
	}

	@Test
	public void overlappingLine() {
		box = new Box(new Vector(1,4), new Vector(3,2));

		Vector[][] lines = new Vector[][] {
			{ new Vector(2,5), new Vector(2,3) }, // top
			{ new Vector(4,3), new Vector(2,3) }, // right
			{ new Vector(2,1), new Vector(2,3) }, // bottom
			{ new Vector(0,3), new Vector(2,3) }, // left
			{ new Vector(2,3), new Vector(2,3) }, // inside, but len = 0
			{ new Vector(0,5), new Vector(1,4) }, // end touches, so inside
			{ new Vector(0,2), new Vector(4,4) },
			{ new Vector(3,1), new Vector(1,5) },
			{ new Vector(1,1), new Vector(1,5) },
			{ new Vector(2,1), new Vector(4,3) }
		};

		for(Vector[] line : lines) {
			boolean overlap = box.overlapping(line[0], line[1]);
			if(!overlap) System.out.println(line[0] + "  ->  " + line[1] + " assertTrue failed");
			assertTrue(overlap);
		}


		Vector[][] wrong = new Vector[][] {
			{ new Vector(3,1), new Vector(4,2) },
			{ new Vector(4,1), new Vector(4,5) },
			{ new Vector(4,2), new Vector(4,4) },
			{ new Vector(4,3), new Vector(4,3) }
		};

		for(Vector[] line : wrong) {
			boolean overlap = box.overlapping(line[0], line[1]);
			if(overlap) System.out.println(line[0] + "  ->  " + line[1] + " assertFalse failed");
			assertFalse(overlap);
		}
	}
}