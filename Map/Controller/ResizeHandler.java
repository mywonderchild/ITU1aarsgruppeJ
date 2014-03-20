package Map.Controller;

import Map.View.*;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ResizeHandler implements ComponentListener {
	private final Canvas canvas;
	private final Translator translator;
	private Timer timer;

	public ResizeHandler(Canvas canvas, Translator translator) {
		this.canvas = canvas;
		this.translator = translator;
		componentResized(null);
	}

	public void componentResized(ComponentEvent e) {
		translator.setLines();
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}
}