package Map.Controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.lang.Iterable;

import Map.Model.Edge;

public class AddressFinder {
	Map<String, String> map;

	public AddressFinder(Map<String, String> roads) {
		this.map = roads;
	}

	public String find(String address) {
		return find(address, 1)[0];
	}

	public String[] find(String address, int amount) {
		String best[] = new String[amount];
		int bestDist[] = new int[amount];
		for(int i = 0; i < bestDist.length; i++)
			bestDist[i] = Integer.MAX_VALUE;
		int cutoff = Integer.MAX_VALUE;

		for(String key : map.keySet()) {
			int dist = dist(address, key);
			if(dist < cutoff) {
				for(int i = 0; i < bestDist.length; i++) {
					if(dist < bestDist[i]) {
						best[i] = key;
						bestDist[i] = dist;
						if(i == bestDist.length-1) cutoff = dist;
						break;
					}
				}
			}
		}
		return best;
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
						d[i][j-1] + (j<c1.length ? 1 : 0), 	// ins, free if prefix
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
}