package com.sains.framework.model;

public class ModuleNameComparator implements java.util.Comparator<Module>{
	public int compare(Module obj1, Module obj2) {
        // Always sort Report Module to the back of the list
		if (obj1.getModule_name().equalsIgnoreCase("Report") || obj1.getModule_name().equalsIgnoreCase("Reports")){
			return 100;
		} else if (obj2.getModule_name().equalsIgnoreCase("Report") || obj2.getModule_name().equalsIgnoreCase("Reports")){
			return -100;
		}

        // Sort by Order,
        // if order value is same, then sort by name
        if (obj1.getModule_order() == obj2.getModule_order()) {
            return obj1.getModule_name().compareTo(obj2.getModule_name());
        } else {
            return obj1.getModule_order().compareTo(obj2.getModule_order());
        }
	}
}