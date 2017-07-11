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
package com.client.quickstart.hibernate4.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/*The Model uses JPA Entity as well as Hibernate Validators
 * 
 */

@Entity
//@XmlRootElement
@Table(name = "ProductInfo", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class ProductInfo implements java.io.Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 1L;

	@NotNull
	@Id
	private int id;

	@NotNull
	@Size(min = 1, max = 10)
	@Pattern(regexp = "[A-Za-z]*", message = "must contain only letters")
	private String symbol;

	@NotNull
	@Size(min = 1, max = 100)
	@Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only letters, numbers and spaces")
	private String companyName;

	@Column(name="price", insertable=false, updatable=false)
	private BigDecimal price;

	public ProductInfo() {
	}

	public ProductInfo(int id) {
		this.id = id;
	}

	public ProductInfo(int id, String symbol, String companyName) {
		this.id = id;
		this.symbol = symbol;
		this.companyName = companyName;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String toString() {
		return "Product: (id) " + getId() + " (symbol) " + getSymbol()
				+ " (companyName) " + getCompanyName() + " (price) "
				+ getPrice();
	}

}
