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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;

/**
 * 
 */

@Indexed
@ProvidedId
public class LineItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1921968291712892230L;
	private @Field
	int id;
	private Product product;
	private @Field
	int quantity;

	public LineItem() {
	}

	public LineItem(int id) {
		this.id = id;
	}

	public LineItem(int id, Product product, int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}

	@Field
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Field
	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int cnt) {
		this.quantity = cnt;
	}

	@Field
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String toString() {
		return "Product (id) " + getId() + " (product) " + getProduct() + " (quantity) " + getQuantity(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
