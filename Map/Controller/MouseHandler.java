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
	private boolean isLeft;
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

		origin.set(e.getX(), e.getY());

		if (SwingUtilities.isLeftMouseButton(e)) {
			isLeft = true;
			panCenter = translator.center;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			isLeft = false;
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (!isLeft) {
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
		translator.setLines();
	}

	public void mouseDragged(MouseEvent e) {
		if (isLeft) {
			Vector stop = new Vector(e.getX(), e.getY());
			if(stop.equals(origin)) return; // Mouse was not dragged

			Box box = translator.modelBox;
			Vector center = translator.translateToModel(stop)
				.sub(translator.translateToModel(origin.copy()))
				.div(box.dimensions());

			translator.center = panCenter.copy().sub(center);
			translator.setLines();
		} else {
			Vector stop = new Vector(e.getX(), e.getY());
			canvas.selectionBox = new Box(origin, stop);
			canvas.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		Vector target = new Vector(e.getX(), e.getY());
		target = translator.translateToModel(target);
		String closest = translator.all.findClosest(target).getName();
		System.out.println(closest);
		// Code that updates label goes here
	}
}