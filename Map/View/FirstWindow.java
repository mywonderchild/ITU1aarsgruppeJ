package Map.View;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import Map.Controller.*;
import Map.*;
import Map.Model.*;


public class FirstWindow extends JFrame {
	public Window window;
	public Canvas canvas;
	public Loader loader;
	public Tiler tiler;
	public KeyboardHandler keyboardHandler; 
	public MouseHandler mouseHandler;
	public JPanel centerPanel;
	public JButton button1, button2;

	public FirstWindow() {
		setMinimumSize(new Dimension(600,400));
		setResizable(false);
		setLayout(null);

		button1 = new JButton("Krak");
		button1.setBackground(Color.LIGHT_GRAY);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openKrak();	
				closeWindow();
			}
		});

		button2 = new JButton("OpenStreetMap");
		button2.setBackground(Color.LIGHT_GRAY);

		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(0, 1));
		centerPanel.add(button1);
		centerPanel.add(button2);
        centerPanel.setBounds(200, 120, 200, 134);

        getContentPane().add(centerPanel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}

	public void openKrak() {

		// Set up model
		this.loader = new Loader();

		// Set up view
		this.canvas = new Canvas();
		this.window = new Window(canvas);
		
		// Tiler
		this.tiler = new Tiler(1.3, new Vector(0.5, 0.5), canvas.getBox(), loader.all.getBox(), loader);
		canvas.tiler = tiler;
		canvas.repaint();

		// Event listeners
		this.keyboardHandler = new KeyboardHandler(canvas, tiler);
		canvas.addComponentListener(new ComponentListener() {
			{componentResized(null);}
			public void componentResized(ComponentEvent e) {
				tiler.viewBox = canvas.getBox();
				tiler.setZoom(tiler.zoom);
		    }
		    public void componentHidden(ComponentEvent e) {}
		    public void componentMoved(ComponentEvent e) {}
		    public void componentShown(ComponentEvent e) {}
		});
		this.mouseHandler = new MouseHandler(window, tiler, loader);
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMotionListener(mouseHandler);
		canvas.addMouseWheelListener(mouseHandler);
	}

	public void closeWindow() {
		setVisible(false);
		dispose();
	}
}