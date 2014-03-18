package Map.Controller;

import java.awt.Color;

import Map.Vector;

public class Line {

	public final Vector start, stop;
	public final Color color;
	public final double width;

	public Line(Vector start, Vector stop, Color color, double width) {
		this.start = start;
		this.stop = stop;
		this.color = color;
		this.width = width;
	}
}