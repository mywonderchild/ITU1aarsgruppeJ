package Map.Controller;

import Map.View.*;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import java.util.List;
import java.util.ArrayList;

import Map.Vector;
import Map.Box;
import Map.Model.Loader;
import Map.Model.Graph;
import Map.Model.ShortestPath;
import Map.Model.Edge;

public class MouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener {

	private final Window window;
	private final Canvas canvas;
	private final Tiler tiler;
	private final Loader loader;
	private final Graph graph;
	private boolean leftDown, rightDown;
	private Vector origin = new Vector(0, 0);
	private Vector panCenter;
	private ShortestPath sp;

	public MouseHandler(Window window, Tiler tiler, Loader loader) {
		this.window = window;
		canvas = window.canvas;
		this.tiler = tiler;
		this.loader = loader;
		this.graph = loader.graph;
	}

	private void reset() {
		leftDown = false;
		rightDown = false;
		canvas.selectionBox = null;
	}

	public void mouseClicked(MouseEvent e) {
		Vector mousepos = new Vector(e.getX(), e.getY());
		tiler.translateToModel(mousepos);

		if (SwingUtilities.isMiddleMouseButton(e)) {
			tiler.reset();
			canvas.repaint();
		}
		else if(SwingUtilities.isLeftMouseButton(e)) {
			if(sp == null) return;
			List<Edge> path = sp.pathTo(loader.all.findClosestEdge(mousepos, false).START.ID);
			tiler.setPath(path);
			canvas.repaint();
		}
		else if(SwingUtilities.isRightMouseButton(e)) {
			sp = new ShortestPath(graph, loader.all.findClosestEdge(mousepos, false).START.ID);
			tiler.setPath(null);
			canvas.repaint();
		}
	}

	public void mousePressed(MouseEvent e) {

		// Left and right button pressed are not exclusive.
		// Both can be pressed at the same time.
		boolean left = SwingUtilities.isLeftMouseButton(e);
		boolean right = SwingUtilities.isRightMouseButton(e);
		boolean middle = SwingUtilities.isMiddleMouseButton(e);

		if (leftDown || rightDown || middle) {
			reset();
		} else if (left) {
			// Prepare for panning
			origin.set(e.getX(), e.getY());
			panCenter = tiler.center;
			leftDown = true;
		} else if (right) {
			// Prepare for selection zoom
			origin.set(e.getX(), e.getY());
			rightDown = true;
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (rightDown) {
			
			if (canvas.selectionBox == null) return;

			Box selection = canvas.selectionBox;

			// Translate selection to model
			selection.start = tiler.translateToModel(selection.start);
			selection.stop = tiler.translateToModel(selection.stop);

			// Find and set relative center
			Vector center = selection.getCenter();
			center = tiler.modelBox.absoluteToRelative(center);
			tiler.center = center;

			// Set zoom
			Vector dimensions = selection.dimensions();
			Vector zoom = dimensions.div(tiler.modelBox.dimensions());
			if (zoom.x > zoom.y)
				tiler.setZoom(zoom.x);
			else
				tiler.setZoom(zoom.y);

			canvas.repaint();
		}

		reset();
	}

	public void mouseDragged(MouseEvent e) {
		if (leftDown) {
			Vector stop = new Vector(e.getX(), e.getY());
			if(stop.equals(origin)) return; // Mouse was not dragged

			Box box = tiler.modelBox;
			Vector center = tiler.translateToModel(stop)
				.sub(tiler.translateToModel(origin.copy()))
				.div(box.dimensions());

			tiler.center = panCenter.copy().sub(center);
			canvas.repaint();
		} else if (rightDown) {
			Vector stop = new Vector(e.getX(), e.getY());
			canvas.selectionBox = new Box(origin, stop).properCorners();
			canvas.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		Vector target = new Vector(e.getX(), e.getY());
		target = tiler.translateToModel(target);
		String closest = tiler.all.findClosestEdge(target, true).NAME;
		if (closest != window.label.getText())
			window.label.setText(closest);

		/*DEBUGGIN CODE: DRAWS CLOSEST EDGE AS PATH*/
		// ArrayList<Edge> list = new ArrayList<>();
		// list.add(tiler.all.findClosestEdge(target, true));
		// tiler.setPath(list.get(0) == null ? null : list);
		// canvas.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e){
		int movement = e.getWheelRotation();
		if (movement < 0)
			tiler.setZoom(tiler.zoom * 0.9);
		else
			tiler.setZoom(tiler.zoom * 1.1);
		canvas.repaint();
    }

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}