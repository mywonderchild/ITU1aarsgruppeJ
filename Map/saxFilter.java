import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.lang.Float;
import java.lang.String;

public class saxFilter extends XMLFilterImpl
{

	private Float latmin;
	private Float lonmin;
	private Float latmax;
	private Float lonmax;
	private String lastopen;

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (localName.equals("node")){
		super.endElement(uri, localName, qName);
		}
		if (localName.equals("bounds")){
		super.endElement(uri, localName, qName);
		}
		if (localName.equals("tag")){
			if(lastopen.equals("addr:street") || lastopen.equals("highway")){
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
			if (attributes.getValue(0).equals("name") || attributes.getValue(0).equals("highway")){

				super.startElement(uri, localName, qName, newattribute);
			}
			lastopen = attributes.getValue(0);
			}


       	if (localName.equals("bounds")){

       		// latmin = Float.parseFloat(attributes.getValue(0));
       		// lonmin = Float.parseFloat(attributes.getValue(1));
       		// latmax = Float.parseFloat(attributes.getValue(2));
       		// lonmax = Float.parseFloat(attributes.getValue(3));
       		super.startElement(uri, localName, qName, newattribute);
       	} 
       	if (localName.equals("node")){

			// newattribute.setValue(1,String.valueOf((Float.parseFloat(attributes.getValue(1))-latmin)*(1000/(latmax-latmin))));
			// newattribute.setValue(2,String.valueOf((Float.parseFloat(attributes.getValue(2))-lonmin)*(1000/(lonmax-lonmin))));
			// attributesImpl.setValue(1,Float.toString((attributes.getValue(2)-7,7011)*127,8001002));
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