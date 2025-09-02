package com.sains.framework.lookup;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Then Sze Wee
 */

public class ConfigHandler extends DefaultHandler {
    protected Map map;

    public Map getMap() {
        return map;
    }

    public void startDocument() throws SAXException {
        map = new HashMap();
    }

    public void endDocument() throws SAXException {
    }
}