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
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.lang.annotation.ElementType;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.cfg.SearchMapping;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.transaction.TransactionMode;

import org.infinispan.query.dsl.FilterConditionContext;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;

import org.infinispan.query.Search;

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

	private static final String CACHE_NAME = "local-quickstart-cache";  // used for the basic jdg-local testing
	private static final String JNDI_NAME = "java:jboss/teiid/jdg-quickstart";

	private static final String MAT_CACHE_NAME = "local-quickstart-mat-cache";  // used in the mat-view processing
	private static final String MAT_STAGING_CACHE_NAME = "local-quickstart-mat-staging-cache";  // used in the mat-view processing

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
				createContainers();
				//createContainerUsingFile();
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			bindContainers();
			PREBOUND = true;
			
			try {
			    loadCache();
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
			throw new Exception("Unable to test cache, JNDI " + JNDI_NAME
					+ "was not found");
		}

		Cache<Integer, Stock> cache = container.getCache(CACHE_NAME);
		QueryFactory qf = Search.getQueryFactory(cache);
		QueryBuilder qb = qf.from(Stock.class);
		
		FilterConditionContext fcc = qb.having("productId").lte(new Integer(100).intValue());
		
		Query query = fcc.toBuilder().build();
		List<Object> results = query.list();

/*		
		if (results.size() != 100) {
			throw new Exception("*******" + CACHE_NAME + " test returned incorrect size of " + results.size());
		}
*/		
		System.out.println("*******" + CACHE_NAME + " is setup and tested");
		
	}
	
	private void loadCache() throws Exception {
	
		Cache<Integer, Stock> cache = container.getCache(CACHE_NAME);
		int x = 0;
		for (int i = 1; i <= 200; i++) {

			Stock s = new Stock(i, (i * 1.25), "RHT" + i, "Red Hat " + i);

			// 12.33
			cache.put(new Integer(i).intValue(), s);
			x = i;

		}	
		System.out.println("******* Loaded " + CACHE_NAME + " with number of objects " + x );

	}
	
	private void printObjects(List<Object> results) {
	
		for(Object o:results) {
			System.out.println("******* Object: " + o.toString());
		}
	}
	
	private void bindContainers() {
	  bindToJNDI(JNDI_NAME, container);

	}

	private void bindToJNDI(String jndiName, DefaultCacheManager cache) {

		InitialContext ctx;
		try {
			ctx = new InitialContext();
			ctx.bind(jndiName, cache);

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
	
	protected void createContainers() throws Exception {
		
		container = createContainer(Stock.class, CACHE_NAME);
		container.startCache(MAT_CACHE_NAME);
		container.getCacheConfiguration(MAT_CACHE_NAME).module(Stock.class);

		container.startCache(MAT_STAGING_CACHE_NAME);
		container.getCacheConfiguration(MAT_STAGING_CACHE_NAME).module(Stock.class);


		container.startCache("aliasCache");
	}

	protected DefaultCacheManager createContainer(Class<?> clz, String cacheName) throws Exception {
	
		SearchMapping mapping = new SearchMapping();
		mapping.entity(clz).indexed().providedId();
//		property("productId", ElementType.METHOD).field().analyze(Analyze.NO).
//		property("price", ElementType.METHOD).field().analyze(Analyze.NO).
//		property("symbol", ElementType.METHOD).field().analyze(Analyze.NO).
//		property("companyName", ElementType.METHOD).field().analyze(Analyze.NO);

		Properties properties = new Properties();
		properties.put(org.hibernate.search.Environment.MODEL_MAPPING, mapping);
		properties.put("lucene_version", "LUCENE_CURRENT");		
		properties.put("hibernate.search.default.directory_provider", "ram");
		
		GlobalConfiguration glob = new GlobalConfigurationBuilder()
        	.nonClusteredDefault() //Helper method that gets you a default constructed GlobalConfiguration, preconfigured for use in LOCAL mode
        	.globalJmxStatistics().enable() //This method allows enables the jmx statistics of the global configuration.
        	.jmxDomain("org.infinispan.cache.indexes." + cacheName)  //prevent collision
        	.build(); //Builds  the GlobalConfiguration object
		
		

		Configuration loc = new ConfigurationBuilder()
    		.indexing().enable().addProperty("hibernate.search.default.directory_provider", "filesystem").addProperty("hibernate.search.default.indexBase", "./data/quickstart/lucene/indexes/" + cacheName)
    		.enable()
    		.indexLocalOnly(true)
    		.withProperties(properties)
		        .persistence().passivation(false).addSingleFileStore().purgeOnStartup(true).location("./data/quickstart/localcache/indexing/" + cacheName) //Disable passivation and adds a SingleFileStore that is purged on Startup
    		.build();

		DefaultCacheManager c = new DefaultCacheManager(glob, loc);
			c.startCache(cacheName);
			
			c.getCacheConfiguration(cacheName).module(clz);
	
		return c;


	}

} // class end
