import java.util.ArrayList;
import java.io.IOException;

public class Loader extends KrakLoader
{
	public Node[] ndata;
	public QuadTree edata;

	public Loader()
	{
		// Low X:  442 254,35659
		// High X: 892 658,21706
		// Low Y:  6 049 914,43018
		// High Y: 6 402 050,98297

		ndata = new Node[700000];
		edata = new QuadTree(new double[][] {{442254.35659, 6049914.43018}, {892658.21706, 6402050.98297}});
	}

	public void processNode(NodeData node)
	{
		ndata[node.KDV_ID] = new Node(node);
	}

	public void processEdge(EdgeData edge)
	{
		Node[] nodes = new Node[] {ndata[edge.FNODE], ndata[edge.TNODE]};
		edata.insert(new Edge(edge, nodes));
	}

	public static void main(String[] args) throws IOException {
		Loader mkp = new Loader();

		String dir = "../data/";
		mkp.load(dir + "kdv_node_unload.txt", dir + "kdv_unload.txt");
		System.out.println(mkp.edata.count);
		
		ArrayList<Edge> edges = mkp.edata.queryRange(new double[][] {{442254, 6049914}, {443254, 6402050}});
		for(Edge edge : edges) {
			System.out.println(edge.toString());
		}
	}
}