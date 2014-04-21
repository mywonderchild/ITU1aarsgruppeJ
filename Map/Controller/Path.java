package Map.Controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import Map.Model.Edge;

public class Path {
	
	static String directionFormat = "%s for %s";
	public List<Edge> edges;

	public Path(List<Edge> edges) {
		this.edges = edges;
	}

	ArrayList<String> getDirections() {
		ArrayList<String> directions = new ArrayList<>();
		String currentName = null;
		double currentDistance = 0;
		Iterator<Edge> iterator = edges.iterator();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			String name = (edge.NAME != null) ? edge.NAME : "Unknown road";
			if (currentName != null && name.equals(currentName)) {
				currentDistance += edge.LENGTH;
			} else {
				if (currentName != null) directions.add(getDirection(currentName, currentDistance));
				currentName = name;
				currentDistance = edge.LENGTH;
			}
			if (!iterator.hasNext()) directions.add(getDirection(currentName, currentDistance));
		}
		return directions;
	}

	String getDirection(String name, double distance) {
		String distanceString;
		if (distance < 1000)
			distanceString = String.format("%d %s", Math.round(distance), "meters");
		else
			distanceString = String.format("%.2f %s", distance / 1000, "kilometers");
		return String.format(directionFormat, name, distanceString);
	}
}