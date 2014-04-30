package Map.View;

import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.Action;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuKeyEvent;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class DropTextField extends JTextField {
	private final JPopupMenu pop;
	private final int rows;
	private DropTextField self = this;

	public DropTextField(int columns, int rows) {
		super(columns);
		this.rows = rows;
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e) {
				hidePop();
			}
		});
		pop = new JPopupMenu();
	}

	public void setItems(String[] items) {
		clearItems();
		for(int i = 0; i < items.length; i++) {
			JMenuItem item = new JMenuItem(items[i]);
			item.addActionListener(new MenuActionListener(items[i]));
			pop.add(item);
		}
	}

	public void clearItems() {
		pop.removeAll();
	}

	public void showPop() {
		hidePop(); // prevents nasty bug. swingiling!

		Dimension tfSize = getSize(); // actual textfield size

		int height = 0; // pref height of all children:
		for(Component comp : pop.getComponents())
			height += comp.getPreferredSize().height;
		pop.setPopupSize(tfSize.width, height);

		pop.show(this, 0, tfSize.height); // just beneath textfield
		requestFocusInWindow(); // popup has stolen focus - show it who is boss
		select(getText().length(), getText().length()); // set cursor to end
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