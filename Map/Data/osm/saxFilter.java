import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.lang.String;

public class saxFilter extends XMLFilterImpl
{

	private String lastopen;

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("node")){
		super.endElement(uri, localName, qName);
		}
		if (localName.equals("bounds")){
		super.endElement(uri, localName, qName);
		}
		if (localName.equals("tag")){
			if(lastopen.equals("addr:street") || lastopen.equals("highway")  || lastopen.equals("maxspeed") || lastopen.equals("natural")){
				super.endElement(uri, localName, qName);
			}
		}
		if (localName.equals("way")){
		super.endElement(uri, localName, qName);
		}

		if (localName.equals("relation")){
		super.endElement(uri, localName, qName);
		}	
		if (localName.equals("member")){
		super.endElement(uri, localName, qName);
		}
		if (localName.equals("nd")){
		super.endElement(uri, localName, qName);
		}		
		
	}


    public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

       	AttributesImpl newattribute = new AttributesImpl(attributes);

       	if (localName.equals("tag")){
			if (attributes.getValue(0).equals("name") || attributes.getValue(0).equals("highway")  || attributes.getValue(0).equals("maxspeed") || (attributes.getValue(0).equals("natural") && attributes.getValue(1).equals("coastline"))){

				super.startElement(uri, localName, qName, newattribute);
			}
			lastopen = attributes.getValue(0);
			}


       	if (localName.equals("bounds")){

       		super.startElement(uri, localName, qName, newattribute);
       	} 
       	if (localName.equals("node")){

			super.startElement(uri, localName, qName, newattribute);
		}
		if (localName.equals("way")){

			super.startElement(uri, localName, qName, newattribute);
		}
		if (localName.equals("nd")){

			super.startElement(uri, localName, qName, newattribute);
		}
		if (localName.equals("relation")){

			super.startElement(uri, localName, qName, newattribute);
		}
		if (localName.equals("member")){

			super.startElement(uri, localName, qName, newattribute);
		}
	}
}