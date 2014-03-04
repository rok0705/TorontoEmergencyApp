package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLParseHandler {
	List<FacilityProfile> facilities;
	private FacilityProfile facility;
	private String text;
	
	public XMLParseHandler(){
		facilities = new ArrayList<FacilityProfile>();
	}
	
	public List<FacilityProfile> parse (InputStream is){
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		
		try {
			factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
 
            parser.setInput(is, null);
 
            int eventType = parser.getEventType();
            String attNameValue = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                
                switch (eventType) {
                case XmlPullParser.START_TAG:
                	
                    if (tagname.equalsIgnoreCase("placemark")) {
                        // create a new instance of employee
                        facility = new FacilityProfile();
                    }else if(tagname.equalsIgnoreCase("simpledata")&&facility!=null){
                    	attNameValue = parser.getAttributeValue(0);
                    	
                    	//System.out.println("START TAG Attributes Value =" + attNameValue);
                    	
                    } 
                    break;
 
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
 
                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("placemark")) {
                        // add employee object to list
                        facilities.add(facility);
                    }else if(tagname.equalsIgnoreCase("simpledata")){
                    	
                    	if (attNameValue.equalsIgnoreCase("name")) {
                    
                    		//System.out.println("END TAG Attributes Value =" + text);
                    		facility.SetName(text);
	                    } else if (attNameValue.equalsIgnoreCase("address")) {
	                        facility.SetAddress(text);
	                    }
                    }else if (tagname.equalsIgnoreCase("coordinates")){
                    	facility.SetCoordinates(text);
                    }
                    break;
                
                default:
                    break;
                }
                eventType = parser.next();
            }
 
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return facilities;
	}
}
