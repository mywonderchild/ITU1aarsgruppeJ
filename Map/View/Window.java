package Map.View;

import javax.swing.JFrame;

import Map.Controller.MouseHandler;

public class Window extends JFrame {

	private Canvas canvas;

	public Window(Canvas canvas) {
		super();

		// Add canvas
		this.canvas = canvas;
		this.canvas.addMouseListener(new MouseHandler(canvas));
		this.getContentPane().add(canvas);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}