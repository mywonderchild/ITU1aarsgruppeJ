package Map.Controller;

import java.util.ArrayList;
import java.awt.Color;

import Map.View.Canvas;
import Map.Model.QuadTree;
import Map.Model.Edge;

public class Translator
{
	private Canvas canvas;
	private QuadTree all;
	private QuadTree[] groups;

	/**
	 * Coordinate for deciding the viewed area. This field is added
	 * to the current viewing bounds (negative x will move the
	 * viewing area to the left etc.)
	 * The center coordinates are added in the raw scale of the map.
	 */
	private double[] center;

	/**
	 * Scale for deciding how much of the raw map data to view.
	 * Default value is 1.00 (this would amount to the width and
	 * height of the raw map data.)
	 */
	private double zoomScale;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;

		center = new double[] { 0.0, 0.0 };
		zoomScale = 1.0;
	}

	public ArrayList<Line> getLines()
	{
		/* Since scaling should not be done on both axes (this would
		skew the map), a single axis must be chosen as the scaling
		factor. In the following code, the x-axis is used as the
		scaling axis. */

		// Width of map bounds (full map size)
		double[][] mapBounds = all.getBounds();
		double mapWidth = mapBounds[1][0] - mapBounds[0][0];

		// Width of canvas bounds (size of drawing area in app)
		double canvasWidth = canvas.getWidth();

		// Scale between the width of the canvas and the map.
		double FrameScale = canvasWidth / mapWidth;

		// Raw bounds of viewed area (corresponding to coordinates
		// in the raw map data.)
		double[][] bounds = new double[][] {
			{
				(mapBounds[0][0] + center[0]) * zoomScale,
				(mapBounds[0][1] + center[1]) * zoomScale
			},
			{
				(mapBounds[1][0] + center[0]) * (1 - (zoomScale - 1)),
				(mapBounds[1][1] + center[1]) * (1 - (zoomScale - 1))
			}
		};

		// Actually viewed roads
		ArrayList<Line> lines = new ArrayList<Line>();
		for(QuadTree qt : visibleGroups()) {
			for(Edge edge : qt.queryRange(bounds)) {
				double[][] coords = edge.getCoords();
				double[][] scaledCoords = new double[][] {
					{
						(coords[0][0] - mapBounds[0][0]) * FrameScale,
						(coords[0][1] - mapBounds[0][1]) * FrameScale
					},
					{
						(coords[1][0] - mapBounds[0][0]) * FrameScale,
						(coords[1][1] - mapBounds[0][1]) * FrameScale
					}
				};
				lines.add(new Line(
					scaledCoords,
					getGroupColor(edge.getGroup()),
					1.0
				));
			}
		}

		System.out.printf("(%f, %f), (%f, %f)\n",
			lines.get(0).coords[0][0],
			lines.get(0).coords[0][1],
			lines.get(0).coords[1][0],
			lines.get(0).coords[1][1]
		);

		return lines;
	}

	private QuadTree[] visibleGroups() {
		if (zoomScale <= 1)
			return new QuadTree[]{groups[0], groups[1]};
		else
			return new QuadTree[]{all};
	}

	private Color getGroupColor(int group) throws RuntimeException {

		// Return color
		switch(group) {
			case 0:
				return Color.RED;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.YELLOW;
			case 4:
				return Color.CYAN;
			case 5:
				return Color.BLACK;
			default:
				throw new RuntimeException("Road group " + group + " is not recognized");
		}
	}
}