package Map.Controller;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import Map.View.DropTextField;
import Map.Model.Edge;

public class AddressFieldListener implements DocumentListener {
	private static final int SUGGESTIONS = 5; // number of suggested addresses

	private final DropTextField tf;
	private final AddressFinder af;
	private Timer timer;

	public AddressFieldListener(DropTextField tf, AddressFinder af) {
		this.tf = tf;
		this.af = af;
		timer = new Timer(true); // daemon tread
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		tf.hidePop();
		queueUpdate();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		tf.hidePop();
		queueUpdate();
	}

	private void queueUpdate() {
		timer.cancel();
		timer = new Timer(true);
		timer.schedule(new UpdateTask(), 500);
	}

	private class UpdateTask extends TimerTask {
		public void run() {
			String text = tf.getText();
			if(text.length() >= 2) {
				List<Edge>[] result = af.find(text, SUGGESTIONS);
				String[] items = new String[SUGGESTIONS];
				for(int i = 0; i < SUGGESTIONS; i++)
					items[i] = result[i].get(0).NAME;
				tf.setItems(items);
				tf.showPop();
			}
		}
	}
}