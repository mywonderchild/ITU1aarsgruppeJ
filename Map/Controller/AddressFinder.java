package Map.Controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.List;

import Map.Model.Edge;
import Map.Model.PriorityQueue;

public class AddressFinder {
	private final Map<String, List<Edge>> map;
	private List<Edge>[] best;
	private boolean working;
	private int threadid = 0;

	public AddressFinder(Map<String, List<Edge>> roads) {
		this.map = roads;
	}

	public Thread getFindThread(String address, Object callback) {
		return getFindThread(address, 1, callback);
	}

	public Thread getFindThread(String address, int amount, Object callback) {
		return new FindThread(address, amount, callback);
	}

	public List<Edge>[] getResult() {
		if(working) return null;
		return best;
	}

	public boolean hasResult() {
		return best != null && !working;
	}

	private static int dist(String s1, String s2) {
		// We do not care about
		// lower/upper case discrepancies.
		char[] c1 = (" "+s1).toLowerCase().toCharArray();
		char[] c2 = (" "+s2).toLowerCase().toCharArray();

		// Initialize distance matrix between c1 and c2.
		// As [0][1] and [1][0] are empty strings, the
		// distance to these must be the length of the
		// opposing string.
		// Thus distance [i][0] and [0][i] must be i.

		int[][] d = new int[c1.length][c2.length];
		for(int i = 0; i < c1.length; i++)
			d[i][0] = i;
		for(int i = 0; i < c2.length; i++)
			d[0][i] = i;

		// Compare each char to every char
		// in the opposing string.
		for(int i = 1; i < c1.length; i++) {
			for(int j = 1; j < c2.length; j++) {
				// If two chars are equal, then dist = 0.
				if(c1[i] == c2[j]) {
					d[i][j] = d[i-1][j-1]; // NW: no action
				}
				else {
					d[i][j] = min(
						d[i-1][j] + 1,	// del
						d[i][j-1] + ((j<c1.length && d[i][j] == 0) ? 1 : 0), 	// ins, free if prefix
						d[i-1][j-1] + 2 // sub
					);
				}
			}
		}
		return d[c1.length-1][c2.length-1];
	}

	private static int min(int... args) {
		int min = Integer.MAX_VALUE;
		for(int i : args)
			if(i < min) min = i;
		return min;
	}

	private class FindThread extends Thread {
		private final int id = ++threadid;
		private final String address;
		private final int amount;
		private final Object callback;

		public FindThread(String address, int amount, Object callback) {
			this.address = address;
			this.amount = amount;
			this.callback = callback;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			working = true;
			PriorityQueue<Integer, List<Edge>> pq = new PriorityQueue<Integer, List<Edge>>();
			for(Entry<String, List<Edge>> entry : map.entrySet()) {
				pq.push(dist(address, entry.getKey()), entry.getValue());
				if(!isValid()) { // if no longer valid
					notifyCaller();
					return; // kill thread
				}
				Thread.yield();
			}

			best = (List<Edge>[]) new List[amount];
			for(int i = 0; i < amount; i++) {
				best[i] = (List<Edge>) pq.pop().getValue();
			}
			working = false;
			notifyCaller();
		}

		private boolean isValid() {
			return id == threadid;
		}

		private void notifyCaller() {
			if(callback != null)
				synchronized(callback) { callback.notify(); }
		}
	}
}