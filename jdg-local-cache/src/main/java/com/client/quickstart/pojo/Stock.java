/*
 * Copyright Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.client.quickstart.pojo;

import java.math.BigDecimal;

//import javax.persistence.Entity;
//import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.NumericField;

/**
 * @ProvidedId
 */
//@Entity
@Indexed(index="StockIndex")
public class Stock implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155172202224249683L;
//	@Id
    @NumericField @Field(index=Index.YES, store=Store.YES, analyze=Analyze.NO)
	public int productId;
	
	// NOTE: hibernate doesn't support BigDecimal using NumericField, so for this
	//       example, it will remain being stored as a string
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
		setPrice(new BigDecimal(price));
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
/*
	public void setPrice(double price) {
		this.price = new BigDecimal(price);
	}
*/
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
