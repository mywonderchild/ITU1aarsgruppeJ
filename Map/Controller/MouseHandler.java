package Map.Controller;

import Map.View.*;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import Map.Vector;
import Map.Box;

public class MouseHandler implements MouseListener, MouseMotionListener {

	private Canvas canvas;
	private Translator translator;
	private boolean action;
	private Vector origin = new Vector(0, 0);

	public MouseHandler(Canvas canvas, Translator translator) {
		this.canvas = canvas;
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
			System.out.println("Left mouse button clicked");
		} else if (SwingUtilities.isRightMouseButton(e)) {
			action = false;
			System.out.println("Right mouse button clicked");
		} else {
			System.out.println("Other mouse button clicked");
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (!action) {
			Box selection = canvas.selectionBox;

			// Translate selection to model
			selection.start = translator.translateToModel(selection.start);
			selection.stop = translator.translateToModel(selection.stop);
			System.out.println(selection);

			// Find and set relative center
			Vector center = selection.getCenter();
			center = translator.modelBox.absoluteToRelative(center);
			translator.center = center;

			// Set zoom
			Vector ratio = selection.ratio();
			Vector dimensions = selection.dimensions();
			Vector zoom = dimensions.div(translator.modelBox.dimensions());
			System.out.println(zoom);
			if (zoom.x > zoom.y)
				translator.zoom = zoom.x;
			else
				translator.zoom = zoom.y;

			canvas.selectionBox = null;
		}

		canvas.setLines(translator.getLines());
		canvas.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (!action) {
			Vector stop = new Vector(e.getX(), e.getY());
			canvas.selectionBox = new Box(origin, stop);
			canvas.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		// INSERT CODE FOR LOCATING NEAREST ROAD HERE!
	}
}