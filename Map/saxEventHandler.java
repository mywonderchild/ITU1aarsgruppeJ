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
import java.lang.Integer;
import java.lang.Long;
import java.util.HashMap;
import java.io.FileReader;
import java.util.HashSet;


public class saxEventHandler extends DefaultHandler{
private PrintWriter purgedNodeWriter;
private HashSet<String> waynodeset;
private PrintWriter purgededgeWriter;
private PrintWriter nodeDataWriter;
private PrintWriter nodeTagWriter;
private PrintWriter edgeDataWriter;
private PrintWriter edgeTagWriter;
private PrintWriter wayDataWriter;
private BufferedReader readedgetag;
private BufferedReader readedgedata;
private BufferedReader readnodetag;
private BufferedReader readnodedata;
private BufferedReader readwaydata;
private File outputNodeData;
private File purgedNodefile;
private File purgedEdgdefile;
private File outputNodeTag;
private File outputEdgeData;
private File outputEdgeTag;
private File outputWayData;
private ArrayList<String> nodedata;
private ArrayList<String> waydata;
private ArrayList<String> edgedata;
private HashMap<String,Integer> nodeIDs;
private HashMap<String,String> wayNodes;
private HashMap<Integer,String> wayTagHash;
private HashMap<Integer,String> nodeTagHash;
private ArrayList<String> nodetag;
private	ArrayList<String> edgetag;
private String whatOpen;
private long nodeID;
private int wayID;
private String tempValue;
private Double latmin;
private Double lonmin;
private Double latmax;
private Double lonmax;
private boolean doneNode;
private int count;
private String testString;

	public saxEventHandler(){
		try{
		outputNodeData = new File("/nodedata.txt");
		outputNodeTag = new File("/nodetag.txt");
		outputEdgeData = new File("/edgedata.txt");
		outputEdgeTag = new File("/edgetag.txt");
		outputWayData = new File("/waydata.txt");
		purgedEdgdefile = new File("/purged_edges.txt");
		purgedNodefile = new File("/purged_nodes.txt");


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
		if(!purgedNodefile.exists()){
			purgedNodefile.createNewFile();
		}
		if(!purgedEdgdefile.exists()){
			purgedEdgdefile.createNewFile();
		}
		nodeDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputNodeData)));
		nodeTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputNodeTag)));
		edgeDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputEdgeData)));
		edgeTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputEdgeTag)));
		wayDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputWayData)));
		purgedNodeWriter = new PrintWriter(new BufferedWriter(new FileWriter(purgedNodefile)));
		purgededgeWriter = new PrintWriter(new BufferedWriter(new FileWriter(purgedEdgdefile)));
		readwaydata = new BufferedReader(new FileReader(outputWayData));
		readedgedata = new BufferedReader(new FileReader(outputWayData));
		readedgetag = new BufferedReader(new FileReader(outputEdgeTag));
		readnodetag = new BufferedReader(new FileReader(outputNodeTag));
		readnodedata = new BufferedReader(new FileReader(outputNodeData));
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
		wayNodes = new HashMap<String,String>();
		wayTagHash = new HashMap<Integer,String>();
		nodeTagHash = new HashMap<Integer,String>();
		waynodeset = new HashSet<String>();

		testString = "";
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

	private String distance(double lat1, double lon1, double lat2, double lon2) {
  		double theta = lon1 - lon2;
	 	double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
	  	dist = rad2deg(dist);
	  	dist = dist * 60 * 1.1515 * 1609.344;
	  	return String.valueOf((dist));
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double deg2rad(double deg) {
	  	return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private double rad2deg(double rad) {
	  	return (rad * 180 / Math.PI);
	}

	private String getWaygroup(String s){


		if(s.equals("motorway") || s.equals("rest_area") || s.equals("services") || s.equals("yes") ||  s.equals("motorway_link")){
			return "0";
		}
		if(s.equals("trunk") || s.equals("trunk_link")){
			return "1";
		}
		if(s.equals("track") || s.equals("footway") || s.equals("byway") || s.equals("bridleway") || s.equals("steps") || s.equals("path") || s.equals("cycleway")){
			return "2";
		}
		if(s.equals("pedestrian") || s.equals("living_street")){
			return "3";
		}
		if(false){
			return "4";
		}
		if(false){
			return "5";
		}
		if(s.equals("primary") || s.equals("parking_aisle") ||s.equals("bus_stop") || s.equals("racetrack") || s.equals("raceway") || s.equals("proposed") || s.equals("construction") || s.equals("secondary") || s.equals("tertiary") || s.equals("unclassified") || s.equals("residential") ||s.equals("service") || s.equals("primary_link") || s.equals("secondary_link") || s.equals("tertiary_link") || s.equals("bus_guideway") || s.equals("road")){
			return "6";
		}
		else{
			testString = testString+","+s;
			return "6";
		}
	}

	private double getLat(String s){
		// System.out.println(s);
		String use = wayNodes.get((s));
		// System.out.println("dette er use:"+use);
		String node[]= use.split(",");
		return Double.parseDouble(node[2]);
	}
	private double getLon(String s){
		String use = wayNodes.get((s));
		String node[]= use.split(",");
		return Double.parseDouble(node[1]);
	}
	private String convertLat(String s){
		return String.valueOf(1000-((Double.parseDouble(s)-latmin)*(1000/(latmax-latmin))));
	}
	private String convertLon(String s){
		double lon =((Double.parseDouble(s)-lonmin)*(1000/(lonmax-lonmin)));

		return String.valueOf(lon);
	}


	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("node")){
			if (count == 300000){
				for (int i=0;i<nodedata.size() ; i++) {
					nodeDataWriter.write(nodedata.get(i)+"\n");
				}
				
				nodedata.clear();
				count = 0;
				System.out.println("flush");
			}
			count++;
			whatOpen = "node";
			nodeID = Long.parseLong(attributes.getValue(0));
			// nodeID++;
			// nodeIDs.put(attributes.getValue(0),nodeID);
			nodedata.add(nodeID+","+attributes.getValue(2)+","+attributes.getValue(1));
		}
		if (localName.equals("tag")){
			// if(whatOpen.equals("node") && (attributes.getValue(0).equals("name"))){
			// 	nodetag.add(nodeID+";'"+attributes.getValue(1)+"'");
			// }
			if (whatOpen.equals("way") && attributes.getValue(0).equals("highway")){
				edgetag.add(wayID+","+attributes.getValue(1));
			}
			if (whatOpen.equals("way") && attributes.getValue(0).equals("name")){
				nodetag.add(wayID+";'"+attributes.getValue(1)+"'");
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
				
				nodedata.clear();
				
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
				for (int i=0;i<nodetag.size() ; i++) {
					nodeTagWriter.write(nodetag.get(i)+"\n");

				}
				count = 0;
				waydata.clear();
				edgetag.clear();
				nodetag.clear();
				System.out.println("flushway"+wayID);
			}
			wayID++;
			tempValue=Integer.toString(wayID);
		}
		if (localName.equals("nd")){
			tempValue = tempValue+","+attributes.getValue(0);
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


	public void endDocument() throws SAXException{
		
		
		for (int i=0;i<edgetag.size() ; i++) {
			edgeTagWriter.write(edgetag.get(i)+"\n");
		}
		for (int i=0;i<waydata.size() ; i++) {
			wayDataWriter.write(waydata.get(i)+"\n");
		}
        for (int i=0;i<nodetag.size() ; i++) {
			nodeTagWriter.write(nodetag.get(i)+"\n");
		}
        edgeTagWriter.close();
        nodeTagWriter.close();
        nodeDataWriter.close();
        edgedata.clear();
        edgetag.clear();
        waydata.clear();   
		String s = "";
		int q = 0;

		try{
			while(!((s = readedgetag.readLine()) == null)){
				String wayTag[]= s.split(",");
				wayTagHash.put(Integer.parseInt(wayTag[0]),wayTag[1]);
			}
			while(!((s = readnodetag.readLine()) == null)){
				String nodeTag[]= s.split(";");
				nodeTagHash.put(Integer.parseInt(nodeTag[0]),nodeTag[1]);
				// System.out.println(nodeTagHash.get(Integer.parseInt(nodeTag[0])));
			}

			s = "";
			while(!((s = readwaydata.readLine()) == null)){
				// System.out.println(s+"\n");
				String way[] = s.split(",");
				wayID = Integer.parseInt(way[0]);
				if(!(wayTagHash.get(wayID) == null)){
					// waydata.add(s);
					for(int y = 1; y<way.length ; y++){
						if(!(waynodeset.contains(way[y]))){
							waynodeset.add(way[y]);
							// System.out.println(way[y]);
						}
					}		
				}
			}
			s = "";
			q = 0;
			nodedata.clear();
			Integer k = 1;
			purgedNodeWriter.write("1000.0000,1000.0000\n");
			while(!((s = readnodedata.readLine()) == null)){
				String node[] = s.split(",");
				// if (node[0].equals("")){
				// System.out.println("Nu kommer den:"+node[0]);
				// }
				// waynodeset.contains(node[0])
				if(waynodeset.contains(node[0])){
					// System.out.println("YAHOOO!");
					wayNodes.put(String.valueOf(k),String.valueOf(k)+","+node[1]+","+node[2]);
					nodeIDs.put(node[0],k);
					nodedata.add(String.valueOf(k)+","+convertLon(node[1])+","+convertLat(node[2]));
					q++;
					k++;
					// if(node[0].equals("9415")){
					// 	System.out.println("DEN ER HER");
					// }
				}
				if(q==100000){
					System.out.println("purgednodes");
					for (int i=0;i<nodedata.size() ; i++) {
						purgedNodeWriter.write(nodedata.get(i)+"\n");
					}
					nodedata.clear();
					q = 0;
				}
			}
			for (int i=0;i<nodedata.size() ; i++) {
				purgedNodeWriter.write(nodedata.get(i)+"\n");
			}
			nodedata.clear();
			s = "";
			q = 0;
			while(!((s = readedgedata.readLine()) == null)){
				// System.out.println("muuh");
				String way[] = s.split(",");
				wayID = Integer.parseInt(way[0]);
				if(!(wayTagHash.get(wayID) == null)){	
					
					for (int y = 1; y<way.length-1 ; y++) {
						String one = String.valueOf(nodeIDs.get(way[y]));
						String two = String.valueOf(nodeIDs.get(way[y+1])); 
						String distance = distance(getLat(one),getLon(one),getLat(two),getLon(two));
						String wayName = nodeTagHash.get(wayID);
						String wayType = getWaygroup(wayTagHash.get(wayID));
						// System.out.println(onetag+"   "+twotag);
						if(wayName == null){
							// System.out.println("NO name");
						 	edgedata.add(one+","+two+","+distance+","+wayType+",'',0,50");
						}
						else{
							// System.out.println("noget");							
							edgedata.add(one+","+two+","+distance+","+wayType+","+wayName+",0,80");
						}
					}				
				}
				q++;
				if(q==100000){
					System.out.println("dinmor3");
					for (int i=0;i<edgedata.size() ; i++) {
						 purgededgeWriter.write(edgedata.get(i)+"\n");
					}
					q = 0;
					edgedata.clear();
				}
			}
			for (int i=0;i<edgedata.size() ; i++) {
			purgededgeWriter.write(edgedata.get(i)+"\n");
			}
			edgedata.clear();

		}catch (IOException e) {
			System.out.println("SKRIV ET ELLER ANDET SJOVT");
		}

		edgeDataWriter.close();
		purgededgeWriter.close();
		purgedNodeWriter.close();
		wayDataWriter.close();
		System.out.println(testString);

        
    }

}