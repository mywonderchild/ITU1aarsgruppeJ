package Map.Controller;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;


public class MouseWheelHandler implements MouseWheelListener{

	Translator translator;	

    public MouseWheelHandler(Translator translator) {

    	this.translator = translator;

      
    }

    public void mouseWheelMoved(MouseWheelEvent e){
    	int movement = e.getWheelRotation();
    	if(movement < 0){
    		System.out.println("zoomIn");
    		translator.zoom *= 0.9;
    		translator.setLines();
    	}
    	else{
    		System.out.println("zoomOut");
    	    translator.zoom *= 1.1;
    		translator.setLines();
    	}
    }
}