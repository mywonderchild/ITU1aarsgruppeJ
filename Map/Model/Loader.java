package Map.Model;

import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

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
		all = new QuadTree(new double[][] {{442254.35659, 6049914.43018}, {892658.21706, 6402050.98297}});
		groups = new QuadTree[Groups.GROUPS.length];
		for(int i = 0; i < groups.length; i++)
			groups[i] = new QuadTree(new double[][] {{442254.35659, 6049914.43018}, {892658.21706, 6402050.98297}});
		
		try {
			System.out.println(new File(".").getCanonicalPath());
			String dir = new File(".").getCanonicalPath() + "/Map/data/";
			load(dir + "purged_kdv_node_unload.txt", dir + "purged_kdv_unload.txt");
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

	public void processNode(NodeData node)
	{
		ndata[node.KDV_ID] = new Node(node);
	}

	public void processEdge(EdgeData edge)
	{
		Node[] nodes = new Node[] {ndata[edge.FNODE], ndata[edge.TNODE]};
		Edge e = new Edge(edge, nodes);
		all.insert(e);
		groups[e.getGroup()].insert(e);
	}
}