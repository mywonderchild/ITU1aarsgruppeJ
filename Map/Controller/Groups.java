package Map.Controller;

import java.awt.Color;

public class Groups {

	public final static int[][] GROUPS;

	static {
		// Groups
		// We view "Motortrafik" and "Sekundærrute" as main roads
		// We view "Markvej" as a path
		// Road type 0 and 95 are undocumented but present – we added them to other
		GROUPS = new int[6][];
		GROUPS[0] = new int[]{1, 21, 31, 41}; // Highways
		GROUPS[1] = new int[]{2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44}; // Main roads
		GROUPS[2] = new int[]{8, 10, 28, 48}; // Paths
		GROUPS[3] = new int[]{11}; // Pedestrian
		GROUPS[4] = new int[]{80}; // Naval
		GROUPS[5] = new int[]{0, 5, 6, 25, 26, 35, 45, 46, 95, 99}; // Other
	}

	public static Color getGroupColor(int group) throws RuntimeException {

		// Return color
		switch(group) {
			case 0:
				return Color.RED;
			case 1:
				return Color.BLUE;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.YELLOW;
			case 4:
				return Color.CYAN;
			case 5:
				return Color.BLACK;
			default:
				throw new RuntimeException("Road group " + group + " is not recognized");
		}
	}
}