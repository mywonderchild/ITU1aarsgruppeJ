package Map.Controller;

import java.util.Map;
import java.util.List;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import Map.View.Canvas;
import Map.View.Window;
import Map.View.Tiler;
import Map.Model.Loader;
import Map.Model.Edge;
import Map.Model.ShortestPath;

public class AddressButtonListener implements ActionListener {
	private final JTextField tf1, tf2;
	private final Canvas canvas;
	private final Window window;
	private final Tiler tiler;
	private final Loader loader;
	private final Map<String,List<Edge>> addresses;
	
	public AddressButtonListener(JTextField tf1, JTextField tf2, Canvas canvas, Window window, Tiler tiler, Loader loader) {
		this.tf1 = tf1;
		this.tf2 = tf2;
		this.canvas = canvas;
		this.window = window;
		this.tiler = tiler;
		this.loader = loader;

		addresses = loader.addresses;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Edge> edges1 = addresses.get(tf1.getText());
		List<Edge> edges2 = addresses.get(tf2.getText());
		if(edges1 == null && edges2 == null) {
			System.out.println("Neither from or to found!");
			return;
		} else if(edges1 == null) {
			System.out.println("From not found!");
			return;
		} else if(edges2 == null) {
			System.out.println("To not found!");
			return;
		}

		ShortestPath sp = new ShortestPath(loader.graph, edges1.get(0).START.ID);
		tiler.path = sp.pathTo(edges2.get(0).START.ID);
		window.setDirections(tiler.path.getDirections());
		if(tiler.path == null) System.out.println("No path found!");
	}
}