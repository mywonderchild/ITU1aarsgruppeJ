package Map.Controller;

import java.util.ArrayList;

import Map.Box;
import Map.Vector;
import Map.Line;

import Map.Model.Loader;
import Map.Model.QuadTree;
import Map.Model.Edge;

import Map.View.Canvas;

import Map.Model.Groups;

public class Translator {
	private Canvas canvas;
	public QuadTree all;
	private QuadTree[] groups;

	private ArrayList<Line> linePool = new ArrayList<Line>();
	private ArrayList<Edge> edges = new ArrayList<>();
	private ArrayList<Line> lines = new ArrayList<>();

	public Vector center;
	public Vector startCenter;
	public double zoom;
	public double startZoom;

	public Box modelBox;
	public Box canvasBox;
	public Box queryBox;

	private long lastSet;

	public Translator(Canvas canvas, Loader loader) {
		this.canvas = canvas;
		this.all = loader.all;
		this.groups = loader.groups;

		canvas.setLines(lines);

		modelBox = all.getBox();

		// Relative center, {0.5, 0.5} is center of map
		startCenter = new Vector(0.5, 0.5);
		center = startCenter;
		startZoom = 1.3;
		zoom = startZoom;
	}

	public void reset() {
		zoom = startZoom;
		center = startCenter;
		setLines();
	}

	public void setLines() {

		// Don't set lines if last time was less than ~1000/60 ms ago
		if (System.currentTimeMillis() - lastSet < 16) return;
		lastSet = System.currentTimeMillis();

		long timer = System.currentTimeMillis();

		canvasBox = canvas.getBox();

		// Make querybox
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

		// Get edges
		edges.clear();
		for(QuadTree tree : visibleGroups())
			edges.addAll(tree.queryRange(queryBox));

		// Save lines
		lines.clear();
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
				Groups.getGroupColor(Groups.getGroup(edge.TYPE)),
				1.0
			));
		}

		System.out.printf(
			"Setting lines took %d ms\n",
			System.currentTimeMillis() - timer
		);

		canvas.repaint();
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
		if (zoom >= 0.15)
			return new QuadTree[]{groups[0], groups[1], groups[4], groups[5]};
		else if (zoom >= 0.05)
			return new QuadTree[]{groups[0], groups[1], groups[4], groups[5], groups[6]};
		else
			return groups;
	}
}