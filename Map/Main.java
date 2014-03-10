package Map;

import Map.Controller.*;
import Map.View.*;
import Map.Model.*;

public class Main
{
	public static void main(String[] args) {
		Canvas canvas = new Canvas();
		Window window = new Window(canvas);
		QuadTree qt = (new Loader()).edata;
		Translator translator = new Translator(canvas, qt);
		translator.setLines(qt.getBounds());
		// Low X:  442 254,35659
		// High X: 892 658,21706
		// Low Y:  6 049 914,43018
		// High Y: 6 402 050,98297
	}
}