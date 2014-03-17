package Map.Controller;

import java.awt.Color;

public class Line
{
	public double[][] coords;
	public Color color;
	public double width;

	public Line(double[][] coords, Color color, double width)
	{
		this.coords = coords;
		this.color = color;
		this.width = width;
	}
}