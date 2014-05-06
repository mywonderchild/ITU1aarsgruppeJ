package Tests;

import static org.junit.Assert.*;
import org.junit.*;

import Map.Vector;
import Map.Box;
import Map.View.Tiler;

public class TestTiler {

	double delta = 1e-3;
	Tiler tiler;
	Box viewBox = new Box(
		new Vector(0, 0),
		new Vector(250, 125)
	);
	Box modelBox = new Box(
		new Vector(0, 0),
		new Vector(3, 2)
	);

	@Before
	public void setup() {
		double zoom = 0.5;
		Vector center = new Vector(0.5, 0.5);
		tiler = new Tiler(zoom, center, viewBox, modelBox, null);
	}

	@Test
	public void reset() {
		Vector dimensions = tiler.mapBox.dimensions();
		assertEquals(500, dimensions.x, 0.5);
		assertEquals(333, dimensions.y, 0.5);
		assertEquals(2, tiler.tiles.length);
		assertEquals(2, tiler.tiles[0].length);
	}

	@Test
	public void getSection() {
		Box section = tiler.getSection();
		assertEquals(125, section.start.x, 0.5);
		assertEquals(104, section.start.y, 0.5);
		assertEquals(375, section.stop.x, 0.5);
		assertEquals(229, section.stop.y, 0.5);
	}

	@Test
	public void getTiles() {
		Box section = new Box(new Vector(0, 0), new Vector(0, 0));
		int[][] expectedOne = new int[][]{{0, 0}};
		int[][] expectedFour = new int[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};

		// Section just below boundary that gets 1 tile
		section.stop.x = 250;
		section.stop.y = 250;
		assertArrayEquals(expectedOne, tiler.getTiles(section));

		// Section just over boundary that gets 4 tiles
		section.stop.x = 251;
		section.stop.y = 251;
		assertArrayEquals(expectedFour, tiler.getTiles(section));

		// Very small section (2 x 2) that gets 4 tiles
		section.start.x = 249;
		section.start.y = 249;
		assertArrayEquals(expectedFour, tiler.getTiles(section));
	}

	@Test
	public void getTileBox() {
		Box tileBox = tiler.getTileBox(0, 0);
		assertEquals(0, tileBox.start.x, delta);
		assertEquals(0, tileBox.start.y, delta);
		assertEquals(250, tileBox.stop.x, delta);
		assertEquals(250, tileBox.stop.y, delta);
	}

	@Test
	public void getQueryBox() {
		Box tileBox = tiler.getTileBox(0, 0);
		Box queryBox = tiler.getQueryBox(tileBox);
		assertEquals(0, queryBox.start.x, delta);
		assertEquals(0, queryBox.start.y, delta);
		assertEquals(1.5, queryBox.stop.x, delta);
		assertEquals(1.5, queryBox.stop.y, delta);
	}

	@Test
	public void translateToRectangle() {
		Box tileBox = tiler.getTileBox(0, 0);
		Vector vector = new Vector(0.5, 0.5);
		Vector translated = tiler.translateToRectangle(vector, tileBox);
		assertEquals(83.33, translated.x, 0.5);
		assertEquals(83.33, translated.y, 0.5);
	}
}