package Map.Controller;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import Map.View.DropTextField;

public class AddressListener implements DocumentListener {
	private final DropTextField tf;
	private final AddressFinder af;

	public AddressListener(DropTextField tf, AddressFinder af) {
		this.tf = tf;
		this.af = af;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) { update(); }

	@Override
	public void removeUpdate(DocumentEvent e) { update(); }

	private void update() {
		String text = tf.getText();
		if(text.length() >= 2) {
			tf.setItems(af.find(text, 5));
			tf.showPop();
		}
		else {
			tf.hidePop();
		}
	}
}