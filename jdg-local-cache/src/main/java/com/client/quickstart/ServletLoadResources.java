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

import java.io.IOException;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.apache.lucene.search.Query;

//import org.infinispan.query.dsl.Query;
//import org.infinispan.query.dsl.QueryBuilder;
//import org.infinispan.query.dsl.QueryFactory;

import com.client.quickstart.pojo.Stock;

/**
 * Note: the @Resource(lookup..) will look like its invalid in Eclipse, but will
 * build using maven. This is because "lookup" in Java 6 endorsed lib, and not
 * part of the classpath.
 * 
 * @author vhalbert
 * 
 */
public class ServletLoadResources implements ServletContextListener {

	private static Logger log = Logger.getLogger(ServletLoadResources.class
			.getName());

	private static final String CACHE_NAME = "local-quickstart-cache";
	private static final String JNDI_NAME = "jboss/teiid/jdg-quickstart";

	// private static ServletLoadResources resource = null;

	private static DefaultCacheManager container;
	private static boolean PREBOUND = false;
	private static boolean PRELOADED = false;

	public void contextInitialized(ServletContextEvent contextEvent) {
		if (PREBOUND) {
			return;
		}
		
			if (exists()) {
				PREBOUND = true;
				return;
			}
			try {
				// createContainer();
				createContainerUsingFile();
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			bindToJNDI();
			PREBOUND = true;
			
			try {
				testCache();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {

	}

	private void testCache() throws Exception {
		
		container = null;
		if (!exists()) {
			throw new Exception("Unable to testcache, JNDI " + JNDI_NAME
					+ "was not found");
		}

		Cache<Long, Stock> cache = container.getCache(CACHE_NAME);
/*		
		QueryFactory qf = Search.getQueryFactory(cache);
	    		  
	    QueryBuilder qb = qf.from(Stock.class);
		
		Query query = qb.build();
		
		query.list();
*/		
/*
		SearchManager sm = Search.getSearchManager(cache);
		sm.getQueryFactory().from(Stock.class).build().list();
*/

		SearchManager sm = Search.getSearchManager(cache);
		Query query = sm.buildQueryBuilderForClass(Stock.class).get().keyword()
				.onField("symbol").matching("CIS").createQuery();
		CacheQuery q1 = sm.getQuery(query);
		
//		Query query = sm.buildQueryBuilderForClass(type).get().all().createQuery();

		System.out
				.println("*******" + CACHE_NAME + " is setup and be searched");
		
	}

	private void bindToJNDI() {

		InitialContext ctx;
		try {
			ctx = new InitialContext();
			ctx.bind(JNDI_NAME, container);

		} catch (NamingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private boolean exists() {
		Object o = null;
		try {
			InitialContext context = new InitialContext();
			o = context.lookup(JNDI_NAME);

			container = (DefaultCacheManager) o;
		} catch (Exception err) {

		}
		return (o != null ? true : false);
	}

	protected void createContainerUsingFile() throws Exception {
		try {
			container = new DefaultCacheManager("infinispan.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		container.start();
		container.startCache(CACHE_NAME);
		
		container.getCacheConfiguration(CACHE_NAME).module(Stock.class);
		
		System.out
				.println("******* Start cache: " + CACHE_NAME + " and set Module Stock.class on default configuration ");

	}

	protected void createContainer() throws Exception {

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.indexing().enable().indexLocalOnly(false)
				.addProperty("default.directory_provider", "ram")
				.addProperty("lucene_version", "LUCENE_CURRENT");

		container = new DefaultCacheManager();

		if (!container.cacheExists(CACHE_NAME)) {

			container.defineConfiguration(CACHE_NAME, builder.build());
			container.startCache(CACHE_NAME);

		}

	}

} // class end
