package Map.Controller;

import Map.View.*;
import javax.swing.KeyStroke;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import Map.Vector;
import Map.Box;

public class KeyboardHandler{

	private Canvas canvas;
	private final Tiler tiler;

	public KeyboardHandler(Canvas canvas, Tiler tiler){

		this.canvas = canvas;
		this.tiler = tiler;
		
		Action zoomOut = new Zoomer(1.2);
		Action zoomIn = new Zoomer(0.8);
		Action zoomReset = new ZoomResetter();
		Action panLeft = new Panner(new Vector(100, 0));
		Action panRight = new Panner(new Vector(-100, 0));
		Action panUp = new Panner(new Vector(0, 100));
		Action panDown = new Panner(new Vector(0, -100));

		Bindkey("ADD", "ZoomIn", zoomIn);
		Bindkey("SPACE", "ZoomReset", zoomReset);
		Bindkey("SUBTRACT", "ZoomOut", zoomOut);
		Bindkey("I", "ZoomIn", zoomIn);
		Bindkey("O", "ZoomOut", zoomOut);
		Bindkey("R", "ZoomReset", zoomReset);
		Bindkey("BACK_SPACE", "ZoomReset", zoomReset);
		Bindkey("PLUS", "ZoomIn", zoomIn);
		Bindkey("MINUS", "ZoomOut", zoomOut);
		Bindkey("LEFT", "PanLeft", panLeft);
		Bindkey("RIGHT", "PanRight", panRight);
		Bindkey("UP", "PanUp", panUp);
		Bindkey("DOWN", "PanDown", panDown);
	};


	private class Zoomer extends AbstractAction{
		
		double zoom;

		public Zoomer(double zoom){
			this.zoom = zoom;
		}

		public void actionPerformed(ActionEvent e) {
			tiler.setZoom(tiler.zoom * zoom);
			canvas.repaint();
		};
	};

	private class ZoomResetter extends AbstractAction{

		public void actionPerformed(ActionEvent e) {
			tiler.reset();
			canvas.repaint();
		};
	};

	private class Panner extends AbstractAction{

		Vector distance;

		public Panner(Vector distance){
			this.distance = distance;
		}

		public void actionPerformed(ActionEvent e) {
			Box box = tiler.modelBox;
			Vector center = tiler.translateToModel(tiler.viewBox.getCenter().add(distance))
				.sub(tiler.translateToModel(tiler.viewBox.getCenter()))
				.div(box.dimensions());
			tiler.center = tiler.center.copy().sub(center);
			canvas.repaint();
		};
	};


	private void Bindkey(String key, String actionName, Action action){
			canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key),actionName);
			canvas.getActionMap().put(actionName, action);
	};

}