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
package com.client.quickstart.pojo;

import java.math.BigDecimal;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;

/**
 * 
 */

@Indexed
@ProvidedId
public class Product implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private @Field(index = Index.YES)
	String name;
	private BigDecimal price;

	public Product() {
	}

	public Product(int id) {
		this.id = id;
	}

	public Product(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = new BigDecimal(price);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String toString() {
		return "ProductInfo: (id) " + getId() + " (name) " + getName() + " (price) " + getPrice(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
	}
}
