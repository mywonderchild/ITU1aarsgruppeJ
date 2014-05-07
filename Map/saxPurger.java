import java.io.IOException;


import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;




public class saxPurger extends DefaultHandler {


public static void main(String args[]) throws Exception{
	XMLReader reader;
	saxEventHandler eHandler = new saxEventHandler();
	saxFilter filter = new saxFilter();
	reader = XMLReaderFactory.createXMLReader();
	filter.setParent(reader);
	filter.setContentHandler(eHandler);
	filter.parse("./Data/osm/denmark-latest.osm");
	// reader.setContentHandler(eHandler);
	// reader.parse(args[0]);
}


}