/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.client.quickstart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.EmbeddedCacheManager;
import org.teiid.resource.spi.WrappedConnection;
import org.teiid.translator.TranslatorException;
import org.teiid.translator.object.ObjectConnection;

import com.client.quickstart.pojo.LineItem;
import com.client.quickstart.pojo.Order;
import com.client.quickstart.pojo.Product;

/**
 * Note:  the @Resource(lookup..)  will look like its invalid in Eclipse, but will build using maven.
 * This is because "lookup" in Java 6 endorsed lib, and not part of the classpath.
 * @author vhalbert
 *
 */
@Singleton
public class Resources {
	private static Logger log = Logger.getLogger(Resources.class.getName());
	
	private static final String CACHE_NAME="local-quickstart-cache";

	@SuppressWarnings("unused")
	@Produces
	@Resource(lookup = "java:jboss/infinispan/container/teiid-infinispan-quickstart")
	private CacheContainer container;
	private Cache<Object, Object> cache;
	private static boolean PRELOADED = false;
	public static Map<Object, Object> CACHE = null;

	@Produces
	@ApplicationScoped
	public Cache<Object, Object> getCache() {
		if (!PRELOADED) {
			loadCache(this.container.getCache(CACHE_NAME));
			PRELOADED = true;
		}
		return this.container.getCache();
	}

	public static final int NUM_ORDERS = 10;
	public static final int NUM_PRODUCTS = 3;

	private static Map<Object, Object> loadCache(Map<Object, Object> incache) {
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

			incache.put(String.valueOf(i), order);

			++lineitems;
			if (lineitems > NUM_PRODUCTS) {
				lineitems = 1;
			}

		}

		return incache;
	} // end loadCache

} // class end
