/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, JBoss Inc., and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2011-2012,
 * @author JBoss Inc.
 */

package org.jboss.teiid.quickstart.data;

import java.math.BigDecimal;

public class MarketData {

    private String companyName;
    private String symbol;
    private BigDecimal price;
    
    private String invalidMessage;
    
    public MarketData(String companyName, String symbol, BigDecimal price) {
    	this.companyName = companyName;
    	this.symbol = symbol;
    	this.price = price;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String name) {
        this.companyName = name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MarketData: " + companyName + ", " + symbol + ", " + price + ", invalidMessage: " + (invalidMessage!=null?invalidMessage:"") ;
	}
	
  public String getInvalidMessage() {
		return invalidMessage;
  }
  public void setInvalidMessage(String message) {
  	 this.invalidMessage = message;
  }

}
