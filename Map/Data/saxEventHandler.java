import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class saxEventHandler extends DefaultHandler{
private PrintWriter writer;
private File outputFile;

	public saxEventHandler(){
		try{
		outputFile = new File("/output.txt");
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
		System.out.printf("File is located at %s%n", outputFile.getAbsolutePath());

	}catch (IOException e) {
			e.printStackTrace();
		
			}
}


	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("node")){
			writer.write("node\""+attributes.getValue(0)+";"+attributes.getValue(1)+";"+attributes.getValue(2)+";");
		}
		if (localName.equals("tag")){
			writer.write("(");
			writer.write(attributes.getValue(0)+"*"+attributes.getValue(1));
		}
		if (localName.equals("bounds")){
			writer.write("bounds\"");
			for ( int i=0 ; i<attributes.getLength() ; i++){
				writer.write(attributes.getValue(i)+";");
			}
		}
		if (localName.equals("way")){
			writer.write("way\""+attributes.getValue(0)+";");
		}
		if (localName.equals("nd")){
			writer.write(attributes.getValue(0)+";");
		}

		if (localName.equals("relation")){
			writer.write("relation\""+attributes.getValue(0)+";");
		}
		if (localName.equals("member")){
			writer.write("[");
			for ( int i=0 ; i<attributes.getLength() ; i++){
				writer.write(attributes.getValue(i)+";");
			}
		}

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("node")){
			writer.write("\" \n");
		}
		if (localName.equals("bounds")){
			writer.write("\" \n");
		}
		if (localName.equals("tag")){
			writer.write(");");
		}
		if (localName.equals("way")){
			writer.write("\" \n");
		}

		if (localName.equals("relation")){
			writer.write("\" \n");
		}	
		if (localName.equals("member")){
			writer.write("];");
		}	
		
	}


	public void endDocument() throws SAXException {
        writer.close();
    }

}