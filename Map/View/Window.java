package Map.View;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLayeredPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import java.awt.Insets;

public class Window extends JFrame {

	public Canvas canvas;
	public JLayeredPane topPanel;
	public JPanel bottomPanel, sidePanel, innerPanel;
	public JLabel label;
	public JToggleButton toggler;
	
	public Window(Canvas canvas) {
		super();

		this.canvas = canvas;

		setMinimumSize(new Dimension(600, 400));
		setLayout(new BorderLayout());

		// Set innerPanel, labels and textfields.
		innerPanel = new JPanel(new MigLayout());
		JLabel text = new JLabel("Enter directions: ");
		JLabel from =  new JLabel("(A) ");
		JLabel to =  new JLabel("(B) ");
		JTextField fromText = new JTextField("", 50);
		JTextField toText = new JTextField("", 50);
		innerPanel.add(text, "span, wrap");
		innerPanel.add(from);
		innerPanel.add(fromText, "wmax 150, wrap");
		innerPanel.add(to);
		innerPanel.add(toText, "wmax 150, wrap");
		innerPanel.add(new JButton("Get route"),"skip, gapleft 65");
		innerPanel.setVisible(false);

		// Toggler
		toggler = new JToggleButton(">");
		toggler.setMargin(new Insets(0, 0, 0, 0));
		toggler.setBackground(Color.LIGHT_GRAY);
		toggler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton tBtn = (JToggleButton)e.getSource();
				if (tBtn.isSelected()) {
					innerPanel.setVisible(true);
					toggler.setText("<");
					sidePanel.setBorder(BorderFactory.createEtchedBorder());
				} else {
					innerPanel.setVisible(false);
					toggler.setText(">");
					sidePanel.setBorder(null);
				}
				resize();
			}
		});

		// SidePanel		
		sidePanel = new JPanel();
		sidePanel.setBackground(new Color(235,235,235));
		sidePanel.setLayout(new BorderLayout());
		sidePanel.add(innerPanel, BorderLayout.CENTER);
		sidePanel.add(toggler, BorderLayout.EAST);

		// TopPanel
		topPanel = new JLayeredPane();
		topPanel.add(canvas, new Integer(0));
		topPanel.add(sidePanel, new Integer(1));

		// Label
		label = new JLabel("ITU1aarsgruppeJ");

		// BottomPanel
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(235,235,235));
		bottomPanel.add(label);

		// Add topPanel and bottomPanel
		getContentPane().add(topPanel, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();

		// Set canvas size
		topPanel.addComponentListener(new ComponentListener() {
			{componentResized(null);}
			public void componentResized(ComponentEvent e) {resize();}
		    public void componentHidden(ComponentEvent e) {}
		    public void componentMoved(ComponentEvent e) {}
		    public void componentShown(ComponentEvent e) {}
		});
	}

	private void resize() {
		canvas.setBounds(0, 0, topPanel.getWidth(), topPanel.getHeight());
		int width = toggler.isSelected() ? 200 : 50;
		sidePanel.setBounds(0, 0, width, topPanel.getHeight());
	}
}
