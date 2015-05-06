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

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * @ProvidedId
 */
@Entity
@Indexed(index="StockIndex")
public class Stock implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155172202224249683L;
	@Id
   @Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	public int productId;
	@Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	public  BigDecimal price;
	@Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	public  String symbol;
	@Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	public  String companyName;

	public Stock() {
	}
	
	public Stock(int id, double price, String symbol, String name) {
		setProductId(id);
		setPrice(price);
		setSymbol(symbol);
		setCompanyName(name);
	}

	public Stock(int id) {
		this.productId = id;
	}


	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int id) {
		this.productId = id;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setPrice(double price) {
		this.price = new BigDecimal(price);
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String name) {
		this.companyName = name;
	}

	public String toString() {
		return "Stock: (id) " + getProductId() + " (Symbol) " + getSymbol() + " (Price) " + getPrice() + " (Company Name) " + getCompanyName() ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 
	}
}
