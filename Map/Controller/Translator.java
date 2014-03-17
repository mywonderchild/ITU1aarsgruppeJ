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

	public double[] center;
	public double zoomScale;

	private double[] canvasDimensions;

	private double[][] bounds;
	private double[] boundsDimensions;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;

		// Relative center, {0.5, 0.5} is center of map
		center = new double[] {0.5, 0.5};
<<<<<<< Updated upstream

		// Relative zoom, 1.5 is 150% of map
		zoomScale = 1.5;
=======
		zoomScale = 1.0;
>>>>>>> Stashed changes
	}

	public ArrayList<Line> getLines()
	{
		// Load bounds and dimensions of model
		double[][] modelBounds = all.getBounds();
		double[] modelDimensions = new double[2];
		for (int i = 0; i < 2; i++)
			modelDimensions[i] = modelBounds[1][i] - modelBounds[0][i];

		// Load dimensions of canvas
		canvasDimensions = new double[] {
			canvas.getWidth(),
			canvas.getHeight()
		};

		// Determine biggest and smallest axis (0 is x and y is 1)
		int biggest = (canvasDimensions[0] > canvasDimensions[1]) ? 0 : 1;
		int smallest = (biggest == 1) ? 0 : 1;

		// Ratio – always the relationship between smallest and largest axis
		double canvasRatio = canvasDimensions[smallest] / canvasDimensions[biggest];

		// Absolute coordinates of center
		double[] modelCenter = new double[]{
			center[0] * modelDimensions[0] + modelBounds[0][0],
			center[1] * modelDimensions[1] + modelBounds[0][1]
		};

		// Absolute coordinates of bounds in map with zoom and center applied
		bounds = new double[2][2];
		double[] offset = new double[]{
			(modelDimensions[0] / 2) * zoomScale,
			(modelDimensions[1] / 2) * zoomScale
		};
		if (smallest == 1) {
			bounds[0][0] = modelCenter[0] - offset[0];
			bounds[0][1] = modelCenter[1] - offset[1] * canvasRatio;
			bounds[1][0] = modelCenter[0] + offset[0];
			bounds[1][1] = modelCenter[1] + offset[1] * canvasRatio;
		} else {
			bounds[0][0] = modelCenter[0] - offset[0] * canvasRatio;
			bounds[0][1] = modelCenter[1] - offset[1];
			bounds[1][0] = modelCenter[0] + offset[0] * canvasRatio;
			bounds[1][1] = modelCenter[1] + offset[1];
		}

		// Bounds dimensions
		boundsDimensions = new double[]{
			bounds[1][0] - bounds[0][0],
			bounds[1][1] - bounds[0][1]
		};

		// for(int i = 0; i < 2; i++)
		// 	for(int j = 0; j < 2; j++)
		// 		System.out.println(bounds[i][j]);

		// Actually viewed roads
		ArrayList<Line> lines = new ArrayList<Line>();
		for(QuadTree qt : visibleGroups()) {
			for(Edge edge : qt.queryRange(bounds)) {

				// Translate coordinates
				double[][] coords = edge.getCoords();
				for (int i = 0; i < 2; i++)
					coords[i] = translateToView(coords[i]);

				// Add line
				lines.add(new Line(
					coords,
					getGroupColor(edge.getGroup()),
					1.0
				));
			}
		}

		return lines;
	}

	double[] translateToView(double[] coord) {

		for (int i = 0; i < 2; i++) {
			coord[i] -= bounds[0][i];
			coord[i] /= boundsDimensions[i];
			coord[i] *= canvasDimensions[i];
		}

		// Reverse y axis
		coord[1] = canvasDimensions[1] - coord[1];

		return coord;
	}

	double[] translateToModel(double[] coord) {

		// This is just the reverse of translate to view...

		// Reverse y axis
		coord[1] = canvasDimensions[1] - coord[1];

		for (int i = 0; i < 2; i++) {
			coord[i] /= canvasDimensions[i];
			coord[i] *= boundsDimensions[i];
			coord[i] += bounds[0][i];
		}

		return coord;
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