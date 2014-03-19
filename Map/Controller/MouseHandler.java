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
	private boolean action;
	private Vector origin = new Vector(0, 0);
	private Vector lastDrag = new Vector(0, 0);

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
		origin.set(e.getX(), e.getY());
		System.out.println(origin);
		if (SwingUtilities.isLeftMouseButton(e)) {
			action = true;
			Box box = canvas.getBox();
			lastDrag = translator.translateToModel(new Vector(e.getX(), e.getY()));
		} else if (SwingUtilities.isRightMouseButton(e)) {
			action = false;
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (!action) {
			Box selection = canvas.selectionBox;

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
		}
		lastDrag = null; // Reset paning
		translator.setLines();
	}

	public void mouseDragged(MouseEvent e) {
		if (!action) { // Right mouse button
			Vector stop = new Vector(e.getX(), e.getY());
			canvas.selectionBox = new Box(origin, stop);
			canvas.repaint();
		}
		else { // Left mouse button
			Box box = translator.all.getBox();//canvas.getBox();

			System.out.println("lastDrag: " + lastDrag);

			Vector stop = translator.translateToModel(new Vector(e.getX(), e.getY()));
			System.out.println("stop: " + stop);
			Vector movement = stop.copy().sub(lastDrag);
			System.out.println("movement: " + movement);
			Vector relative = movement.copy().div(box.dimensions());
			//relative.mult(translator.zoom);

			System.out.println("relative: " + relative);
			translator.center.add(relative);
			System.out.println("center: " + translator.center);
			lastDrag = stop;

			translator.setLines();
		}
	}

	public void mouseMoved(MouseEvent e) {
		Vector target = new Vector(e.getX(), e.getY());
		target = translator.translateToModel(target);
		Edge closest = translator.all.findClosest(target);
		// Code that updates label goes here
	}
}