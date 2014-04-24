package Map.Controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import Map.Vector;
import Map.Model.Edge;

public class Path {
	
	static String directionFormat = "%s%s%s";
	public List<Edge> edges;
	private Segment[] segments;

	public Path(List<Edge> edges) {

		this.edges = edges;

		// Generate segments
		LinkedHashSet<Segment> set = new LinkedHashSet<>();
		Segment segment = null;
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = edges.get(i);
			String name = (edge.NAME != null) ? edge.NAME : "Unknown road";
			if (segment == null || !name.equals(segment.name)) segment = new Segment(name);
			segment.edges.add(edge);
			set.add(segment);
		}
		segments = set.toArray(new Segment[set.size()]);
	}

	ArrayList<String> getDirections() {
		ArrayList<String> directions = new ArrayList<>();
		for (int i = 0; i < segments.length; i++) {
			Segment segment = segments[i];
			Edge lastEdgeOfPrevious = (i > 0) ? segments[i - 1].last() : null;
			directions.add(getDirection(
				segment.name,
				segment.distance(),
				lastEdgeOfPrevious,
				segment.edges.get(0)
			));
		}
		return directions;
	}

	String getDirection(String name, double distance, Edge one, Edge two) {
		String turn = (one != null) ? getTurn(one, two) : "";
		String distanceString;
		if (distance < 1000)
			distanceString = String.format(" (%d %s)", Math.round(distance), "m");
		else
			distanceString = String.format(" (%.2f %s)", distance / 1000, "km");
		return String.format(directionFormat, turn, name, distanceString);
	}

	String getTurn(Edge one, Edge two) {
		Vector vectorOne = one.END.VECTOR.copy().sub(one.START.VECTOR);
		Vector vectorTwo = two.END.VECTOR.copy().sub(two.START.VECTOR);
		double angle = -vectorOne.angle(vectorTwo);
		if (Math.abs(angle) < Math.PI / 4)
			return "Continue straight, follow ";
		else if (angle > 0)
			return "Turn right, follow ";
		else
			return "Turn left, follow ";
	}

	private class Segment {

		public String name;
		public ArrayList<Edge> edges = new ArrayList<>();

		public Segment(String name) {
			this.name = name;
		}

		public double distance() {
			double distance = 0;
			for (Edge edge : edges) distance += edge.LENGTH;
			return distance;
		}

		public Edge last() {
			return edges.get(edges.size() - 1);
		}
	}
}