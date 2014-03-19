package Map.Controller;

import Map.View.*;
import javax.swing.KeyStroke;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class KeyboardHandler{

	private Canvas canvas;
	private Translator translator;
	public double standardZoom;



	public KeyboardHandler(Canvas canvas, Translator translator){
		this.canvas = canvas;
		this.translator = translator;
		standardZoom = translator.zoom;


		class zoomInClass extends AbstractAction{
			Canvas innerCanvas;
			Translator innerTranslator;

			public zoomInClass(Canvas canvas, Translator translator){
				innerCanvas = canvas;
				innerTranslator = translator;
			}
		    public void actionPerformed(ActionEvent e) {
		        innerTranslator.zoom *= 0.8;
		        innerTranslator.setLines();
				innerCanvas.repaint();
				System.out.print("zoomIn");

		   	};
		};

		class zoomOutClass extends AbstractAction{
			Canvas innerCanvas;
			Translator innerTranslator;

			public zoomOutClass(Canvas canvas, Translator translator){
				innerCanvas = canvas;
				innerTranslator = translator;
			}
		    public void actionPerformed(ActionEvent e) {
		        innerTranslator.zoom *= 1.2;
		        innerTranslator.setLines();
				innerCanvas.repaint();
				System.out.print("zoomOut");

		   	};
		};

		class zoomResetClass extends AbstractAction{
			Canvas innerCanvas;
			Translator innerTranslator;

			public zoomResetClass(Canvas canvas, Translator translator){
				innerCanvas = canvas;
				innerTranslator = translator;
			}
		    public void actionPerformed(ActionEvent e) {
		        innerTranslator.zoom = standardZoom;
		        innerTranslator.setLines();
				innerCanvas.repaint();
				System.out.print("zoomReset");

		   	};
		};
		
		Action zoomOut = new zoomOutClass(canvas, translator);
		Action zoomIn = new zoomInClass(canvas, translator);
		Action zoomReset = new zoomResetClass(canvas, translator);

		Bindkey("ADD","ZoomIn",zoomIn);
		Bindkey("SPACE","ZoomReset",zoomReset);
		Bindkey("SUBTRACT","ZoomOut",zoomOut);

	};
	private void Bindkey(String key, String actionName, Action action){
			canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key),actionName);
			canvas.getActionMap().put(actionName, action);
	};

}