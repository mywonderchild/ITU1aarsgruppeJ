package Map.Controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import Map.Model.Edge;

public class Path {
	
	private List<Edge> edges;	

	public Path(List<Edge> edges) {
		this.edges = edges;
	}

	ArrayList<String> getDirections() {
		ArrayList<String> directions = new ArrayList<>();
		String directionFormat = "%s for %s";
		String currentName = null;
		double currentDistance = 0;
		Iterator<Edge> iterator = edges.iterator();
		while (iterator.hasNext()) {
			Edge edge = iterator.next();
			String name = (edge.NAME != null) ? edge.NAME : "Unknown road";
			if (currentName != null) {
				if (name.equals(currentName) && iterator.hasNext()) {
					currentDistance += edge.LENGTH;
				} else {
					String distance;
					if (currentDistance < 1000)
						distance = String.format("%d %s", Math.round(currentDistance), "meters");
					else
						distance = String.format("%.2f %s", currentDistance / 1000, "kilometers");
					String direction = String.format(directionFormat, currentName, distance);
					System.out.println(direction);
					directions.add(direction);
					currentName = null;
				}
			}
			if (currentName == null) {
				currentName = name;
				currentDistance = edge.LENGTH;
			}
		}
		return directions;
	}
}