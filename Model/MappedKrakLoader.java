import java.util.ArrayList;
import java.io.IOException;

public class MappedKrakLoader extends KrakLoader
{
	public QuadTree ndata;

	public boolean first = true;
	public double hiX, hiY, loX, loY;

	public MappedKrakLoader()
	{
		// High X: 892658,21706
		// Low X: 442254,35659
		// Middle X: 667456.2868250001
		// High Y: 6402050,98297
		// Low Y: 6049914,43018
		// Middle Y: 6225982.706575001
		// Size: 450403.86047 (lowest distance between low and high x/y)

		ndata = new QuadTree(667456.2868250001, 6225982.706575001, 450403.86047);
	}

	public void processNode(NodeData node)
	{
		ndata.insert((Locatable) node);
		if(first)
		{
			hiX = node.X_COORD;
			loX = node.X_COORD;
			hiY = node.Y_COORD;
			loY = node.Y_COORD;
			first = false;
		}
		if(node.X_COORD > hiX) hiX = node.X_COORD;
		if(node.X_COORD < loX) loX = node.X_COORD;
		if(node.Y_COORD > hiY) hiY = node.Y_COORD;
		if(node.Y_COORD < loY) loY = node.Y_COORD;
	}

	public void processEdge(EdgeData node)
	{
		
	}

	public static void main(String[] args) throws IOException {
		MappedKrakLoader mkp = new MappedKrakLoader();

		String dir = "../data/";
		mkp.load(dir + "kdv_node_unload.txt", dir + "kdv_unload.txt");

		System.out.println(mkp.ndata.nodeCount);
		System.out.println(mkp.ndata.insertCalls);
		System.out.println("High X: " + mkp.hiX);
		System.out.println("Low X: " + mkp.loX);
		System.out.println("Middle X: " + (mkp.loX + (mkp.hiX - mkp.loX)/2));
		System.out.println("High Y: " + mkp.hiY);
		System.out.println("Low Y: " + mkp.loY);
		System.out.println("Middle Y: " + (mkp.loY + (mkp.hiY - mkp.loY)/2));
	}
}