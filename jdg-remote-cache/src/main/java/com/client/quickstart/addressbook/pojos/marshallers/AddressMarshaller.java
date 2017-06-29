/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.client.quickstart.addressbook.pojos.marshallers;

import org.infinispan.protostream.MessageMarshaller;
import com.client.quickstart.addressbook.pojos.domain.Address;

import java.io.IOException;

/**
 * @author Adrian Nistor
 */
public class AddressMarshaller implements MessageMarshaller<Address> {

   @Override
   public String getTypeName() {
      return "quickstart.Person.Address";
   }

   @Override
   public Class<Address> getJavaClass() {
      return Address.class;
   }

   @Override
   public Address readFrom(ProtoStreamReader reader) throws IOException {
      String addressLine = reader.readString("Address");
      String city = reader.readString("City");
      String state = reader.readString("State");
 
      Address address = new Address();
      address.setAddress(addressLine);
      address.setCity(city);
      address.setState(state);
      return address;
   }

   @Override
   public void writeTo(ProtoStreamWriter writer, Address address) throws IOException {
      writer.writeString("Address", address.getAddress());
      writer.writeString("City", address.getCity());
      writer.writeString("State", address.getState());
   }
}
