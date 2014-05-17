package Map.View;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.lang.Iterable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

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

	private static final int TILESIZE = 256;
	public Box mapBox, viewBox, modelBox, section;
	private Canvas canvas;
	private HashMap<Long, Tile> tileHash = new HashMap<>();
	public int tilesX, tilesY;
	public double zoom, resetZoom, minZoom = 0.005, maxZoom = 1.5, zoomOrigin;
	public Vector center, resetCenter;
	public QuadTree all;
	public Path path;
	private QuadTree[] groups;
	private ArrayList<Line> linePool = new ArrayList<Line>();
	private BufferedImage buffer, snapshot;
	private Graphics2D bufferGraphics;
	private Timer timer;
	private boolean fake;
	private AffineTransform transformer = new AffineTransform();
	private GraphicsConfiguration gc;
	private int threadID = 0;

	public Tiler(double zoom, Vector center, Box viewBox, Box modelBox, Loader loader, Canvas canvas) {

		this.center = center;
		this.viewBox = viewBox;
		this.modelBox = modelBox;
		this.canvas = canvas;
		this.all = loader.all;
		this.groups = loader.groups;
		resetCenter = center.copy();
		resetZoom = zoom;

		gc = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration();

		setZoom(zoom, false);
	}

	private class Tile {

		public final int x, y;
		public BufferedImage image;
		public boolean isRendering = false;

		public Tile(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public void setZoom(double zoom, boolean fake) {

		threadID++;
		Vector viewDimensions = viewBox.dimensions();

		if (snapshot == null) {
			zoomOrigin = this.zoom;
			snapshot = gc.createCompatibleImage(
				(int)viewDimensions.x,
				(int)viewDimensions.y,
				Transparency.OPAQUE
			);
			Graphics2D graphics = (Graphics2D)snapshot.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, snapshot.getWidth(), snapshot.getHeight());
			render(graphics);
			graphics.dispose();
		} 

		final double zoomBounded = Math.min(Math.max(zoom, minZoom), maxZoom);
		this.zoom = zoomBounded;

		buffer = gc.createCompatibleImage(
			(int)viewDimensions.x + TILESIZE * 2,
			(int)viewDimensions.y + TILESIZE * 2,
			Transparency.OPAQUE
		);
		bufferGraphics = buffer.createGraphics();
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Vector mapDimensions = viewDimensions
			.div(viewBox.ratio())
			.div(this.zoom)
			.mult(modelBox.ratio());
		mapBox = new Box(new Vector(0, 0), mapDimensions);

		tilesX = (int)Math.ceil(mapDimensions.x / TILESIZE);
		tilesY = (int)Math.ceil(mapDimensions.y / TILESIZE);

		tileHash.clear();

		canvas.repaint();	
	}

	public void reset() {
		this.center = resetCenter;
		setZoom(resetZoom, false);
	}

	public void render(Graphics2D graphics) {

		// Do the fake render using snapshot no matter what
		if (snapshot != null) {
			transformer.setToIdentity();
			double scale = zoomOrigin / zoom;
			transformer.translate(
				(int)(snapshot.getWidth() * (1 - scale) / 2),
				(int)(snapshot.getHeight() * (1 - scale) / 2)
			);
			transformer.scale(scale, scale);
			graphics.drawRenderedImage(snapshot, transformer);
		}

		// Render the tiles in the visible section
		if (mapBox == null) return;
		section = getSection();
		Tile[] tiles = getTiles(section);
		
		for (int[][] rectangle : getRectangles(tiles)) {
			Thread thread = new RenderThread(rectangle, this);
			thread.setDaemon(true);
			thread.start();
		}

		// Draw tiles
		for (Tile tile : tiles) {
			if (tile.image != null) {
				transformer.setToIdentity();
				transformer.translate(
					tile.x * TILESIZE - (int)section.start.x,
					tile.y * TILESIZE - (int)section.start.y
				);
				graphics.drawRenderedImage(tile.image, transformer);
			}
		}
		renderPath(graphics);
	}

	public void renderPath(Graphics2D graphics) {
		if (path == null) return;
		float width = 1.3f;
		ArrayList<Line> lines = new ArrayList<Line>();
		for(Edge edge : path.edges) {
			lines.add(new Line().set(
				translateToView(edge.START.VECTOR.copy()),
				translateToView(edge.END.VECTOR.copy()),
				Color.MAGENTA,
				(float)(width*(1+(0.05*(width/zoom))))
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

	public Tile[] getTiles(Box section) {

		// Determine which tiles are inside section
		int[][] query = new int[2][2];
		query[0][0] = Math.max((int)(section.start.x / TILESIZE), 0);
		query[0][1] = Math.max((int)(section.start.y / TILESIZE), 0);
		query[1][0] = Math.min((int)Math.ceil(section.stop.x / TILESIZE), tilesX);
		query[1][1] = Math.min((int)Math.ceil(section.stop.y / TILESIZE), tilesY);

		// Return the tiles
		int width = query[1][0] - query[0][0];
		int height = query[1][1] - query[0][1];
		if (width <= 0 || height <= 0) return new Tile[0];
		Tile[] selected = new Tile[width * height];
		int cursor = 0;
		long key;
		for (int y = query[0][1]; y < query[1][1]; y++) {
			for (int x = query[0][0]; x < query[1][0]; x++) {
				key = getTileKey(x, y);
				Tile tile = tileHash.get(key);
				if (tile == null) {
					tile = new Tile(x, y);
					tileHash.put(key, tile);
				}
				selected[cursor++] = tile;
			}
		}
		return selected;
	}

	public ArrayList<int[][]> getRectangles(Tile[] tiles) {

		ArrayList<int[][]> rectangles = new ArrayList<>();

		ArrayList<Tile> list = new ArrayList<>();
		for (Tile tile : tiles) if (tile.image == null && !tile.isRendering) {
			tile.isRendering = true;
			list.add(tile);
		}
		if (list.size() == 0) return rectangles;

		// Terminate if usual case
		int width = (list.get(list.size() - 1).x - list.get(0).x + 1);
		int height = (list.get(list.size() - 1).y - list.get(0).y + 1);
		int area = width * height;
		if (area == list.size()) {
			rectangles.add(new int[][]{
				{list.get(0).x, list.get(0).y},
				{width, height}
			});
			return rectangles;
		}

		// Wasn't usual case, now we find the max rects
		width = tiles[tiles.length - 1].x - tiles[0].x + 1;
		height = tiles[tiles.length - 1].y - tiles[0].y + 1;

		// Build boolean map of tile render states
		boolean[][] unrendered = new boolean[width][height];
		for (Tile tile : list)
			unrendered[tile.x - tiles[0].x][tile.y - tiles[0].y] = true;
		int[][] histogram = new int[width][height];
		int[][] rectangle = null;

		int count = list.size();
		while (count > 0) {

			// Build histogram
			for (int x = 0; x < width; x++)
			for (int y = height - 1; y >= 0; y--)
				if (!unrendered[x][y])
					histogram[x][y] = 0;
				else if (y == height - 1)
					histogram[x][y] = 1;
				else
					histogram[x][y] = 1 + histogram[x][y + 1];

			// Find biggest rectangle
			int start, min, max = 0;
			for (int y = 0; y < height; y++) {
				start = 0;
				min = Integer.MAX_VALUE;
				for (int x = 0; x < width; x++) {
					if (histogram[x][y] == 0) {
						start = x + 1;
						min = Integer.MAX_VALUE;
					} else {
						min = Math.min(histogram[x][y], min);
						area = (x - start + 1) * min;
						if (area > max) {
							max = area;
							rectangle = new int[][] {
								{start, y},
								{x - start + 1, min}
							};
						}
					}
				}
			}

			// Update render states
			for (int y = rectangle[0][1]; y < rectangle[0][1] + rectangle[1][1]; y++) {
				for (int x = rectangle[0][0]; x < rectangle[0][0] + rectangle[1][0]; x++){
					unrendered[x][y] = false;
					count--;
				}
			}

			rectangle[0][0] += tiles[0].x;
			rectangle[0][1] += tiles[0].y;
			rectangles.add(rectangle);
		}

		return rectangles;
	}

	private long getTileKey(int x, int y) {
		return (long)x << 32 | y;
	}

	public Box getRectangleBox(int[][] rectangle) {
		Vector start = new Vector(
			rectangle[0][0] * TILESIZE,
			rectangle[0][1] * TILESIZE
		);
		Vector stop = new Vector(
			start.x + rectangle[1][0] * TILESIZE,
			start.y + rectangle[1][1] * TILESIZE
		);
		return new Box(start, stop);
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

	private class RenderThread extends Thread {
		
		int id = threadID;
		int[][] rectangle;
		Tiler tiler;

		public RenderThread(int[][] rectangle, Tiler tiler) {
			this.rectangle = rectangle;
			this.tiler = tiler;
		}

		@Override
		public void run() {

			synchronized(tiler) {
				Box rectangleBox = getRectangleBox(rectangle);
				Box queryBox = rectangleBox.copy().translate(mapBox, modelBox);

				// Get edges from QT's
				ArrayList<Edge> edges = new ArrayList<>();
				int[] visible = Groups.getVisibleGroups(zoom);
				for(int i = visible.length-1; i >= 0; i--)
					edges.addAll(groups[visible[i]].queryRange(queryBox));


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
						Groups.getWidth(edge, zoom)
					));
				}

				// Render image to buffer
				bufferGraphics.setColor(Color.WHITE);
				bufferGraphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
				Painter.paintLines(bufferGraphics, lines);

				if (!isValid()) return;

				// Save buffer fragments to tiles
				int x, y;
				for (int i = 0; i < rectangle[1][1]; i++) {
					for (int j = 0; j < rectangle[1][0]; j++) {
						if (!isValid()) return;
						x = rectangle[0][0] + j;
						y = rectangle[0][1] + i;
						Tile tile = tileHash.get(getTileKey(x, y));
						tile.image = gc.createCompatibleImage(TILESIZE, TILESIZE, Transparency.OPAQUE);
						buffer.getRGB(
							j * TILESIZE, i * TILESIZE,
							TILESIZE, TILESIZE,
							((DataBufferInt) tile.image.getRaster().getDataBuffer()).getData(),
							0, TILESIZE
						);
						tile.isRendering = false;
		 			}
				}
				snapshot = null;
				synchronized(canvas) { canvas.repaint(); }
			}
		}

		private boolean isValid() {
			return id == threadID;
		}
	}
}