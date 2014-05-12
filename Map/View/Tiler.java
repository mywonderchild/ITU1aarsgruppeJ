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
	public int tilesX, tilesY;
	public HashMap<Long, BufferedImage> tiles = new HashMap<>();
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

	public Tiler(double zoom, Vector center, Box viewBox, Box modelBox, Loader loader, Canvas canvas) {
		this.center = center;
		this.viewBox = viewBox;
		this.modelBox = modelBox;
		this.canvas = canvas;
		resetCenter = center.copy();
		resetZoom = zoom;
		gc = GraphicsEnvironment
			.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice()
			.getDefaultConfiguration();
		setZoom(zoom, false);
		if (loader != null) {
			this.all = loader.all;
			this.groups = loader.groups;
		}
	}

	public void setZoom(double zoom, boolean fake) {

		final double zoomBounded = Math.min(Math.max(zoom, minZoom), maxZoom);
		double oldZoom = this.zoom;
		this.zoom = zoomBounded;
		Vector viewDimensions = viewBox.dimensions();

		if (fake) {

			// Save a snapshot of origin zoom level if one doesn't exist
			if (snapshot == null) {
				zoomOrigin = oldZoom;
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

			this.fake = true;

			if (timer != null) timer.cancel();
			timer = new Timer(true);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					snapshot = null;
					setZoom(zoomBounded, false);
				}
			}, 200);
		} else {
			this.fake = false;

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
			tiles.clear();
		}

		if (canvas != null) canvas.repaint();
	}

	public void reset() {
		this.center = resetCenter;
		setZoom(resetZoom, false);
	}

	public void render(Graphics2D graphics) {
		if (fake) {
			fakeRender(graphics);
		} else {

			// Render the tiles in the visible section
			section = getSection();
			long[] sectionTiles = getTiles(section);
			for (int[][] rectangle : getRectangles(sectionTiles))
				renderRectangle(rectangle);

			// Draw tiles
			long timer = System.currentTimeMillis();
			int[] xy;
			for (long key : sectionTiles) {
				xy = getXY(key);
				transformer.setToIdentity();
				transformer.translate(
					xy[0] * TILESIZE - (int)section.start.x,
					xy[1] * TILESIZE - (int)section.start.y
				);
				graphics.drawRenderedImage(tiles.get(key), transformer);
			}
			System.out.println(System.currentTimeMillis() - timer);

			renderPath(graphics);
		}
		graphics.dispose();
	}

	public void fakeRender(Graphics2D graphics) {
		transformer.setToIdentity();
		double scale = zoomOrigin / zoom;
		transformer.translate(
			(int)(snapshot.getWidth() * (1 - scale) / 2),
			(int)(snapshot.getHeight() * (1 - scale) / 2)
		);
		transformer.scale(scale, scale);
		graphics.drawRenderedImage(snapshot, transformer);
	}

	public void renderPath(Graphics2D graphics) {
		if(path == null) return;

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

	public long[] getTiles(Box section) {

		// Determine which tiles are inside section
		int[][] query = new int[2][2];
		query[0][0] = Math.max((int)(section.start.x / TILESIZE), 0);
		query[0][1] = Math.max((int)(section.start.y / TILESIZE), 0);
		query[1][0] = Math.min((int)Math.ceil(section.stop.x / TILESIZE), tilesX);
		query[1][1] = Math.min((int)Math.ceil(section.stop.y / TILESIZE), tilesY);

		// Return the tiles
		int width = query[1][0] - query[0][0];
		int height = query[1][1] - query[0][1];
		if (width <= 0 || height <= 0) return new long[0];
		long[] selected = new long[width * height];
		int cursor = 0;
		for (int y = query[0][1]; y < query[1][1]; y++)
			for (int x = query[0][0]; x < query[1][0]; x++) {
				selected[cursor++] = getTileKey(x, y);
			}
		return selected;
	}

	public ArrayList<int[][]> getRectangles(long[] keys) {

		LinkedList<Long> queue = new LinkedList<>();
		for (long key : keys) if (!tiles.containsKey(key)) queue.addFirst(key);
		HashMap<Long, Integer> horizontal = new HashMap<>();
		HashMap<Long, Integer> vertical = new HashMap<>();
		ArrayList<int[][]> rectangles = new ArrayList<>();

		int[] xy;
		long east, north, largest = 1, largestKey = 0;
		int width, height;

		// Run until there are no more rectangles left
		while (largest > 0) {

			// Build map of horizontal and vertical values of rectangles dynamically
			// while keeping track of the largest rectangle found
			horizontal.clear();
			vertical.clear();
			largest = 0;
			for (long key : queue) {
				xy = getXY(key);
				east = getTileKey(xy[0] + 1, xy[1]);
				north = getTileKey(xy[0], xy[1] + 1);
				width = 1 + (horizontal.containsKey(east) ? horizontal.get(east) : 0);
				height = 1 + (vertical.containsKey(north) ? vertical.get(north) : 0);
				horizontal.put(key, width);
				vertical.put(key, height);
				if (width * height > largest) {
					largest = width * height;
					largestKey = key;
				}
			}

			// Save rectangle if one is found
			if (largest > 0) {
				xy = getXY(largestKey);
				// Remove rendered tiles from queue
				for (int i = xy[1]; i < xy[1] + vertical.get(largestKey); i++)
					for (int j = xy[0]; j < xy[0] + horizontal.get(largestKey); j++)
						queue.remove(getTileKey(j, i));
				rectangles.add(new int[][] {
					{xy[0], xy[1]},
					{horizontal.get(largestKey), vertical.get(largestKey)}
				});
			}
		}

		return rectangles;
	}

	public void renderRectangle(int[][] rectangle) {

		Box rectangleBox = getRectangleBox(rectangle);
		Box queryBox = getQueryBox(rectangleBox.copy());

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

		// Save buffer fragments to tiles
		int x, y;
		for (int i = 0; i < rectangle[1][1]; i++) {
			for (int j = 0; j < rectangle[1][0]; j++) {
				x = rectangle[0][0] + j;
				y = rectangle[0][1] + i;
				BufferedImage tile = gc.createCompatibleImage(TILESIZE, TILESIZE, Transparency.OPAQUE);
				buffer.getRGB(
					j * TILESIZE, i * TILESIZE,
					TILESIZE, TILESIZE,
					((DataBufferInt) tile.getRaster().getDataBuffer()).getData(),
					0, TILESIZE
				);
				tiles.put(getTileKey(x, y), tile);
 			}
		}
	}

	public long getTileKey(int x, int y) {
		return (long)x << 32 | y;
	}

	public int[] getXY(long tileKey) {
		return new int[] {
			(int)(tileKey >> 32),
			(int)tileKey
		};
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
}