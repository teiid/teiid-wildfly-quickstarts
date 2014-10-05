/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.infinispan.commons.api.BasicCache;
import org.jboss.logging.Logger;

import com.client.quickstart.pojo.Orders;

/**
 * 
 */

@SessionScoped
@Named
public class Controller implements java.io.Serializable {
	private static Logger log = Logger.getLogger(Controller.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7276160166278313350L;

	private BasicCache<Object, Object> cache = Resources.getCache();

	private Orders[] orders = null;

	public Controller() {
	}

	public Orders[] getOrderList() {
		if (orders == null) {
			Set<Object> keys = cache.keySet();
			orders = new Orders[cache.size()];
			int i = 0;
			for (Iterator<?> it = keys.iterator(); it.hasNext();) {
				Orders v = (Orders) cache.get(it.next());
				orders[i] = v;
				++i;
			}
			log.info("--- Controller has orders: " + i); //$NON-NLS-1$
		}
		return orders;

	}
}
