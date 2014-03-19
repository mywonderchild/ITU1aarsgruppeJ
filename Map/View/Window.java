package Map.View;

import javax.swing.JFrame;

public class Window extends JFrame {

	private Canvas canvas;

	public Window(Canvas canvas) {
		super();

		// Add canvas
		this.canvas = canvas;
		this.getContentPane().add(canvas);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}