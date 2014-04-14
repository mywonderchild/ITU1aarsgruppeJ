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
import net.miginfocom.swing.MigLayout;
import javax.swing.JLayeredPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import java.awt.Insets;

public class Window extends JFrame {

	public Canvas canvas;
	public JPanel sidePanel, bottomPanel, innerPanel;
	public JLabel label;
	public JToggleButton toggler;
	
	public Window(Canvas canvas) {
		super();

		setMinimumSize(new Dimension(600, 400));
		setLayout(new BorderLayout());

		// Set bottomPanel and label
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(235,235,235));
		label = new JLabel("ITU1aarsgruppeJ");
		bottomPanel.add(label);

		// Set sidePanel		
		sidePanel = new JPanel();
		sidePanel.setBackground(new Color(235,235,235));
		sidePanel.setLayout(new BorderLayout());

		// Set canvas and panel
		this.canvas = canvas;
		
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
		sidePanel.add(innerPanel, BorderLayout.CENTER);
		
		// Toggler
		toggler = new JToggleButton(">");
		// FIND A WAY TO SLIM DOWN THE WIDTH
		toggler.setMargin(new Insets(0, 0, 0, 0));
		toggler.setBackground(Color.LIGHT_GRAY);
		sidePanel.add(toggler, BorderLayout.EAST);
		toggler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton tBtn = (JToggleButton)e.getSource();
				if(tBtn.isSelected()) {
					innerPanel.setVisible(true);
					toggler.setText("<");
					sidePanel.setBorder(BorderFactory.createEtchedBorder());
				} else {
					innerPanel.setVisible(false);
					toggler.setText(">");
					sidePanel.setBorder(null);
				}
			}
		});

		// Add canvas and bottomPanel
		getContentPane().add(canvas, BorderLayout.CENTER);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		getContentPane().add(sidePanel, BorderLayout.WEST);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}
