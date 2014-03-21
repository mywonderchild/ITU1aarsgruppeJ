package Map.Controller;

import Map.View.*;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import Map.Vector;
import Map.Box;

public class MouseHandler implements MouseListener, MouseMotionListener {

	private Window window;
	private Canvas canvas;
	private Translator translator;
	private boolean leftDown;
	private boolean rightDown;
	private Vector origin = new Vector(0, 0);

	private Vector panCenter;

	public MouseHandler(Window window, Translator translator) {
		this.window = window;
		canvas = window.canvas;
		this.translator = translator;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {

		// Left and right button pressed are not exclusive.
		// Both can be pressed at the same time.
		boolean left = SwingUtilities.isLeftMouseButton(e);
		boolean right = SwingUtilities.isRightMouseButton(e);

		if (left && right) {
			// Reset
			leftDown = false;
			rightDown = false;
			canvas.selectionBox = null;
		} else if (left) {
			// Prepare for panning
			origin.set(e.getX(), e.getY());
			panCenter = translator.center;
			leftDown = true;
		} else if (right) {
			// Prepare for selection zoom
			origin.set(e.getX(), e.getY());
			rightDown = true;
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (rightDown) {

			rightDown = false;
			if (canvas.selectionBox == null) return;

			Box selection = canvas.selectionBox;
			if (selection == null) return;

			// Translate selection to model
			selection.start = translator.translateToModel(selection.start);
			selection.stop = translator.translateToModel(selection.stop);

			// Find and set relative center
			Vector center = selection.getCenter();
			center = translator.modelBox.absoluteToRelative(center);
			translator.center = center;

			// Set zoom
			Vector ratio = selection.ratio();
			Vector dimensions = selection.dimensions();
			Vector zoom = dimensions.div(translator.modelBox.dimensions());
			if (zoom.x > zoom.y)
				translator.zoom = zoom.x;
			else
				translator.zoom = zoom.y;

			canvas.selectionBox = null;
			translator.setLines();
			
		} else if (leftDown) {
			leftDown = false; // Reset
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (leftDown) {
			Vector stop = new Vector(e.getX(), e.getY());
			if(stop.equals(origin)) return; // Mouse was not dragged

			Box box = translator.modelBox;
			Vector center = translator.translateToModel(stop)
				.sub(translator.translateToModel(origin.copy()))
				.div(box.dimensions());

			translator.center = panCenter.copy().sub(center);
			translator.setLines();
		} else if (rightDown) {
			Vector stop = new Vector(e.getX(), e.getY());
			canvas.selectionBox = new Box(origin, stop).properCorners();
			canvas.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		Vector target = new Vector(e.getX(), e.getY());
		target = translator.translateToModel(target);
		String closest = translator.all.findClosest(target).NAME;
		// System.out.println(closest);
		// Code that updates label goes here
	}
}