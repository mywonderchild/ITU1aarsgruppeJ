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

public class Window extends JFrame {

	public Canvas canvas;
	public JPanel sidePanel;
	public JPanel lPane;
	private JPanel bottomPanel;
	public JLabel label;
	public JPanel innerPanel;
	public JToggleButton hide;
    public int sideWidth;
    public int sideHeight;
	
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
		sideHeight = getHeight() - bottomPanel.getHeight();
		sideWidth = 200;		
		sidePanel = new JPanel();
		sidePanel.setBackground(new Color(235,235,235));
		sidePanel.setSize(45,sideHeight);
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setBorder(BorderFactory.createEtchedBorder());
        sidePanel.setOpaque(false);
        sidePanel.setBorder(null);

		// Set canvas and panel
		this.canvas = canvas;
		
		// Set JPanel
		lPane = new JPanel();
		
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
		
        // Hide button
        hide = new JToggleButton(">");
        hide.setPreferredSize(new Dimension(45, 25));
        hide.setBackground(Color.LIGHT_GRAY);
        sidePanel.add(hide, BorderLayout.LINE_END);
        hide.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e){
    		JToggleButton tBtn = (JToggleButton)e.getSource();
    		if(tBtn.isSelected()) {
	    		innerPanel.setVisible(true);
    			hide.setText("<");
    			sidePanel.setBackground(new Color(235,235,235));
				sidePanel.setBorder(BorderFactory.createEtchedBorder());
    		} else {
	        innerPanel.setVisible(false);
            hide.setText(">");	
    		}
    		} // evt ryk actionlistener i controller
    	});

        // Add canvas and bottomPanel
        getContentPane().add(canvas, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.PAGE_END);
        getContentPane().add(sidePanel, BorderLayout.WEST);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}

}
