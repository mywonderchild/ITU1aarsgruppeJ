package Map.Controller;

import java.util.ArrayList;
import java.awt.Color;

import Map.View.Canvas;
import Map.Model.QuadTree;
import Map.Model.Edge;

import Map.Box;
import Map.Vector;

public class Translator
{
	private Canvas canvas;
	private QuadTree all;
	private QuadTree[] groups;

	public Vector center;
	public double zoom;

	private double[][] bounds;
	private double[] boundsDimensions;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;

		// Relative center, {0.5, 0.5} is center of map
		center = new Vector(0.5, 0.5);
		zoom = 2.0;
	}

	public ArrayList<Line> getLines() {

		long timer = System.currentTimeMillis();

		Box modelBox = all.getBox();
		Box canvasBox = canvas.getBox();

		Vector modelCenter = modelBox.relativeToAbsolute(center);

		Vector offset = modelBox.dimensions().div(2).mult(zoom);
		Vector ratio = canvasBox.ratio();
		Vector start = (new Vector(0, 0))
			.sub(offset)
			.mult(ratio)
			.add(modelCenter);
		Vector stop = (new Vector(0, 0))
			.add(offset)
			.mult(ratio)
			.add(modelCenter);
		Box queryBox = new Box(start, stop);

		ArrayList<Line> lines = new ArrayList<Line>();
		
		// Get edges and save lines
		for(QuadTree qt : visibleGroups()) {
			for(Edge edge : qt.queryRange(queryBox.toArray())) {

				Vector[] vectors = edge.getVectors();

				// Translate vectors
				for (int i = 0; i < 2; i++)
					vectors[i] = vectors[i]
						.sub(queryBox.start)
						.translate(queryBox, canvasBox)
						.mirrorY(canvasBox);

				// Add line
				lines.add(new Line(
					vectors[0], vectors[1],
					getGroupColor(edge.getGroup()),
					1.0
				));
			}
		}

		System.out.printf(
			"Getting lines took %d ms\n",
			System.currentTimeMillis() - timer
		);

		return lines;
	}

	private QuadTree[] visibleGroups() {
		if (zoom <= 2)
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