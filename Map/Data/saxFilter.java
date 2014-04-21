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

       	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

       	AttributesImpl newattribute = new AttributesImpl(attributes);


       	if (localName.equals("bounds")){

       		latmin = Float.parseFloat(attributes.getValue(0));
       		lonmin = Float.parseFloat(attributes.getValue(1));
       		latmax = Float.parseFloat(attributes.getValue(2));
       		lonmax = Float.parseFloat(attributes.getValue(3));
       	} 
       	if (localName.equals("node")){

			newattribute.setValue(1,String.valueOf((Float.parseFloat(attributes.getValue(1))-latmin)*(1000/(latmax-latmin))));
			newattribute.setValue(2,String.valueOf((Float.parseFloat(attributes.getValue(2))-lonmin)*(1000/(latmax-latmin))));
			// attributesImpl.setValue(1,Float.toString((attributes.getValue(2)-7,7011)*127,8001002));
		}
		super.startElement(uri, localName, qName, newattribute);
	}

// Float.toString((Float.parseFloat(attributes.getValue(1))-54.37361)*274.18819729)+";"+Float.toString((Float.parseFloat(attributes.getValue(2))-7,7011)*127,8001002)


// FLoat.toString(Float.parseFloat(attributes.getValue(1)))
}