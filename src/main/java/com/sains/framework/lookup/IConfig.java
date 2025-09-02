package com.sains.framework.lookup;

import java.util.Map;

import org.xml.sax.InputSource;
/**
 * Interface class for xml parsing implementation.
 *
 * @version $Revision$
 */
public interface IConfig {
	Map read(InputSource inputSource, ContentHandler handler);
}