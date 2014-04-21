package Map.View;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;

import java.awt.geom.Rectangle2D;

import java.lang.Iterable;
import java.util.ArrayList;

import Map.Box;
import Map.Vector;
import Map.Line;
import Map.Model.Loader;
import Map.Model.QuadTree;
import Map.Model.Groups;
import Map.Model.Edge;
import Map.View.Painter;

public class Tiler {

	public Box mapBox, viewBox, modelBox, section;
	public int tileSize;
	public BufferedImage[][] tiles;
	public double zoom, resetZoom, minZoom = 0.005, maxZoom = 1.5;
	public Vector center, resetCenter;
	public QuadTree all;
	public Iterable<Edge> path;
	private QuadTree[] groups;
	private ArrayList<Line> linePool = new ArrayList<Line>();
	private BufferedImage renderTile;
	private Graphics2D render;

	public Tiler(double zoom, Vector center, Box viewBox, Box modelBox, Loader loader) {
		this.center = center;
		this.viewBox = viewBox;
		this.modelBox = modelBox;
		resetCenter = center;
		resetZoom = zoom;
		setZoom(zoom);
		if (loader != null) {
			this.all = loader.all;
			this.groups = loader.groups;
		}
	}

	public void setZoom(double zoom) {

		if (zoom > maxZoom) zoom = maxZoom;
		if (zoom < minZoom) zoom = minZoom;
		this.zoom = zoom;

		Vector viewDimensions = viewBox.dimensions();
		tileSize = (int)(Math.sqrt(viewDimensions.x * viewDimensions.y) / 4);

		renderTile = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
		render = renderTile.createGraphics();
		render.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Vector mapDimensions = viewDimensions
			.div(viewBox.ratio())
			.div(zoom)
			.mult(modelBox.ratio());
		mapBox = new Box(new Vector(0, 0), mapDimensions);

		int tilesX = (int)Math.ceil(mapDimensions.x / tileSize);
		int tilesY = (int)Math.ceil(mapDimensions.y / tileSize);
		tiles = new BufferedImage[tilesX][tilesY];
	}

	public void reset() {
		this.center = resetCenter;
		setZoom(resetZoom);
	}

	public void render(Graphics2D graphics) {
		section = getSection();
		int[][] sectionTiles = getTiles(section);
		for (int[] tile : sectionTiles) {
			int x = tile[0];
			int y = tile[1];
			renderTile(x, y);
			graphics.drawImage(
				tiles[x][y],
				null,
				x * tileSize - (int)section.start.x,
				y * tileSize - (int)section.start.y
			);
		}
		renderPath(graphics);
	}

	public void renderPath(Graphics2D graphics) {
		if(path == null) return;

		ArrayList<Line> lines = new ArrayList<Line>();
		for(Edge edge : path) {
			lines.add(new Line().set(
				translateToView(edge.START.VECTOR.copy()),
				translateToView(edge.END.VECTOR.copy()),
				Color.MAGENTA,
				2
			));
		}
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Painter.paintLines(graphics, lines);
	}

	public Box getSection() {
		Box section = viewBox.copy();
		Vector offset = center.copy()
			.mult(mapBox.dimensions())
			.sub(viewBox.dimensions().div(2));
		section.start.add(offset);
		section.stop.add(offset);
		return section;
	}

	public int[][] getTiles(Box section) {

		// Determine which tiles are inside section
		int[][] query = new int[2][2];
		query[0][0] = Math.max((int)(section.start.x / tileSize), 0);
		query[0][1] = Math.max((int)(section.start.y / tileSize), 0);
		query[1][0] = Math.min((int)Math.ceil(section.stop.x / tileSize), tiles.length);
		query[1][1] = Math.min((int)Math.ceil(section.stop.y / tileSize), tiles[0].length);

		// Return the tiles
		int width = query[1][0] - query[0][0];
		int height = query[1][1] - query[0][1];
		int[][] selected = new int[width * height][2];
		int cursor = 0;
		for (int x = query[0][0]; x < query[1][0]; x++) {
			for (int y = query[0][1]; y < query[1][1]; y++) {
				selected[cursor][0] = x;
				selected[cursor][1] = y;
				cursor++;
			}
		}
		return selected;
	}

	public void renderTile(int x, int y) {

		if (tiles[x][y] != null) return;

		Box tileBox = getTileBox(x, y);
		Box queryBox = getQueryBox(tileBox.copy());

		ArrayList<Edge> edges = new ArrayList<>();
		for(QuadTree tree : visibleGroups())
			edges.addAll(tree.queryRange(queryBox));

		ArrayList<Line> lines = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			Vector[] vectors = edge.getVectors();
			for (Vector vector : vectors)
				vector = translateToTile(vector, tileBox);
			if (i >= linePool.size()) linePool.add(new Line());
			lines.add(linePool.get(i).set(
				vectors[0], vectors[1],
				Groups.getColor(edge),
				1
			));
		}
		
		render.setColor(Color.WHITE);
		render.fillRect(0, 0, tileSize, tileSize);
		Painter.paintLines(render, lines);

		ColorModel colorModel = renderTile.getColorModel();
		boolean premultiplied = colorModel.isAlphaPremultiplied();
		WritableRaster raster = renderTile.copyData(null);
		tiles[x][y] = new BufferedImage(colorModel, raster, premultiplied, null);
	}

	public Box getTileBox(int x, int y) {
		Vector start = new Vector(x * tileSize, y * tileSize);
		Vector stop = new Vector(start.x + tileSize, start.y + tileSize);
		return new Box(start, stop);
	}

	public Box getQueryBox(Box tileBox) {
		tileBox.translate(mapBox, modelBox);
		tileBox.start.add(modelBox.start);
		tileBox.stop.add(modelBox.start);
		return tileBox;
	}

	public Vector translateToTile(Vector vector, Box tileBox) {
		return vector
			.translate(modelBox, mapBox)
			.sub(tileBox.start);
	}

	public Vector translateToModel(Vector vector) {
		return vector
			.add(section.start)
			.translate(mapBox, modelBox);
	}

	public Vector translateToView(Vector vector) {
		return vector
			.translate(modelBox, mapBox)
			.sub(section.start);
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