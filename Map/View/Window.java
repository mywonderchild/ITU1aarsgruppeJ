package Map.View;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class Window extends JFrame {

	public Canvas canvas;
	private JPanel bottomPanel;
	private JLabel label;

	public Window(Canvas canvas) {
		
		super();

		setMinimumSize(new Dimension(400, 300));
		setPreferredSize(new Dimension(800,600));

		// Set canvas
		this.canvas = canvas;

        // Set bottomPanel
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.LIGHT_GRAY);

		// Set label
		label = new JLabel("ITU1aarsgruppeJ");
        bottomPanel.add(label);

        // Add canvas and bottomPanel
		getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}