package Map.View;

import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.Action;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuKeyEvent;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DropTextField extends JTextField {
	private final JPopupMenu pop;
	private final int rows;

	public DropTextField(int columns, int rows) {
		super(columns);
		this.rows = rows;
		pop = new JPopupMenu();
		//pop.setFocusable(false);
		for(int i = 0; i < rows; i++)
			pop.add("" + i); // fill out pop
	}

	public void setItems(String[] items) {
		if(items.length != rows) throw new IllegalArgumentException("Given array of length " + items.length + ". Expected " + rows);

		for(int i = 0; i < items.length; i++)
			pop.remove(0);

		for(int i = 0; i < items.length; i++) {
			JMenuItem item = new JMenuItem(items[i]);
			item.addActionListener(new MenuActionListener(items[i]));
			pop.add(item);
		}
	}

	public void showPop() {
		hidePop();
		Dimension tfSize = getSize(); // actual current size
		pop.setPopupSize(tfSize.width, pop.getPreferredSize().height);
		// pop.setInvoker(this);
		// pop.setLocation(getLocation().x, getLocation().y);
		// pop.setVisible(true);
		pop.show(this, 0, tfSize.height);
		requestFocusInWindow();
	}

	public void hidePop() {
		pop.setVisible(false);
	}

	private class MenuActionListener implements ActionListener {
		private final String item;

		public MenuActionListener(String item) {
			this.item = item;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setText(item);
			hidePop();
		}
	}
}