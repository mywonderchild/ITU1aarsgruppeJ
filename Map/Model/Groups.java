package Map.Model;

import java.awt.Color;

import Map.Model.Edge;
import Map.View.Tiler;

public class Groups {

	public double zoom;
	public final static int[][] GROUPS;
	public final static int[] GROUPMAP;
	public final static Color[] COLORMAP;
	public final static float[] WIDTHMAP;

	static {
		// We view "Motortrafik" and "Sekundærrute" as main roads
		// We view "Markvej" as a path
		// Road type 0 and 95 are undocumented but present – we added them to other
		GROUPS = new int[][] {
			{1, 21, 31, 41}, // Highways
			{2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44}, // Main roads
			{8, 10, 28, 48}, // Paths
			{11}, // Pedestrian
			{80}, // Naval
			{81}, // Coastline
			{0, 5, 6, 25, 26, 35, 45, 46, 95, 99} // Other
		};

		// Build groupmap
		GROUPMAP = new int[100];
		for (int i = 0; i < GROUPMAP.length; i++)
			GROUPMAP[i] = -1;
		for (int i = 0; i < GROUPS.length; i++)
			for (int j = 0; j < GROUPS[i].length; j++)
				GROUPMAP[GROUPS[i][j]] = i;

		// Build colormap
		COLORMAP = new Color[] {
			Color.RED,
			Color.BLUE,
			Color.GREEN,
			Color.MAGENTA,
			Color.CYAN,
			Color.BLACK,
			Color.BLACK
		};

		WIDTHMAP = new float[] {1.3f, 1.0f, 0.7f, 1.0f, 1.0f, 1.0f, 0.7f};
	}
	

	public static int getGroup(int type) throws RuntimeException {
		int group = GROUPMAP[type];
		if (group != -1)
			return group;
		else
			throw new RuntimeException("Road group not found, type is: " + type);
	}


	private static Color getGroupColor(int group) {
		return COLORMAP[group];
	}

	public static Color getColor(Edge edge) throws RuntimeException {
		return getGroupColor(getGroup(edge.TYPE));
	}

	
	private static float getGroupWidth(int group) {
		return WIDTHMAP[group];
	}

	public static float getWidth(Edge edge) throws RuntimeException {
		return getGroupWidth(getGroup(edge.TYPE));
	}	
}