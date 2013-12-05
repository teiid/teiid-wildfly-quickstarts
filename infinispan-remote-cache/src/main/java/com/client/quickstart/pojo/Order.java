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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;

/**
 * @ProvidedId
 */
@Entity
@Indexed(index = "Order")
public class Order implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155172202224249683L;

	private static DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy"); //$NON-NLS-1$

	private int id;
	private Date orderDate;
	private String person;
	private List<LineItem> lineItems;

	// @IndexedEmbedded
	public Order() {
	}

	public Order(int id) {
		this.id = id;
	}

	public Order(int id, Date date, String person, List<LineItem> items) {
		this.id = id;
		this.orderDate = date;
		this.person = person;
		this.lineItems = items;
	}

	@Id
	@DocumentId
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderDateString() {
		return dateFormat.format(orderDate);
	}

	@Field
	@DateBridge(resolution = Resolution.MINUTE)
	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Field(index = Index.YES)
	public String getPerson() {
		return this.person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public int getNumberOfItems() {
		return getLineItems().size();

	}

	// @IndexedEmbedded
	public List<LineItem> getLineItems() {
		return this.lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	public String toString() {
		return "OrderID: (id) " + getId() + " (Person) " + getPerson() + " (OrderDate) " + getOrderDate() + " (#LineItems) " + (getLineItems() != null ? getLineItems().size() : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
	}
}
