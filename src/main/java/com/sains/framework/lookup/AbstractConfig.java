package com.sains.framework.lookup;

import com.sains.framework.base.LogFunction;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;


public class AbstractConfig implements IConfig {

    public Map read(String filename, ConfigHandler handler) {
        Map result = null;

        if (handler != null) {
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

                parser.parse(filename, handler);
                
                result = handler.getMap();

            } catch (Exception e) {
                new LogFunction().logError(this.getClass(), "", e);
            }
        }
        
        return result;
    }

    public Map read(java.io.InputStream is, ConfigHandler handler) {
        Map result = null;

        if (handler != null) {
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

                parser.parse(is, handler);

                result = handler.getMap();

            } catch (Exception e) {
                new LogFunction().logError(this.getClass(), "", e);
            }
        }

        return result;
    }

	@Override
	public Map read(InputSource inputSource, ContentHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}