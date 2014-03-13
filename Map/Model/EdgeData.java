package Map.Model;

/**
 * Represents the raw data from a line in kdv_unload.txt.
 */
public class EdgeData {
	public final int FNODE; 
	public final int TNODE;
	public final int TYP;

	public String toString() {
		return "";/*
			FNODE + "," +
			TNODE + "," +
			String.format("%.5f,", LENGTH) +
			DAV_DK + "," +
			DAV_DK_ID + "," +
			TYP + "," +
			"'" + VEJNAVN +"'," +
			FROMLEFT + "," +
			TOLEFT + "," +
			FROMRIGHT + "," +
			TORIGHT + "," +
			"'" + FROMLEFT_BOGSTAV + "'," +
			"'" + TOLEFT_BOGSTAV + "'," +
			"'" + FROMRIGHT_BOGSTAV + "'," +
			"'" + TORIGHT_BOGSTAV + "'," +
			V_SOGNENR + "," +
			H_SOGNENR + "," +
			V_POSTNR + "," +
			H_POSTNR + "," +
			KOMMUNENR + "," +
			VEJKODE + "," +
			SUBNET + "," +
			"'" + RUTENR + "'," +
			FRAKOERSEL + "," +
			ZONE + "," +
			SPEED + "," +
			String.format("%.3f,", DRIVETIME) +
			"'" + ONE_WAY + "'," +
			"'" + F_TURN + "'," +
			"'" + T_TURN + "'," +
			(VEJNR == -1 ? "**********," : VEJNR + ",") +
			"'" + AENDR_DATO + "'," +
			TJEK_ID;*/
	}

	public EdgeData(String line) {
		DataLine dl = new DataLine(line);
		FNODE = dl.getInt();
		TNODE = dl.getInt();
		dl.getDouble();
		dl.getInt();
		dl.getInt();
		TYP = dl.getInt();
	}
}
