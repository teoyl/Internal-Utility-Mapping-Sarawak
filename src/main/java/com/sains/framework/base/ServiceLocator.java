/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sains.framework.base;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author lenovo
 */
public class ServiceLocator {
    private static ServiceLocator instance;
    private Map<String, Object> cache;

    static {
        if (instance == null) instance = new ServiceLocator();
    }

    private ServiceLocator(){
        if (cache == null) cache = new Hashtable<String, Object>();
    }

    public synchronized static ServiceLocator getInstance() {
        if (instance == null) instance = new ServiceLocator();
        return instance;
    }

    public void clear() {
        cache.clear();
    }

    public void remove(String className) {
        cache.remove(className);
    }

    // Without XxxDAOImpl, Utilize BaseDAOImpl with unique name
    public <T> Object locateBaseDAO(String className, Class<T> modelClass) throws Exception {
        Object service;
//        System.out.println("locateBaseDAO, Before: " + cache);
        synchronized (cache) {
            if (cache.containsKey(className)) {
                service = cache.get(className);
            } else {
                service = new BaseDAOImpl<T>();
                cache.put(className, service);
            }
        }
//        System.out.println("locateBaseDAO, After: " + cache);
        return service;
    }

    // For Dynamic Action
    public Object locateBaseDAOGeneral(String className) throws Exception {
        Object service;
//        System.out.println("locateBaseDAO, Before: " + cache);
        synchronized (cache) {
            if (cache.containsKey(className)) {
                service = cache.get(className);
            } else {
                service = new BaseDAOImpl();
                cache.put(className, service);
            }
        }
//        System.out.println("locateBaseDAO, After: " + cache);
        return service;
    }

    // With XxxDAOImpl
    public Object locate(Class implementationClass) throws Exception {
        Object service;
//        System.out.println("Locate, Before: " + cache);
        String className = implementationClass.getPackage() + "." + implementationClass.getSimpleName();
        synchronized (cache) {
            if (cache.containsKey(implementationClass.getSimpleName())) {
                service = cache.get(implementationClass.getSimpleName());
            } else {
                service = implementationClass.newInstance();
                cache.put(implementationClass.getSimpleName(), service);
            }
        }
//        System.out.println("Locate, After: " + cache);
        return service;
    }
}
