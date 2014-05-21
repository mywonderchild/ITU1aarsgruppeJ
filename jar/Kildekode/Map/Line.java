package Map;

import java.awt.Color;

import Map.Vector;

public class Line {

	public Vector start, stop;
	public Color color;
	public float width;

	public Line set(Vector start, Vector stop, Color color, float width) {
		this.start = start;
		this.stop = stop;
		this.color = color;
		this.width = width;
		return this;
	}
}