import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.lang.Double;
import java.util.HashMap;
import java.io.FileReader;

public class saxEventHandler extends DefaultHandler{
private PrintWriter nodeDataWriter;
private PrintWriter nodeTagWriter;
private PrintWriter edgeDataWriter;
private PrintWriter edgeTagWriter;
private PrintWriter wayDataWriter;
private BufferedReader readedgetag;
private BufferedReader readwaydata;
private File outputNodeData;
private File outputNodeTag;
private File outputEdgeData;
private File outputEdgeTag;
private File outputWayData;
private ArrayList<String> nodedata;
private ArrayList<String> waydata;
private ArrayList<String> edgedata;
private HashMap<String,Integer> nodeIDs;
private ArrayList<String> nodetag;
private	ArrayList<String> edgetag;
private String whatOpen;
private int nodeID;
private int wayID;
private String tempValue;
private Double latmin;
private Double lonmin;
private Double latmax;
private Double lonmax;
private boolean doneNode;
private int count;

	public saxEventHandler(){
		try{
		outputNodeData = new File("/nodedata.txt");
		outputNodeTag = new File("/nodetag.txt");
		outputEdgeData = new File("/edgedata.txt");
		outputEdgeTag = new File("/edgetag.txt");
		outputWayData = new File("/waydata.txt");


		if(!outputNodeData.exists()){
			outputNodeData.createNewFile();
		}
		if(!outputNodeTag.exists()){
			outputNodeTag.createNewFile();
		}
		if(!outputEdgeData.exists()){
			outputEdgeData.createNewFile();
		}
		if(!outputEdgeTag.exists()){
			outputEdgeTag.createNewFile();
		}
		if(!outputWayData.exists()){
			outputWayData.createNewFile();
		}
		nodeDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputNodeData)));
		nodeTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputNodeTag)));
		edgeDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputEdgeData)));
		edgeTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputEdgeTag)));
		wayDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputWayData)));
		readwaydata = new BufferedReader(new FileReader(outputWayData));
		readedgetag = new BufferedReader(new FileReader(outputEdgeTag));
		System.out.printf("File is located at %s%n", outputWayData.getAbsolutePath());

		}catch (IOException e) {
			e.printStackTrace();
		
		}
		nodedata = new ArrayList<String>();
		nodetag = new  ArrayList<String>();
		edgedata = new ArrayList<String>();
		edgetag = new ArrayList<String>();
		waydata = new ArrayList<String>();
		nodeIDs = new HashMap<String,Integer>();
		whatOpen = "";
		nodeID = 0;
		wayID = 0;
		tempValue = "";
		latmin = 0.0;
		lonmin = 0.0;
		latmax = 0.0;
		lonmax = 0.0;
		doneNode = false;
		count = 0;


	}



	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("node")){
			if (count == 300000){
				for (int i=0;i<nodedata.size() ; i++) {
					nodeDataWriter.write(nodedata.get(i)+"\n");
				}
				
				for (int i=0;i<nodetag.size() ; i++) {
					nodeTagWriter.write(nodetag.get(i)+"\n");

				}
				nodedata.clear();
				nodetag.clear();
				count = 0;
				System.out.println("flush"+nodeID);
			}
			count++;
			whatOpen = "node";
			nodeID++;
			nodeIDs.put(attributes.getValue(0),nodeID);
			nodedata.add(nodeID+","+attributes.getValue(1)+","+attributes.getValue(2));
		}
		if (localName.equals("tag")){
			if(whatOpen.equals("node") && (attributes.getValue(0).equals("addr:street"))){
			
			nodetag.add(nodeID+","+attributes.getValue(1));
			}
			if (whatOpen.equals("way")){
				if(edgetag.size()==wayID){
					edgetag.add(wayID-1,edgetag.get(wayID-1)+attributes.getValue(1));
				}
				else edgetag.add(wayID+","+attributes.getValue(1));
			}
		}
		if (localName.equals("bounds")){
			latmin = Double.parseDouble(attributes.getValue(0));
       		lonmin = Double.parseDouble(attributes.getValue(1));
       		latmax = Double.parseDouble(attributes.getValue(2));
       		lonmax = Double.parseDouble(attributes.getValue(3));
       		whatOpen = "bounds";			
			}
		
		if (localName.equals("way")){
			whatOpen ="way";
			if (!doneNode){
				System.out.println("dinmor");
				count = 0;
				for (int i=0;i<nodedata.size() ; i++) {
					nodeDataWriter.write(nodedata.get(i)+"\n");
				}
				nodeDataWriter.close();
				nodedata.clear();
				for (int i=0;i<nodetag.size() ; i++) {
					nodeTagWriter.write(nodetag.get(i)+"\n");

				}
				nodeTagWriter.close();
				nodetag.clear();
				doneNode = true;
			}
			count++;
			if (count == 100000){
				for (int i=0;i<edgetag.size() ; i++) {
					if(!(edgetag.get(i)==null)){
						edgeTagWriter.write(edgetag.get(i)+"\n");
					}
				}
				for (int i=0;i<waydata.size() ; i++) {
					wayDataWriter.write(waydata.get(i)+"\n");
				}
				count = 0;
				waydata.clear();
				edgetag.clear();
				System.out.println("flushway"+wayID);
			}
			wayID++;
			tempValue=Integer.toString(wayID);
		}
		if (localName.equals("nd")){
			tempValue = tempValue+","+nodeIDs.get(attributes.getValue(0));
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("node")){
			
		}
		if (localName.equals("bounds")){

		}
		if (localName.equals("tag")){
			
		}
		if (localName.equals("way")){
			waydata.add(tempValue);
			tempValue = "";
		}

		if (localName.equals("relation")){
			
		}	
		if (localName.equals("member")){
			
		}	
		
	}


	public void endDocument() throws SAXException {
		// for (int i=0; i<waydata.size();i++){
		// 	String way[] = waydata.get(i).split(",");
		// 	for (int y=1;y<way.length-1 ;y++ ) {
		// 		 // System.out.println(way[y]+","+way[y+1]);
		// 		edgedata.add(way[y]+","+way[y+1]+",3.0,1,'markvej'");
		// 	}
		// 	// System.out.println();
		// }

		
		for (int i=0;i<edgetag.size() ; i++) {
			edgeTagWriter.write(edgetag.get(i)+"\n");
		}
		for (int i=0;i<waydata.size() ; i++) {
			wayDataWriter.write(waydata.get(i)+"\n");
		}
        
        edgeTagWriter.close();
        wayDataWriter.close();
        edgedata.clear();
        edgetag.clear();
        waydata.clear();
		nodeIDs.clear();     
		String s = "";
		int q = 0;
		try{
			while(!((s = readwaydata.readLine()) == null)){
				edgedata.add(s);
				q++;
				if(q==100000){
					for (int i=0;i<edgedata.size() ; i++) {
						edgeDataWriter.write(edgedata.get(i)+"\n");
					}
					q = 0;
				}
			}
		}
		
		edgeDataWriter.close();
        
    }

}