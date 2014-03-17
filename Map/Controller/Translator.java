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
	public double[] center;

	/**
	 * Scale for deciding how much of the raw map data to view.
	 * Default value is 1.00 (this would amount to the width and
	 * height of the raw map data.)
	 */
	public double zoomScale;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;

		center = new double[] {0.5, 0.5};
		zoomScale = 2.0;
	}

	public ArrayList<Line> getLines()
	{
		/* Since scaling should not be done on both axes (this would
		skew the map), a single axis must be chosen as the scaling
		factor. In the following code, the x-axis is used as the
		scaling axis. */

		/* SCALING AXIS SHOULD PROBABLY BE CHOSEN AS THE LONGEST
		(or shortest?) CANVAS AXIS TO PREVENT CUTOFF. */

		// Width of map bounds (full map size)
		double[][] mapBounds = all.getBounds();
		double[] mapDimensions = new double[]{
			mapBounds[1][0] - mapBounds[0][0],
			mapBounds[1][1] - mapBounds[0][1]
		};

		// Width of canvas bounds (size of drawing area in app)
		double[] canvasDimensions = new double[]{
			canvas.getWidth(),
			canvas.getHeight()
		};

		// Biggest axe
		int biggest = (canvasDimensions[0] > canvasDimensions[1]) ? 0 : 1;
		int smallest = (biggest == 1) ? 0 : 1;
		double ratio = canvasDimensions[smallest] / canvasDimensions[biggest];

		// Raw bounds of viewed area (corresponding to coordinates
		// in the raw map data.)
		double[] mapCenter = new double[]{
			center[0] * mapDimensions[0] + mapBounds[0][0],
			center[1] * mapDimensions[1] + mapBounds[0][1]
		};

		// UGLY CODE HERE AGAIN
		double[][] bounds;
		if (smallest == 1) {
			bounds = new double[][] {
				{
					mapCenter[0] - (mapDimensions[0] / 2) * zoomScale,
					mapCenter[1] - (mapDimensions[1] / 2) * zoomScale * ratio
				},
				{
					mapCenter[0] + (mapDimensions[0] / 2) * zoomScale,
					mapCenter[1] + (mapDimensions[1] / 2) * zoomScale * ratio
				}
			};
		} else {
			bounds = new double[][] {
				{
					mapCenter[0] - (mapDimensions[0] / 2) * zoomScale * ratio,
					mapCenter[1] - (mapDimensions[1] / 2) * zoomScale
				},
				{
					mapCenter[0] + (mapDimensions[0] / 2) * zoomScale * ratio,
					mapCenter[1] + (mapDimensions[1] / 2) * zoomScale
				}
			};
		}
		// UGLY CODE ENDS HERE AGAIN

		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 2; j++)
				System.out.println(bounds[i][j]);

		// Actually viewed roads
		ArrayList<Line> lines = new ArrayList<Line>();
		for(QuadTree qt : visibleGroups()) {
			for(Edge edge : qt.queryRange(bounds)) {

				// THIS IS FUCKED UP AND UNOPTIMIZED; FIX THE NUMBER OF OBJECTS USED AND REDUCE RUNTHROUGHS GODAMMIT
				double[][] coords = edge.getCoords();
				double[][] scaledCoords = new double[][] {
					{
						(coords[0][0] - bounds[0][0]) / (bounds[1][0] - bounds[0][0]) * canvasDimensions[0],
						(coords[0][1] - bounds[0][1]) / (bounds[1][1] - bounds[0][1]) * canvasDimensions[1]
					},
					{
						(coords[1][0] - bounds[0][0]) / (bounds[1][0] - bounds[0][0]) * canvasDimensions[0],
						(coords[1][1] - bounds[0][1]) / (bounds[1][1] - bounds[0][1]) * canvasDimensions[1]
					}
				};
				// Reverse y-axis
				for (int i = 0; i < 2; i++)
					scaledCoords[i][1] = canvasDimensions[1] - scaledCoords[i][1];
				// HERE ENDS THE SHIT

				lines.add(new Line(
					scaledCoords,
					getGroupColor(edge.getGroup()),
					1.0
				));
			}
		}

		return lines;
	}

	private QuadTree[] visibleGroups() {
		if (zoomScale <= 2)
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