package com.sains.framework.lookup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandler extends DefaultHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Map elements;
	
	public void startDocument() throws SAXException {
		elements = new HashMap();
    }

    public void endDocument() throws SAXException {
    }

	/**
	 * @return the element
	 */
	public Map getElements() {
		return elements;
	}
}