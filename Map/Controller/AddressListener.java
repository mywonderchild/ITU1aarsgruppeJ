package Map.Controller;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTextField;

public class AddressListener implements DocumentListener {
	private final JTextField tf;
	private final AddressFinder af;

	public AddressListener(JTextField tf, AddressFinder af) {
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
			System.out.println(af.find(text));
		}
	}
}