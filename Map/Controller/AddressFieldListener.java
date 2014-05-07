package Map.Controller;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import java.lang.InterruptedException;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.KeyboardFocusManager;

import Map.View.DropTextField;
import Map.Model.Edge;

public class AddressFieldListener implements DocumentListener {
	private static final int SUGGESTIONS = 5; // number of suggested addresses
	private int threadid = 0;

	private final DropTextField tf;
	private final AddressFinder af;
	private final Map<Integer, String> cities;
	private Timer timer;

	public AddressFieldListener(DropTextField tf, AddressFinder af, Map<Integer, String> cities) {
		this.tf = tf;
		this.af = af;
		this.cities = cities;
		timer = new Timer(true); // daemon tread
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(tf.isDropInserting()) return;
		tf.hidePop();
		queueUpdate();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(tf.isDropRemoving()) return;
		tf.hidePop();
		queueUpdate();
	}

	private void queueUpdate() {
		Thread t = new UpdateThread();
		t.setDaemon(true);
		t.start();
	}

	private class UpdateThread extends Thread {
		private final int id = ++threadid;

		public UpdateThread() {
		}

		@Override
		public void run() {
			String text = tf.getText();
			if(text.length() == 0) return;

			Thread t = af.getFindThread(text, SUGGESTIONS, this);
			t.setDaemon(true);
			t.start();
			try {
				synchronized(this){ wait(); } // wait for find thread to finish
			} catch(InterruptedException e) { e.printStackTrace(); }

			if(!isValid() || !tf.equals(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner())) {
				return;
			} // stop update thread if no longer valid

			List<Edge>[] result = af.getResult();
			String[] items = new String[SUGGESTIONS];
			for(int i = 0; i < SUGGESTIONS; i++) {
				Edge edge = result[i].get(0);
				String suffix = "";
				if(edge.ZIP > 0) {
					suffix = ", " + edge.ZIP;
					String city = cities.get(edge.ZIP);
					if(city != null) suffix += " " + city;
				}
				items[i] = edge.NAME + suffix; // add zip to name, if edge has real zip;
			}
			tf.setItems(items);
			tf.showPop();
		}

		private boolean isValid() {
			return id == threadid;
		}
	}
}