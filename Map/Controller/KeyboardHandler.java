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
	private final Translator translator;


	public KeyboardHandler(Canvas canvas, Translator translator){
		this.canvas = canvas;
		this.translator = translator;





		
		Action zoomOut = new zoomClass(1.2);
		Action zoomIn = new zoomClass(0.8);
		Action zoomReset = new zoomResetClass();
		Action panLeft = new panClass(new Vector (-100,0));

		Bindkey("ADD","ZoomIn",zoomIn);
		Bindkey("SPACE","ZoomReset",zoomReset);
		Bindkey("SUBTRACT","ZoomOut",zoomOut);
		Bindkey("I","ZoomIn",zoomIn);
		Bindkey("O","ZoomOut",zoomOut);
		Bindkey("R","ZoomReset",zoomReset);
		Bindkey("BACK_SPACE","ZoomReset",zoomReset);
		Bindkey("PLUS","ZoomIn",zoomIn);
		Bindkey("MINUS","ZoomOut",zoomOut);
		Bindkey("B","Pan",panLeft);
	};


			private class zoomClass extends AbstractAction{
			
			double zoom;

			public zoomClass(double zoom){
				this.zoom = zoom;


			}
		    public void actionPerformed(ActionEvent e) {
		        translator.zoom *= zoom;
		        translator.setLines();
				System.out.print("zoomOut");

		   	};
		};

		private class zoomResetClass extends AbstractAction{

		    public void actionPerformed(ActionEvent e) {
		        translator.zoom = translator.startZoom;
		        translator.center = translator.startCenter;
		        translator.setLines();
				System.out.print("zoomReset");

		   	};
		};

		private class panClass extends AbstractAction{
			
			Vector distance;

			public panClass(Vector distance){

			this.distance = distance;
				
			}
		    public void actionPerformed(ActionEvent e) {
			Box box = translator.modelBox;
			Vector center = translator.translateToModel(translator.canvasBox.getCenter().add(distance))
				.sub(translator.translateToModel(translator.canvasBox.getCenter()))
				.div(box.dimensions());

			translator.center = translator.center.copy().sub(center);
			translator.setLines();

		        // Vector pandistance = distance.div(translator.modelBox.dimensions());
		        // translator.center = translator.center.copy().sub(pandistance);
		        // translator.setLines();
		        System.out.print(translator.center);

		   	};
		};


	private void Bindkey(String key, String actionName, Action action){
			canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key),actionName);
			canvas.getActionMap().put(actionName, action);
	};

}