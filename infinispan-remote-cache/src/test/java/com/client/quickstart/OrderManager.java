/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.client.quickstart;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.Properties;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import com.client.quickstart.pojo.*;

/**
 * @author Martin Gencur
 */
public class OrderManager {

    private static final String JDG_HOST = "jdg.host";
    private static final String HOTROD_PORT = "jdg.hotrod.port";
    private static final String PROPERTIES_FILE = "jdg.properties";
    private static final String CACHE_NAME = "jdg.cache.name";
    
    private static final String initialPrompt = "Choose action:\n" + "============= \n" + "lo  -  load orders\n"
             + "p   -  print all orders\n" + "q   -  quit\n";
    
    public static final int NUM_ORDERS = 10;
	public static final int NUM_PRODUCTS = 3;

    private Console con;
    private RemoteCacheManager cacheManager;
    private RemoteCache<String, Object> cache;
    
    private Properties props = null;

    public OrderManager(Console con) {
        this.con = con;
        final String cacheName=jdgProperty(CACHE_NAME);
        if (cacheName==null) {
            throw new RuntimeException(CACHE_NAME + " is null");
        }
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.addServer()
              .host(jdgProperty(JDG_HOST))
              .port(Integer.parseInt(jdgProperty(HOTROD_PORT)));
        cacheManager = new RemoteCacheManager(builder.build());
        cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            con.printf("Cache " + cacheName + " is null");
        }
    }


    public void printOrders() {
        @SuppressWarnings("unchecked")
        Map mapCache = cache.getBulk();
        if (mapCache != null) {
            
			Set<Object> keys = mapCache.keySet();
			for (Iterator<?> it = keys.iterator(); it.hasNext();) {
				Order v = (Order) cache.get(it.next());
                con.printf(v.toString());
			}
            
        }
    }

    public void stop() {
        cacheManager.stop();
    }
    
    private void loadOrders() {
        if (cache != null && cache.keySet().isEmpty()) {
             int cnt = loadCache();
             con.printf("Loaded " + cnt + " orders");
        } else {
            con.printf("Cache is null, no orders will be loaded");
        }
        
    }
	private int loadCache() {
		List<Product> products = new ArrayList<Product>(NUM_PRODUCTS);
		products.add(new Product(1, "Shirt", 54.99)); //$NON-NLS-1$
		products.add(new Product(2, "Pants", 89.00)); //$NON-NLS-1$
		products.add(new Product(3, "Socks", 1.29)); //$NON-NLS-1$
        
		int lineitems = 1;
		for (int i = 1; i <= NUM_ORDERS; i++) {
            
			List<LineItem> items = new ArrayList<LineItem>();
			for (int j = 0, p = 0, q = 1; j < lineitems; j++) {
                
				LineItem item = new LineItem(j + 1, products.get(p), q);
                
				items.add(item);
                
				++p;
				++q;
			}
            
			Order order = new Order(i, new Date(), "Person " + i, items); //$NON-NLS-1$
            
			cache.put(String.valueOf(i), order);
            
			++lineitems;
			if (lineitems > NUM_PRODUCTS) {
				lineitems = 1;
			}
            
		}
        
		return NUM_ORDERS;
	} // end loadCache    

    public static void main(String[] args) {
        Console con = System.console();
        OrderManager manager = new OrderManager(System.console());
        con.printf(initialPrompt);

        while (true) {
            String action = con.readLine(">");
            if ("lo".equals(action)) {
                manager.loadOrders();
            } else if ("p".equals(action)) {
                manager.printOrders();
            } else if ("q".equals(action)) {
                manager.stop();
                break;
            }
        }
    }

    public String jdgProperty(String name) {
        if (props == null) {
            props = new Properties();
            try {
                props.load(OrderManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
        return props.getProperty(name);
    }
}
