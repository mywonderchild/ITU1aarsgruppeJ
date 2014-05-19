package Map.Tests;

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

	@Test
	public void constructor() {
		assertEquals(1, box.start.x, delta);
		assertEquals(2, box.start.y, delta);
		assertEquals(3, box.stop.x, delta);
		assertEquals(4, box.stop.y, delta);
	}

	@Test
	public void dimensions() {
		Vector dimensions = box.dimensions();
		assertEquals(2, dimensions.x, delta);
		assertEquals(2, dimensions.y, delta);
	}

	@Test
	public void relativeToAbsolute() {
		Vector relative = new Vector(0.5, 0.5);
		Vector absolute = box.relativeToAbsolute(relative);
		assertEquals(2, absolute.x, delta);
		assertEquals(3, absolute.y, delta);
	}

	@Test
	public void absoluteToRelative() {
		Vector absolute = new Vector(2, 3);
		Vector relative = box.absoluteToRelative(absolute);
		assertEquals(0.5, relative.x, delta);
		assertEquals(0.5, relative.y, delta);
	}

	@Test
	public void ratioSquare() {
		Vector ratio = box.ratio();
		assertEquals(1, ratio.x, delta);
		assertEquals(1, ratio.y, delta);
	}

	@Test
	public void ratioWide() {
		box = new Box(new Vector(0, 0), new Vector(2, 1));
		Vector ratio = box.ratio();
		assertEquals(1, ratio.x, delta);
		assertEquals(0.5, ratio.y, delta);
	}

	@Test
	public void ratioNarrow() {
		box = new Box(new Vector(0, 0), new Vector(1, 2));
		Vector ratio = box.ratio();
		assertEquals(0.5, ratio.x, delta);
		assertEquals(1, ratio.y, delta);
	}

	// TRANSLATE

	@Test
	public void overlapping() {
		Box overlapping = new Box(new Vector(0.5, 1.5), new Vector(1.5, 2.5));
		Box notOverlapping = new Box(new Vector(0.5, 1.5), new Vector(0.99, 1.99));
		assertTrue(box.overlapping(overlapping));
		assertFalse(box.overlapping(notOverlapping));
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

	@Test
	public void getCenter() {
		Vector center = box.getCenter();
		assertEquals(2, center.x, delta);
		assertEquals(3, center.y, delta);
	}

	@Test
	public void scale() {
		box.scale(2);
		assertEquals(0, box.start.x, delta);
		assertEquals(1, box.start.y, delta);
		assertEquals(4, box.stop.x, delta);
		assertEquals(5, box.stop.y, delta);
	}

	@Test
	public void properCorners() {
		Box box = new Box(
			new Vector(51, -10),
			new Vector(0, 80)
		);
		box.properCorners();
		assertEquals(0, box.start.x, delta);
		assertEquals(51, box.stop.x, delta);
		assertEquals(-10, box.start.y, delta);
		assertEquals(80, box.stop.y, delta);
	}

	@Test
	public void flipX() {
		Box box = new Box(
			new Vector(11, 0),
			new Vector(-3, 0)
		);
		box.flipX();
		assertEquals(-3, box.start.x, delta);
		assertEquals(11, box.stop.x, delta);
	}

	@Test
	public void flipY() {
		Box box = new Box(
			new Vector(0, 11),
			new Vector(0, -3)
		);
		box.flipY();
		assertEquals(-3, box.start.y, delta);
		assertEquals(11, box.stop.y, delta);
	}

	@Test
	public void grow() {
		box.grow(1);
		assertEquals(0, box.start.x, delta);
		assertEquals(1, box.start.y, delta);
		assertEquals(4, box.stop.x, delta);
		assertEquals(5, box.stop.y, delta);
	}

	@Test
	public void isInside() {
		Box inside = new Box(new Vector(0.5, 1.5), new Vector(3.5, 4.5));
		Box outside = new Box(new Vector(1.5, 2.5), new Vector(2.5, 3.5));
		assertTrue(box.isInside(inside));
		assertFalse(box.isInside(outside));
	}
}