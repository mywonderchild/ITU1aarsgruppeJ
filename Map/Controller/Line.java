package Map.Controller;

import java.awt.Color;

public class Line
{
	public double[][] coords;
	public Color color;
	public int width;

	public Line(double[][] coords, Color color, int width)
	{
		this.coords = coords;
		this.color = color;
		this.width = width;
	}
}