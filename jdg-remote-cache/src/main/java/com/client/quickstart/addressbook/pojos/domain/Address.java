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
package com.client.quickstart.addressbook.pojos.domain;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;

/**
 * @author vhalbert
 *
 */
@ProtoDoc("@Indexed")
public class Address {

	private String address;
	private String city;
	private String state;
	/**
	 * @return address
	 */
	@ProtoField(number = 1, required = true)
	public String getAddress() {
		return address;
	}
	/**
	 * @param address Sets address to the specified value.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return city
	 */
	@ProtoField(number = 2, required = true)
	public String getCity() {
		return city;
	}
	/**
	 * @param city Sets city to the specified value.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return state
	 */
	@ProtoField(number = 3, required = true)
	public String getState() {
		return state;
	}
	/**
	 * @param state Sets state to the specified value.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	
}
