package com.sains.framework.lookup;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.sains.framework.lookup.ConfigHandler;

/**
 * Description of the class. Change accordingly.
 * 
 * @version 1.0
 */

public class LookupConfigHandler extends ConfigHandler {
    private final String QUERY = "query";
    private final String QUERY_NAME = "name";
    
    private final String PROPERTY = "property";
    private final String PROPERTY_NAME = "name";
    private final String PROPERTY_VALUE = "value";
    
    private Map propertyMap;

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        localName = (localName.length() == 0) ? qName : localName;

        if (localName.equalsIgnoreCase(QUERY)) {
            if ((attributes.getValue(QUERY_NAME) != null) && (attributes.getValue(QUERY_NAME) != ""))
            {
                propertyMap = new HashMap();
            
                map.put(attributes.getValue(QUERY_NAME), propertyMap);
            }
        } else if (localName.equalsIgnoreCase(PROPERTY)) {
            if (propertyMap != null) {
                if ((attributes.getValue(PROPERTY_NAME) != null) && (attributes.getValue(PROPERTY_VALUE) != null))
                {                    
                    propertyMap.put(attributes.getValue(PROPERTY_NAME), attributes.getValue(PROPERTY_VALUE));
                }
            }
        }
    }
}
