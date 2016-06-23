/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

@SuppressWarnings("nls")
public class SparkSQLClient {
	
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	public static final String USERNAME_DEFAULT = "teiidUser";
	public static final String PASSWORD_DEFAULT = "password1!";
	
	public static void main(String[] args) throws Exception {
		if (args.length < 4) {
			System.out.println("usage: SparkSQLClient <host> <port> <vdb> <table>");
			System.exit(-1);
		}

		Map<String, String> options = new HashMap<String, String>();
		options.put("url", getURL(args[0], args[1], args[2]));
		options.put("user", getUserName());
		options.put("Password", getPassword());
		options.put("dbtable", args[3]);
		
		JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("SparkJdbcFromTeiid").setMaster("local[*]"));
		SQLContext sqlContext = new SQLContext(sc);
		
		DataFrame jdbcDF = sqlContext.read().format("jdbc"). options(options).load();		
		List<Row> rows = jdbcDF.collectAsList();
		for (Row row : rows){
		    System.out.println(row.toString());
		}

	}
	
	static String getURL(String host, String port, String vdb) throws Exception {
		return "jdbc:teiid:"+vdb+"@mm://"+host+":"+port;		
	}
	
	static String getUserName() {
		return (System.getProperties().getProperty(USERNAME) != null ? System.getProperties().getProperty(USERNAME) : USERNAME_DEFAULT );
	}
	
	static String getPassword() {
		return (System.getProperties().getProperty(PASSWORD) != null ? System.getProperties().getProperty(PASSWORD) : PASSWORD_DEFAULT );
	}


}
