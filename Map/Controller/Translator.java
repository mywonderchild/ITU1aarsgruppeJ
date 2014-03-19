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
	public QuadTree all;
	private QuadTree[] groups;

	public ArrayList<Line> linePool = new ArrayList<Line>();

	public Vector center;
	public double zoom;

	public Box modelBox;
	public Box canvasBox;
	public Box queryBox;

	public Translator(Canvas canvas, QuadTree all, QuadTree[] groups) {
		this.canvas = canvas;
		this.all = all;
		this.groups = groups;

		modelBox = all.getBox();

		// Relative center, {0.5, 0.5} is center of map
		center = new Vector(0.5, 0.5);
		zoom = 5.0;
	}

	public ArrayList<Line> getLines() {

		long timer = System.currentTimeMillis();

		canvasBox = canvas.getBox();

		Vector modelCenter = modelBox.relativeToAbsolute(center);

		Vector offset = modelBox.dimensions()
			.div(2)
			.mult(zoom)
			.div(modelBox.ratio());
		Vector ratio = canvasBox.ratio();
		Vector start = (new Vector(0, 0))
			.sub(offset)
			.mult(ratio)
			.add(modelCenter);
		Vector stop = (new Vector(0, 0))
			.add(offset)
			.mult(ratio)
			.add(modelCenter);
		queryBox = new Box(start, stop);
		
		ArrayList<Edge> edges = new ArrayList<>();

		// Get edges
		for(QuadTree tree : visibleGroups())
			edges.addAll(tree.queryRange(queryBox.toArray()));

		// Save lines
		ArrayList<Line> lines = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {

			Edge edge = edges.get(i);
			Vector[] vectors = edge.getVectors();

			// Translate vectors
			for (int j = 0; j < 2; j++)
				vectors[j] = translateToView(vectors[j]);

			// Add new line to linepool if needed
			if (i >= linePool.size()) linePool.add(new Line());

			lines.add(linePool.get(i).set(
				vectors[0], vectors[1],
				getGroupColor(edge.getGroup()),
				1.0
			));
		}

		System.out.printf(
			"Getting lines took %d ms\n",
			System.currentTimeMillis() - timer
		);

		return lines;
	}

	Vector translateToView(Vector vector) {
		return vector
			.sub(queryBox.start)
			.translate(queryBox, canvasBox)
			.mirrorY(canvasBox);
	}

	Vector translateToModel(Vector vector) {
		return vector
			.mirrorY(canvasBox)
			.translate(canvasBox, queryBox)
			.add(queryBox.start);
	}

	private QuadTree[] visibleGroups() {
		if (zoom <= 10)
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