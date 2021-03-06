package Map.View;

import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Insets;

public class Window extends JFrame {

	public Canvas canvas;
	public JLayeredPane topPanel;
	public JPanel bottomPanel, sidePanel, innerPanel;
	public JLabel closest, directions;
	public JToggleButton toggler;
	public JScrollPane scrollPane;
	public DropTextField fromText, toText;
	public JButton routeButton;
	
	public Window(Canvas canvas) {

		this.canvas = canvas;

		setMinimumSize(new Dimension(1024, 768));
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.WHITE);

		// Set innerPanel, labels and textfields.
		innerPanel = new JPanel(new MigLayout());
		JLabel text = new JLabel("Enter directions: ");
		JLabel from =  new JLabel("(A) ");
		JLabel to =  new JLabel("(B) ");
		routeButton = new JButton("Get route");
		fromText = new DropTextField(50);
		toText = new DropTextField(50);
		innerPanel.add(text, "span, wrap");
		innerPanel.add(from);
		innerPanel.add(fromText, "span, wrap");
		innerPanel.add(to);
		innerPanel.add(toText, "span, wrap");
		innerPanel.add(routeButton,"skip, al 100%");
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
					scrollPane.setVisible(true);
					toggler.setText("<");
					sidePanel.setBorder(BorderFactory.createEtchedBorder());
            		sidePanel.setOpaque(true);
				} else {
					innerPanel.setVisible(false);
					scrollPane.setVisible(false);
					toggler.setText(">");
					sidePanel.setBorder(null);
					sidePanel.setOpaque(false);
				}
				resize();
			}
		});

		// Scrollpane
		scrollPane = new JScrollPane();
		directions = new JLabel("");
		directions.setVerticalAlignment(JLabel.TOP);
		directions.setVerticalTextPosition(JLabel.TOP);
		scrollPane.getViewport().add( directions );
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBorder(BorderFactory.createEtchedBorder());
		scrollPane.setVisible(false);

		// SidePanel		
		sidePanel = new JPanel();
		sidePanel.setBackground(new Color(235,235,235));
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setOpaque(false);
		sidePanel.add(innerPanel, BorderLayout.NORTH);
		sidePanel.add(toggler, BorderLayout.SOUTH);
		sidePanel.add(scrollPane, BorderLayout.CENTER);

		// TopPanel
		topPanel = new JLayeredPane();
		topPanel.add(canvas, new Integer(0));
		topPanel.add(sidePanel, new Integer(1));

		// Label
		closest = new JLabel("ITU1aarsgruppeJ");

		// BottomPanel
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(235,235,235));
		bottomPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomPanel.add(closest);


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
		int width = toggler.isSelected() ? 400 : 50;
		sidePanel.setBounds(0, 0, width, topPanel.getHeight());
	}

	public void setDirections(ArrayList<String> directions) {
		setDirections(directions, null);
	}

	public void setDirections(ArrayList<String> directions, NotFound error) {
		String html = "<html><body>";

		if(directions == null) {
			if(error == null) this.directions.setText(null);
			else {
				switch(error) {
					case FROM:	html += "<font color=\"red\">'From' address not found!</font>";
								break;
					case TO:	html += "<font color=\"red\">'To' address not found!</font>";
								break;
					case BOTH:	html += "<font color=\"red\">'From' and 'To' addresses not found!</font>";
								break;
					case PATH:	html += "<font color=\"red\">No route found!</font>";
				}
			}
		}
		else {
			for(String direction : directions)
				html += direction + "<br>";
		}

		html += "</body></html>";
		this.directions.setText(html);
		if (!toggler.isSelected()) toggler.doClick();
	}

	public enum NotFound { FROM, TO, BOTH, PATH; }
}
