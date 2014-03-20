package Map.Model;

import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

import Map.Vector;
import Map.Box;
import Map.Controller.Groups;

public class Loader extends KrakLoader
{
	public final Node[] ndata;
	public final QuadTree all;
	public final QuadTree[] groups;

	public Loader()
	{
		// Low X:  442 254,35659
		// High X: 892 658,21706
		// Low Y:  6 049 914,43018
		// High Y: 6 402 050,98297
		ndata = new Node[700000];
		Vector start = new Vector(442254.35659, 6049914.43018);
		Vector stop = new Vector(892658.21706, 6402050.98297);
		Box box = new Box(start, stop);
		all = new QuadTree(box);
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new QuadTree(box);
		try {
			System.out.println(new File(".").getCanonicalPath());
			String dir = new File(".").getCanonicalPath() + "/Map/data/";
			load(dir + "purged_kdv_node_unload.txt", dir + "purged_kdv_unload.txt");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void processNode(Node node) {
		ndata[node.KDV_ID] = node;
	}

	public void processEdge(Edge edge) {
		edge.setNodes(ndata[edge.FNODE], ndata[edge.TNODE]);
		all.insert(edge);
		groups[edge.getGroup()].insert(edge);
	}
}