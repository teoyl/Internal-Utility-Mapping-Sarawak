package com.sains.framework.model;

public class ApplicationNameComparator implements java.util.Comparator<Application>{
	public int compare(Application obj1, Application obj2) {

        // Sort by Order,
        // if order value is same, then sort by name
        if (obj1.getApplication_order() == obj2.getApplication_order()) {
            return obj1.getApplication_name().compareTo(obj2.getApplication_name());
        } else {
            return obj1.getApplication_order().compareTo(obj2.getApplication_order());
        }
	}
}