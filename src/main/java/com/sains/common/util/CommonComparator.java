package com.sains.common.util;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.StringTokenizer;

public class CommonComparator implements java.util.Comparator<Object> {
    private String[] compareItems = null;
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String ABOVE = "above";
    public static final String BELOW = "below";
    public CommonComparator (String[] args){
        compareItems = args;
    }
    public int compare(Object obj1, Object obj2) {
        String[] itemArr = null;
        Object object1Value;
        Object object2Value;
        String order;
        try {
            for (String compareItem : compareItems) {
                itemArr = compareItem.split(";");
                object1Value = getMethodValueFromObject(obj1, itemArr[0]);
                object2Value = getMethodValueFromObject(obj2, itemArr[0]);
                if (object1Value == null && object2Value == null) {
                    return 0;
                }
                if (object1Value == null) {
                    return -1;
                }
                if (object2Value == null) {
                    return 1;
                }
                if (object1Value == null) {
                    object1Value = "";
                }
                if (object2Value == null) {
                    object2Value = "";
                }
                String object1Type = object1Value.getClass().getSimpleName();
                if (itemArr.length == 3){//with default value
                    if (object1Type.equals("String")) {
                        if ( ((String)object1Value).compareTo((String)object2Value) != 0){
                            if ( ((String)object1Value).equalsIgnoreCase(itemArr[1]) ){
                                if (itemArr[2].equalsIgnoreCase("above")){
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        }
                    }
                } else {
                    order = ASC;
                    if (itemArr.length == 2){
                        if (itemArr[1].equalsIgnoreCase("desc")){
                            order = DESC;
                        }
                    }
                    if (object1Type.equals("String")) {
                        if ( ((String)object1Value).compareTo((String)object2Value) != 0){
                            if (order.equals(ASC)){
                                return ((String)object1Value).compareTo((String)object2Value);
                            }
                            return ((String)object1Value).compareTo((String)object2Value) * -1;
                        }
                    } else if (object1Type.equals("Double")) {
                        if ( ((Double)object1Value).compareTo((Double)object2Value) != 0){
                            if (order.equals(ASC)){
                                return ((Double)object1Value).compareTo((Double)object2Value);
                            }
                            return ((Double)object1Value).compareTo((Double)object2Value) * -1;
                        }
                    } else if (object1Type.equals("Long")) {
                        if ( ((Long)object1Value).compareTo((Long)object2Value) != 0){
                            if (order.equals(ASC)){
                                return ((Long)object1Value).compareTo((Long)object2Value);
                            }
                            return ((Long)object1Value).compareTo((Long)object2Value) * -1;
                        }
                    } else if (object1Type.equals("Integer")) {
                        if ( ((Integer)object1Value).compareTo((Integer)object2Value) != 0){
                            if (order.equals(ASC)){
                                return ((Integer)object1Value).compareTo((Integer)object2Value);
                            }
                            return ((Integer)object1Value).compareTo((Integer)object2Value) * -1;
                        }
//                    } else if (object1Type.equals("Date")) {
//                        if ( ((Date)object1Value).compareTo((Date)object2Value) != 0){
//                            return ((Integer)object1Value).compareTo((Integer)object2Value);
//                        }
                    } else if (object1Type.equals("Timestamp")) {
                        if ( ((Timestamp)object1Value).compareTo((Timestamp)object2Value) != 0){
                            if (order.equals(ASC)){
                                return ((Timestamp)object1Value).compareTo((Timestamp)object2Value);
                            }
                            return ((Timestamp)object1Value).compareTo((Timestamp)object2Value) * -1;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return 1;
//        // Always sort Report Module to the back of the list
//        if (obj1.getModule_name().equalsIgnoreCase("Report") || obj1.getModule_name().equalsIgnoreCase("Reports")) {
//            return 100;
//        } else if (obj2.getModule_name().equalsIgnoreCase("Report") || obj2.getModule_name().equalsIgnoreCase("Reports")) {
//            return -100;
//        }
//
//        // Sort by Order,
//        // if order value is same, then sort by name
//        if (obj1.getModule_order() == obj2.getModule_order()) {
//            return obj1.getModule_name().compareTo(obj2.getModule_name());
//        } else {
//            return obj1.getModule_order().compareTo(obj2.getModule_order());
//        }
    }

    public Object getMethodValueFromObject(Object model, String method) throws Exception{
        Method m;
        StringTokenizer st = new StringTokenizer(method, ".");
        String token = "";
        Object obj = null;
        int count = 0;
        try {
            while (st.hasMoreTokens()) {
                count++;
                token = st.nextToken().trim();
                if (obj == null) {
                    m = model.getClass().getMethod(token);
                    obj = m.invoke(model);
                } else {
                    m = obj.getClass().getMethod(token);
                    obj = m.invoke(obj);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return obj;
//        try {
//            Method m = model.getClass().getMethod(method);
//            Object returnObj = m.invoke(model);
//            if (returnObj != null){
//                return returnObj.toString();
//            }
//        } catch (Exception e){
//
//        }
    }
}
