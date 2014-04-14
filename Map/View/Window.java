package Map.View;

// Se på ComponentListener for resize

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
	public JLayeredPane lPane;
	private JPanel bottomPanel;
	public JLabel label;
	public JPanel innerPanel;
	public JToggleButton hide;
	public JPanel canvasPanel;
    public int sideWidth;
    public int sideHeight;
    public int canvasHeight;
    public int canvasWidth;
	
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
		sidePanel.setBounds(0,0,sideWidth,sideHeight);
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setBorder(BorderFactory.createEtchedBorder());

		// Set canvas and panel
		canvasHeight = sideHeight;
		canvasWidth = getWidth();
		canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout());
		this.canvas = canvas;
		canvasPanel.setBounds(sideWidth,0,canvasWidth,canvasHeight);
		canvasPanel.add(canvas, BorderLayout.CENTER);
		
		// Set layeredPane
		lPane = new JLayeredPane();
		lPane.setPreferredSize(new Dimension(getWidth(),canvasHeight));
		
		// Set innerPanel, labels and textfields.
		innerPanel = new JPanel(new MigLayout());
		JLabel text = new JLabel("Enter directions: ");
		JLabel from =  new JLabel("(A) ");
		JLabel to =  new JLabel("(B) ");
		JTextField fromText = new JTextField("", 50);
		JTextField toText = new JTextField("", 50);
		innerPanel.add(text, "cell 0 0 3 1");
		innerPanel.add(from, "cell 0 1");
		innerPanel.add(fromText, "cell 1 1 3 1");
		innerPanel.add(to, "cell 0 2");
		innerPanel.add(toText, "cell 1 2 3 1");
		innerPanel.add(new JButton("Get route"), "cell 3 3");	
		sidePanel.add(innerPanel, BorderLayout.CENTER);
	
        // Hide button
        hide = new JToggleButton("<");
        hide.setPreferredSize(new Dimension(45, 25));
        hide.setBackground(Color.LIGHT_GRAY);
        sidePanel.add(hide, BorderLayout.PAGE_END);
        hide.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e){
    		JToggleButton tBtn = (JToggleButton)e.getSource();
    		if(tBtn.isSelected()) {
            innerPanel.setVisible(false);
            hide.setText(">");
            sidePanel.setBounds(0,0,45,sideHeight);
            canvasPanel.setBounds(0,0,canvasWidth,canvasHeight);
            sidePanel.setOpaque(false);
            sidePanel.setBorder(null);
    		} else {
    		innerPanel.setVisible(true);
    			hide.setText("<");
    			sidePanel.setBounds(0,0,sideWidth,sideHeight);          	
    			canvasPanel.setBounds(sideWidth,0,canvasWidth,canvasHeight);    			
    			sidePanel.setBackground(new Color(235,235,235));
				sidePanel.setBorder(BorderFactory.createEtchedBorder());
    			
    		}
    		} // evt ryk actionlistener i controller
    	});

        // Add canvas and bottomPanel
        lPane.add(canvasPanel, new Integer(0),0);
        lPane.add(sidePanel, new Integer(1),0);
        getContentPane().add(bottomPanel, BorderLayout.PAGE_END);
        getContentPane().add(lPane, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}

}
