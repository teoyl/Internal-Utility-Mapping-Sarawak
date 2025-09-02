package com.sains.framework.base;

import com.sains.common.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class AliasToEntityOrderedMapResultTransformer extends AliasedTupleSubsetResultTransformer {

public static final AliasToEntityOrderedMapResultTransformer INSTANCE = new AliasToEntityOrderedMapResultTransformer();

/**
 * Disallow instantiation of AliasToEntityOrderedMapResultTransformer .
 */
private AliasToEntityOrderedMapResultTransformer () {
}

/**
 * {@inheritDoc}
 */
public Object transformTuple(Object[] tuple, String[] aliases) {
//linkedhashmap to get table column name in order        
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Map result = new LinkedHashMap(tuple.length);
    for ( int i=0; i<tuple.length; i++ ) {
        String alias = aliases[i];
        if ( alias!=null ) {
            if (tuple[i] instanceof java.sql.Date || tuple[i] instanceof java.util.Date) {
                try {
                    result.put( alias, DateUtil.getTimestampFromDate(formatter.parse(tuple[i].toString())) );
                } catch (Exception e) {
                    Debug.printError("Transformer fail to convert date to timestamp");
                }
            } else {
                result.put( alias, tuple[i] );
            }
        }
    }
    return result;
}

/**
 * {@inheritDoc}
 */
public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
    return false;
}

/**
 * Serialization hook for ensuring singleton uniqueing.
 *
 * @return The singleton instance : {@link #INSTANCE}
 */
private Object readResolve() {
    return INSTANCE;
}
}