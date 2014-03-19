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
		if(timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new BeautyTask(), 500);

		translator.setLines();
    }

    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

	private class BeautyTask extends TimerTask {

		public void run() {
			canvas.beauty = true;
			canvas.repaint();
		}
	}
}