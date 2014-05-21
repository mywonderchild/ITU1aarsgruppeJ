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
private PrintWriter wayNameTagWriter;
private PrintWriter wayTypeTagWriter;
private PrintWriter wayDataWriter;
private PrintWriter waySpeedTagWriter;
private PrintWriter coastWriter;
private BufferedReader readwayTypeTag;
private BufferedReader readedgedata;
private BufferedReader readwayNametag;
private BufferedReader readnodedata;
private BufferedReader readwaydata;
private BufferedReader readSpeedtag;
private BufferedReader readCoast;
private File outputNodeData;
private File purgedNodefile;
private File purgedEdgdefile;
private File outputwayNameTag;
private File outputwayTypeTag;
private File outputWayData;
private File waySpeedFile;
private File coastFile;
private ArrayList<String> nodedata;
private ArrayList<String> waydata;
private ArrayList<String> edgedata;
private ArrayList<String> wayNametag;
private	ArrayList<String> wayTypeTag;
private	ArrayList<String> waySpeedTag;
private	ArrayList<String> coastTag;
private HashMap<String,Integer> nodeIDs;
private HashMap<String,String> wayNodes;
private HashMap<Integer,String> wayTypeTagHash;
private HashMap<Integer,String> wayNameTagHash;
private HashMap<Integer,String> waySpeedTagHash;
private HashSet<Integer> coastHash;
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


	public saxEventHandler(){
		try{
		outputNodeData = new File("./nodedata.txt");
		outputwayNameTag = new File("./wayNametag.txt");
		outputwayTypeTag = new File("./wayTypeTag.txt");
		outputWayData = new File("./waydata.txt");
		purgedEdgdefile = new File("./purged_edges.txt");
		purgedNodefile = new File("./purged_nodes.txt");
		waySpeedFile = new File("./wayspeed.txt");
		coastFile = new File("./coast.txt");

		outputNodeData.createNewFile();
		outputwayNameTag.createNewFile();
		outputwayTypeTag.createNewFile();
		outputWayData.createNewFile();
		purgedNodefile.createNewFile();
		purgedEdgdefile.createNewFile();
		waySpeedFile.createNewFile();
		coastFile.createNewFile();
		
		nodeDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputNodeData)));
		wayNameTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputwayNameTag)));
		wayTypeTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputwayTypeTag)));
		waySpeedTagWriter = new PrintWriter(new BufferedWriter(new FileWriter(waySpeedFile)));
		wayDataWriter = new PrintWriter(new BufferedWriter(new FileWriter(outputWayData)));
		purgedNodeWriter = new PrintWriter(new BufferedWriter(new FileWriter(purgedNodefile)));
		purgededgeWriter = new PrintWriter(new BufferedWriter(new FileWriter(purgedEdgdefile)));
		coastWriter = new PrintWriter(new BufferedWriter(new FileWriter(coastFile)));
		readwaydata = new BufferedReader(new FileReader(outputWayData));
		readedgedata = new BufferedReader(new FileReader(outputWayData));
		readwayTypeTag = new BufferedReader(new FileReader(outputwayTypeTag));
		readwayNametag = new BufferedReader(new FileReader(outputwayNameTag));
		readSpeedtag = new BufferedReader(new FileReader(waySpeedFile));
		readnodedata = new BufferedReader(new FileReader(outputNodeData));
		readCoast = new BufferedReader(new FileReader(coastFile));

		}catch (IOException e) {
			e.printStackTrace();
		
		}
		nodedata = new ArrayList<String>();
		wayNametag = new  ArrayList<String>();
		edgedata = new ArrayList<String>();
		wayTypeTag = new ArrayList<String>();
		waySpeedTag = new ArrayList<String>();
		waydata = new ArrayList<String>();
		coastTag = new ArrayList<String>();
		nodeIDs = new HashMap<String,Integer>();
		wayNodes = new HashMap<String,String>();
		wayTypeTagHash = new HashMap<Integer,String>();
		wayNameTagHash = new HashMap<Integer,String>();
		waySpeedTagHash = new HashMap<Integer,String>();
		coastHash = new HashSet<Integer>();
		waynodeset = new HashSet<String>();

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

	// denne metode er hentet fra: http://www.geodatasource.com/developers/java og mildt omskrevet til at udføre vores behov.
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

	//her stopper den hentede kode

	private String getWaygroup(String s){

		if(s.equals("motorway") ||  s.equals("motorway_link")){
			return "1";
		}
		if(s.equals("trunk") || s.equals("trunk_link") || s.equals("primary_link") || s.equals("secondary_link") || s.equals("primary") || s.equals("secondary")){
			return "2";
		}
		if(s.equals("track") || s.equals("footway") || s.equals("byway") || s.equals("bridleway") || s.equals("steps") || s.equals("path") || s.equals("cycleway")){
			return "8";
		}
		if(s.equals("pedestrian") || s.equals("living_street")){
			return "11";
		}
		if(false){
			return "4";
		}
		if(false){
			return "5";
		}
		else{
			return "6";
		}
	}

	private double getLat(String s){
		String use = wayNodes.get((s));
		String node[]= use.split(",");
		return Double.parseDouble(node[2]);
	}
	private double getLon(String s){
		String use = wayNodes.get((s));
		String node[]= use.split(",");
		return Double.parseDouble(node[1]);
	}
	private String convertLat(Double l){
		Double lat = Math.round(10000.0*((l-latmin)*(1000/(latmax-latmin))))/10000.0;

		return String.valueOf(lat);
	}
	private String convertLon(Double l){
		Double lon = Math.round(10000.0*(1000-((l-lonmin)*(1000/(lonmax-lonmin)))))/10000.0;

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
				System.out.println("Loading nodes from OSM file");
			}
			count++;
			whatOpen = "node";
			nodeID = Long.parseLong(attributes.getValue(0));
			nodedata.add(nodeID+","+attributes.getValue(2)+","+attributes.getValue(1));
		}
		if (localName.equals("tag")){
			if (whatOpen.equals("way") && attributes.getValue(0).equals("highway")){
				wayTypeTag.add(wayID+","+attributes.getValue(1));
			}
			if (whatOpen.equals("way") && attributes.getValue(0).equals("name")){
				wayNametag.add(wayID+";'"+attributes.getValue(1).replace("'","")+"'");
			}
			if (whatOpen.equals("way") && attributes.getValue(0).equals("maxspeed") && (attributes.getValue(1).equals("DK:rural")) || (attributes.getValue(1).equals("50;80"))){
				waySpeedTag.add(wayID+",80");
			}
			else if (whatOpen.equals("way") && attributes.getValue(0).equals("maxspeed") && (attributes.getValue(1).equals("DK:urban") || attributes.getValue(1).equals("dk:urban"))){
				waySpeedTag.add(wayID+",50");
			}
			else if (whatOpen.equals("way") && attributes.getValue(0).equals("maxspeed") && !(attributes.getValue(1).equals("*")) && !(attributes.getValue(1).equals("signals")) && !(attributes.getValue(1).equals("signal"))){
				waySpeedTag.add(wayID+","+attributes.getValue(1));
			}
			if (whatOpen.equals("way") && attributes.getValue(1).equals("coastline")){
				coastTag.add(String.valueOf(wayID));
			}
		}
		if (localName.equals("bounds")){
			try{
			double max[] = GeoConvert.toUtm(Double.parseDouble(attributes.getValue(1)),Double.parseDouble(attributes.getValue(0)));
			double min[] = GeoConvert.toUtm(Double.parseDouble(attributes.getValue(3)),Double.parseDouble(attributes.getValue(2)));
			latmax = max[1];
			latmin = min[1];
       		lonmin = min[0];
       		
       		lonmax = max[0];
       		whatOpen = "bounds";
       		}catch (Exception e) {
				System.out.println(e);
				System.exit(0);
			}			
			}
		
		if (localName.equals("way")){
			whatOpen ="way";
			if (!doneNode){
				count = 0;
				for (int i=0;i<nodedata.size() ; i++) {
					nodeDataWriter.write(nodedata.get(i)+"\n");
				}
				
				nodedata.clear();
				System.out.println("Done loading nodes from OSM File");

				doneNode = true;
			}
			count++;
			if (count == 100000){
				for (int i=0;i<wayTypeTag.size() ; i++) {
					if(!(wayTypeTag.get(i)==null)){
						wayTypeTagWriter.write(wayTypeTag.get(i)+"\n");
					}
				}
				for (int i=0;i<waydata.size() ; i++) {
					wayDataWriter.write(waydata.get(i)+"\n");
				}
				for (int i=0;i<wayNametag.size() ; i++) {
					wayNameTagWriter.write(wayNametag.get(i)+"\n");
				}
				for (int i=0;i<waySpeedTag.size() ; i++) {
					waySpeedTagWriter.write(waySpeedTag.get(i)+"\n");
				}
				for (int i=0;i<coastTag.size() ; i++) {
					coastWriter.write(coastTag.get(i)+"\n");
				}
				count = 0;
				waydata.clear();
				wayTypeTag.clear();
				waySpeedTag.clear();
				wayNametag.clear();
				coastTag.clear();
				System.out.println("Loading ways from OSM file");
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
		
		
		for (int i=0;i<wayTypeTag.size() ; i++) {
			wayTypeTagWriter.write(wayTypeTag.get(i)+"\n");
		}
		for (int i=0;i<waydata.size() ; i++) {
			wayDataWriter.write(waydata.get(i)+"\n");
		}
        for (int i=0;i<wayNametag.size() ; i++) {
			wayNameTagWriter.write(wayNametag.get(i)+"\n");
		}
		for (int i=0;i<waySpeedTag.size() ; i++) {
			waySpeedTagWriter.write(waySpeedTag.get(i)+"\n");
		}
		for (int i=0;i<coastTag.size() ; i++) {
			coastWriter.write(coastTag.get(i)+"\n");
		}
        wayTypeTagWriter.close();
        wayNameTagWriter.close();
        nodeDataWriter.close();
		wayDataWriter.close();
		waySpeedTagWriter.close();
		coastWriter.close();
		coastTag.clear();
        edgedata.clear();
        wayTypeTag.clear();
        waydata.clear();
        waySpeedTag.clear();  
		String s = "";
		int q = 0;
		System.out.println("Done loading ways from OSM file\nSorting way tags");


		try{
			while(!((s = readwayTypeTag.readLine()) == null)){
				String wayTag[]= s.split(",");
				wayTypeTagHash.put(Integer.parseInt(wayTag[0]),wayTag[1]);
			}
			s = "";
			while(!((s = readwayNametag.readLine()) == null)){
				String wayNameTager[]= s.split(";");
				wayNameTagHash.put(Integer.parseInt(wayNameTager[0]),wayNameTager[1]);
			}
			s = "";
			while(!((s = readSpeedtag.readLine()) == null)){
				String waySpeedTager[]= s.split(",");
				waySpeedTagHash.put(Integer.parseInt(waySpeedTager[0]),waySpeedTager[1]);
			}
			s = "";
			while(!((s = readCoast.readLine()) == null)){
				coastHash.add(Integer.parseInt(s));
			}
			System.out.println("Done sorting way tags \nSorting ways");
			readCoast.close();

			s = "";
			while(!((s = readwaydata.readLine()) == null)){
				String way[] = s.split(",");
				wayID = Integer.parseInt(way[0]);
				if((!(wayTypeTagHash.get(wayID) == null)) || ((coastHash.contains(wayID)))){
					for(int y = 1; y<way.length ; y++){
						if(!(waynodeset.contains(way[y]))){
							waynodeset.add(way[y]);
						}
					}		
				}
			}
		    System.out.println("Done sorting ways \nParsing nodes and generating node output file");

			s = "";
			q = 0;
			nodedata.clear();
			Integer k = 1;
			purgedNodeWriter.write("1000.0000,1000.0000\n");
			while(!((s = readnodedata.readLine()) == null)){
				String node[] = s.split(",");
				if(waynodeset.contains(node[0])){
					wayNodes.put(String.valueOf(k),String.valueOf(k)+","+node[1]+","+node[2]);
					nodeIDs.put(node[0],k);
					double lat = Double.parseDouble(node[2]);
					double lon = Double.parseDouble(node[1]);
					try{
						double lonlat[] = GeoConvert.toUtm(lon,lat);
						nodedata.add(String.valueOf(k)+","+convertLon(lonlat[0])+","+convertLat(lonlat[1]));
						}catch (Exception e) {
							System.out.println(e);
							System.exit(0);
						}
					
					q++;
					k++;
				}
				if(q==50000){
					System.out.println("Parsing nodes and generating node output file");
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
			System.out.println("Done parsing nodes and generating node output file \nParsing edges and generating edge output file ");
			s = "";
			q = 0;
			while(!((s = readedgedata.readLine()) == null)){
				String way[] = s.split(",");
				wayID = Integer.parseInt(way[0]);
				if(!(wayTypeTagHash.get(wayID) == null)){	
					
					for (int y = 1; y<way.length-1 ; y++) {
						String one = String.valueOf(nodeIDs.get(way[y]));
						String two = String.valueOf(nodeIDs.get(way[y+1])); 
						String distance = distance(getLat(one),getLon(one),getLat(two),getLon(two));
						String wayName = wayNameTagHash.get(wayID);
						String wayType = getWaygroup(wayTypeTagHash.get(wayID));
						String waySpeed = waySpeedTagHash.get(wayID);
						
						
						if(wayName == null){
							if(waySpeed == null){
						 		edgedata.add(one+","+two+","+distance+","+wayType+",'',0,50");
						 	}
						 	else{
						 		edgedata.add(one+","+two+","+distance+","+wayType+",'',0,"+waySpeed);
						 	}
						}
						else{
							if(waySpeed == null){
								edgedata.add(one+","+two+","+distance+","+wayType+","+wayName+",0,50");
							}
							else{
								edgedata.add(one+","+two+","+distance+","+wayType+","+wayName+",0,"+waySpeed);
							}
						}
					}				
				}
				else if(coastHash.contains(wayID)){
					for (int y = 1; y<way.length-1 ; y++) {
						String one = String.valueOf(nodeIDs.get(way[y]));
						String two = String.valueOf(nodeIDs.get(way[y+1])); 
						edgedata.add(one+","+two+",0,81,'',0,0");
					}
				}
				q++;
				if(q==50000){
					System.out.println("Parsing edges and generating edge output file");
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
			System.out.println("Done parsing edges and generating edge output file\nStarting cleanup");
			edgedata.clear();
			readwayTypeTag.close();
			readedgedata.close();
			readwayNametag.close();
			readnodedata.close();
			readSpeedtag.close();
			readwaydata.close();
			purgededgeWriter.close();
			purgedNodeWriter.close();
			outputNodeData.delete();
			outputwayNameTag.delete();
			outputwayTypeTag.delete();
			outputWayData.delete();
			waySpeedFile.delete();
			coastFile.delete();
			System.out.println("Cleanup done!");

			

		}catch (IOException e) {
			e.printStackTrace();
		}


		
		

        
    }

}