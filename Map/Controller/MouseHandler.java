package Map.Controller;

import Map.View.*;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

public class MouseHandler implements MouseListener, MouseMotionListener {
	private Canvas canvas;

	public MouseHandler(Canvas canvas) {
		this.canvas = canvas;
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

		if (SwingUtilities.isLeftMouseButton(e)) {
			System.out.println("Left mouse button clicked");
		} else if (SwingUtilities.isRightMouseButton(e)) {
			System.out.println("Right mouse button clicked");
		} else {
			System.out.println("Other mouse button clicked");
		}
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		// INSERT CODE FOR LOCATING NEAREST ROAD HERE!
	}
}