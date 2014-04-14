package Map.Controller;

import Map.View.*;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

public class ResizeHandler implements ComponentListener {

	private final Canvas canvas;
	private final Tiler tiler;

	public ResizeHandler(Canvas canvas, Tiler tiler) {
		this.canvas = canvas;
		this.tiler = tiler;
		componentResized(null);
	}

	public void componentResized(ComponentEvent e) {
		tiler.viewBox = canvas.getBox();
		tiler.setZoom(tiler.zoom);
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}
}