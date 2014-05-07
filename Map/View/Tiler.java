package Map.View;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
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
import Map.Controller.Path;

public class Tiler {

	public Box mapBox, viewBox, modelBox, section;
	public int tileSize;
	public BufferedImage[][] tiles;
	public double zoom, resetZoom, minZoom = 0.005, maxZoom = 1.5;
	public Vector center, resetCenter;
	public QuadTree all;
	public Path path;
	private QuadTree[] groups;
	private ArrayList<Line> linePool = new ArrayList<Line>();
	private BufferedImage buffer;
	private Graphics2D bufferGraphics;

	public Tiler(double zoom, Vector center, Box viewBox, Box modelBox, Loader loader) {
		this.center = center;
		this.viewBox = viewBox;
		this.modelBox = modelBox;
		resetCenter = center.copy();
		resetZoom = zoom;
		setZoom(zoom);
		if (loader != null) {
			this.all = loader.all;
			this.groups = loader.groups;
		}
	}

	public void setZoom(double zoom) {

		this.zoom = Math.min(Math.max(zoom, minZoom), maxZoom);

		Vector viewDimensions = viewBox.dimensions();
		tileSize = (int)(Math.sqrt(viewDimensions.x * viewDimensions.y) / 4);

		buffer = new BufferedImage((int)viewDimensions.x + tileSize * 2, (int)viewDimensions.y + tileSize * 2, BufferedImage.TYPE_INT_RGB);
		bufferGraphics = buffer.createGraphics();
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Vector mapDimensions = viewDimensions
			.div(viewBox.ratio())
			.div(this.zoom)
			.mult(modelBox.ratio());
		mapBox = new Box(new Vector(0, 0), mapDimensions);

		int tilesX = (int)Math.ceil(mapDimensions.x / tileSize);
		int tilesY = (int)Math.ceil(mapDimensions.y / tileSize);
		tiles = new BufferedImage[tilesX][tilesY];
	}

	public void fakeRender(Graphics2D graphics) {


	}

	public void reset() {
		this.center = resetCenter;
		setZoom(resetZoom);
	}

	public void render(Graphics2D graphics) {
		section = getSection();
		int[][] sectionTiles = getTiles(section);
		for (int[][] rectangle : getRectangles(sectionTiles))
			renderRectangle(rectangle);
		for (int[] tile : sectionTiles) {
			int x = tile[0];
			int y = tile[1];
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
		for(Edge edge : path.edges) {
			lines.add(new Line().set(
				translateToView(edge.START.VECTOR.copy()),
				translateToView(edge.END.VECTOR.copy()),
				Color.MAGENTA,
				(float)(1.5*(1+(0.05*(1.5/zoom))))
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
		if (width <= 0 || height <= 0) return new int[0][0];
		int[][] selected = new int[width * height][2];
		int cursor = 0;
		for (int y = query[0][1]; y < query[1][1]; y++) {
			for (int x = query[0][0]; x < query[1][0]; x++) {
				selected[cursor][0] = x;
				selected[cursor][1] = y;
				cursor++;
			}
		}
		return selected;
	}

	public ArrayList<int[][]> getRectangles(int[][] selected) {

		// Find the dimensions of the section of tiles
		int[] start = selected[0];
		int[] stop = selected[selected.length - 1];
		int[] dimensions = new int[] {
			stop[0] - start[0] + 1,
			stop[1] - start[1] + 1,
		};

		// Make a map over the render state of tiles
		boolean[][] rendered = new boolean[dimensions[0]][dimensions[1]];
		for (int i = 0; i < dimensions[1]; i++)
			for (int j = 0; j < dimensions[0]; j++)
				rendered[j][i] = tiles[j + start[0]][i + start[1]] != null ? true : false;

		// Prepare for rectangle detection algorithm
		int[][] horizontal = new int[dimensions[0]][dimensions[1]];
		int[][] vertical = new int[dimensions[0]][dimensions[1]];
		ArrayList<int[][]> rectangles = new ArrayList<>();
		int max = 1, x = 0, y = 0;

		// Run until there are no more rectangles left
		while (true) {

			// Build map of horizontal and vertical values of rectangles dynamically
			for (int i = dimensions[1] - 1; i >= 0; i--)
				for (int j = dimensions[0] - 1; j >= 0; j--)
					if (!rendered[j][i]) {
						horizontal[j][i] = 1;
						if (j != dimensions[0] - 1) horizontal[j][i] += horizontal[j + 1][i];
						vertical[j][i] = 1;
						if (i != dimensions[1] - 1) vertical[j][i] += vertical[j][i + 1];
					} else {
						horizontal[j][i] = 0;
						vertical[j][i] = 0;
					}

			// Find the largest rectangle
			max = 0;
			for (int i = 0; i < dimensions[1]; i++)
				for (int j = 0; j < dimensions[0]; j++)
					if (horizontal[j][i] * vertical[j][i] > max) {
						max = horizontal[j][i] * vertical[j][i];
						x = j;
						y = i;
					}

			// Save rectangle if one is found, break otherwise
			if (max > 0) {
				for (int i = y; i < y + vertical[x][y]; i++)
					for (int j = x; j < x + horizontal[x][y]; j++)
						rendered[j][i] = true;
				rectangles.add(new int[][] {
					{start[0] + x, start[1] + y},
					{horizontal[x][y], vertical[x][y]}
				});
			} else {
				break;
			}
		}

		return rectangles;
	}

	public void renderRectangle(int[][] rectangle) {

		Box rectangleBox = getRectangleBox(rectangle);
		Box queryBox = getQueryBox(rectangleBox.copy());

		// Get edges from QT's
		ArrayList<Edge> edges = new ArrayList<>();
		QuadTree[] visible = visibleGroups();
		for(int i = visible.length-1; i >= 0; i--)
			edges.addAll(visible[i].queryRange(queryBox));

		// Make lines
		ArrayList<Line> lines = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			Vector[] vectors = edge.getVectors();
			for (Vector vector : vectors)
				vector = translateToRectangle(vector, rectangleBox);
			if (i >= linePool.size()) linePool.add(new Line());
			lines.add(linePool.get(i).set(
				vectors[0], vectors[1],
				Groups.getColor(edge),
				lineWidth(edge)
			));
		}

		// Render image to buffer
		bufferGraphics.setColor(Color.WHITE);
		bufferGraphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		Painter.paintLines(bufferGraphics, lines);

		// Save buffer fragments to tiles
		int x, y;
		for (int i = 0; i < rectangle[1][1]; i++) {
			for (int j = 0; j < rectangle[1][0]; j++) {
				x = rectangle[0][0] + j;
				y = rectangle[0][1] + i;
				tiles[x][y] = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
				buffer.getRGB(
					j * tileSize, i * tileSize,
					tileSize, tileSize,
					((DataBufferInt) tiles[x][y].getRaster().getDataBuffer()).getData(),
					0, tileSize
				);
 			}
		}
	}

	public Box getTileBox(int x, int y) {
		Vector start = new Vector(x * tileSize, y * tileSize);
		Vector stop = new Vector(start.x + tileSize, start.y + tileSize);
		return new Box(start, stop);
	}

	public Box getRectangleBox(int[][] rectangle) {
		Vector start = new Vector(
			rectangle[0][0] * tileSize,
			rectangle[0][1] * tileSize
		);
		Vector stop = new Vector(
			start.x + rectangle[1][0] * tileSize,
			start.y + rectangle[1][1] * tileSize
		);
		return new Box(start, stop);
	}

	public Box getQueryBox(Box rectangleBox) {
		rectangleBox.translate(mapBox, modelBox);
		rectangleBox.start.add(modelBox.start);
		rectangleBox.stop.add(modelBox.start);
		return rectangleBox;
	}

	public Vector translateToRectangle(Vector vector, Box rectangleBox) {
		return vector
			.translate(modelBox, mapBox)
			.sub(rectangleBox.start);
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
			return new QuadTree[]{groups[0], groups[1], groups[2], groups[3]};
		else if (zoom >= 0.05)
			return new QuadTree[]{groups[0], groups[1], groups[2], groups[3], groups[4]};
		else
			return groups;
	}
	
	public float lineWidth(Edge edge) {
		return (float)((Groups.getWidth(edge))*(1+(0.05*((Groups.getWidth(edge))/zoom))));	
	}
}