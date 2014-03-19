package Map.View;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

	private Canvas canvas;
	private JPanel buttomPanel;
	private JLabel label;

	public Window(Canvas canvas) {
		super();

		setMinimumSize(new Dimension(400, 300));
		setPreferredSize(new Dimension(800,625));

		// set canvas
		this.canvas = canvas;

        // set buttomPanel
		buttomPanel = new JPanel();
		buttomPanel.setPreferredSize(new Dimension(800,25));
		buttomPanel.setBackground(new Color(204,204,204));

		// Set label
		label = new JLabel();
		label.setText("ITU1aarsgruppeJ");
        label.setMaximumSize(new Dimension(800, 25));
        buttomPanel.add(label);

        // add canvas and buttomPanel
		getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(buttomPanel, BorderLayout.PAGE_END);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}