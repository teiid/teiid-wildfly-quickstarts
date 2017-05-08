package com.client.quickstart.remotecache.pojos;
/**
* Maps a relational database table Stock to a java object, Stock
*
* 
*
* @author	ReverseEngineer
*/
import java.io.Serializable;
import java.sql.*;
import java.util.*;
import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;

public class Stock implements Serializable {

	
	public java.lang.String m_Symbol;

	
	public java.lang.String m_Price;

	
	public java.lang.Integer m_Product_id;

	
	public java.lang.String m_Company_name;


	@ProtoField(number = 2, required = false)
	public java.lang.String getSymbol( ) { 
		return this.m_Symbol;
	}

	public void setSymbol( java.lang.String symbol) { 
		 this.m_Symbol = symbol; 
	}

	@ProtoField(number = 3, required = false)
	public java.lang.String getPrice( ) { 
		return this.m_Price;
	}

	public void setPrice( java.lang.String price) { 
		 this.m_Price = price; 
	}

	@ProtoField(number = 1, required = true)
	public java.lang.Integer getProduct_id( ) { 
		return this.m_Product_id;
	}

	public void setProduct_id( java.lang.Integer product_id) { 
		 this.m_Product_id = product_id; 
	}

	@ProtoField(number = 4, required = false)
	public java.lang.String getCompany_name( ) { 
		return this.m_Company_name;
	}

	public void setCompany_name( java.lang.String company_name) { 
		 this.m_Company_name = company_name; 
	}
	public String toString()  {
		StringBuffer output = new StringBuffer();
		output.append("symbol:");
		output.append(getSymbol());
		output.append("\n");
		output.append("price:");
		output.append(getPrice());
		output.append("\n");
		output.append("product_id:");
		output.append(getProduct_id());
		output.append("\n");
		output.append("company_name:");
		output.append(getCompany_name());
		output.append("\n");

		return output.toString();
	}

} // class Stock
