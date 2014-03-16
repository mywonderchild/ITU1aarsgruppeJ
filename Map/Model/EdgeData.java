package Map.Model;

/**
 * Represents the raw data from a line in kdv_unload.txt.
 */
public class EdgeData {
	public final int FNODE; 
	public final int TNODE;
	public final int TYP;
	public final String VEJNAVN;

	public String toString() {
		return 
			FNODE + "," +
			TNODE + "," +
			TYP + "," +
			"'" + VEJNAVN + "'";
	}

	public EdgeData(String line) {
		DataLine dl = new DataLine(line);
		FNODE = dl.getInt();
		TNODE = dl.getInt();
		TYP = dl.getInt();
		VEJNAVN = dl.getString();
	}
}
