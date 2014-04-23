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
		String best = null;
		int bestDist = Integer.MAX_VALUE;

		for(String key : map.keySet()) {
			int dist = dist(address, key);
			if(dist < bestDist) {
				best = key;
				bestDist = dist;
			}
		}
		return best;
	}

	private static int dist(String s1, String s2) {
		// Initialize distance matrix between s1 and s2.
		// As [0][1] and [1][0] are empty strings, the
		// distance to these must be the length of the
		// opposing string.
		// Thus distance [i][0] and [0][i] must be i.

		int[][] d = new int[s1.length()][s2.length()];
		for(int i = 0; i < s1.length(); i++)
			d[i][0] = i;
		for(int i = 0; i < s2.length(); i++)
			d[0][i] = i;

		// We do not care about
		// lower/upper case discrepancies.
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		// Compare each char to every char
		// in the opposing string.
		for(int i = 1; i < s1.length(); i++) {
			for(int j = 1; j < s2.length(); j++) {
				// If two chars are equal, then dist = 0.
				if(s1.charAt(i) == s2.charAt(j)) {
					d[i][j] = d[i-1][j-1]; // NW: no action
				}
				else {
					d[i][j] = min(
						d[i-1][j] + 1,	// del
						d[i][j-1] + 1, 	// ins
						d[i-1][j-1] + 1 // sub
					);
				}
			}
		}

		return d[s1.length()-1][s2.length()-1];
	}

	private static int min(int... args) {
		int min = Integer.MAX_VALUE;
		for(int i : args)
			if(i < min) min = i;
		return min;
	}

	public static void main(String[] args) {
		TreeMap<String, String> addresses = new TreeMap<String, String>();
		addresses.put("Rued Langgards Vej","Rued Langgards Vej");
		addresses.put("Brydes Allé","Brydes Allé");
		addresses.put("Kongens Nytorv","Kongens Nytorv");
		addresses.put("Strøget","Strøget");
		addresses.put("Kaj Munks Vej","Kaj Munks Vej");
		AddressFinder af = new AddressFinder(addresses);

		System.out.println(af.find("rudslangardsvej"));
		System.out.println(af.find("rued"));
		System.out.println(af.find("bds ll"));
	}
}