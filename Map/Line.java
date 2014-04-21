package Map;

import java.awt.Color;

import Map.Vector;

public class Line {

	public Vector start, stop;
	public Color color;
	public int width;

	public Line set(Vector start, Vector stop, Color color, int width) {
		this.start = start;
		this.stop = stop;
		this.color = color;
		this.width = width;
		return this;
	}
}