import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class DataPurger
{
	BufferedReader in;
	PrintWriter out;

	public DataPurger(String fileIn, String fileOut) throws IOException {
		in = new BufferedReader(new FileReader(new File(fileIn)));
		out = new PrintWriter(fileOut, "UTF-8");
	}

	public void purge(int[] indexes) throws IOException {
		in.readLine(); // skip 1st
		ArrayList<String[]> data = new ArrayList<String[]>();
		String line = null;
		while((line = in.readLine()) != null) {
			ArrayList<String> splitLine = new ArrayList<String>();
			int comma = -1;
			while((comma = line.indexOf(",")) != -1) {
				if(!"'".equals(String.valueOf(line.charAt(0)))) {
					splitLine.add(line.substring(0,comma));
					line = line.substring(comma+1, line.length());
				}
				else {
					// string case
					splitLine.add(line.substring(0,line.indexOf("'", 1)+1));
					line = line.substring(line.indexOf("'", 1)+2,line.length());
				}
			}
			splitLine.add(line);
			String[] purgedLine = new String[indexes.length];
			for(int i = 0; i < indexes.length; i++)
				purgedLine[i] = splitLine.get(indexes[i]);
			data.add(purgedLine);
		}

		for(String[] dataLine : data) {
			String concat = "";
			for(String s : dataLine)
				concat += s + ",";
			concat = concat.substring(0, concat.length()-1);
			out.println(concat);
		}
		in.close();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		if(args.length != 3) {
			System.out.println("DataPurger takes exactly 3 arguments: input file, outputfile and comma separated indexes");
			return;
		}

		DataPurger dataPurger = new DataPurger(args[0], args[1]);
		String[] split = args[2].split(",");
		int[] indexes = new int[split.length];
		for(int i = 0; i < split.length; i++)
			indexes[i] = Integer.parseInt(split[i]);
		dataPurger.purge(indexes);
	}
}