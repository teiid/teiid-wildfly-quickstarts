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
package org.teiid.quickstarts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.teiid.jdbc.TeiidDataSource;

@SuppressWarnings("nls")
public class JDBCClient {
	
	public static void main(String[] args) throws Exception {
		
		if (args.length < 3) {
			System.out.println("usage: JDBCClient <directory-with-queries> <teiidUsername> <teiidPassword>");
			System.out.println("       directory-with-queries - contains .sql files, each file contains a single SQL query");
			System.exit(-1);
		}

		System.out.println("Executing using the TeiidDriver");
		Connection conn = getDataSourceConnection("localhost", "31000", "tpch", args[1] , args[2]);

		List<File> files = listGenreatedQueryFiles(args[0]);
		float num  = files.size();
				
		System.out.println("Starting query execution");
		
		long start = System.currentTimeMillis(); 
		for (File f : files) {
			String query = getTextFileContent(f);
			try {
			    System.out.print("Executing "+f.getName());
			    long testStart = System.currentTimeMillis();
				execute(conn, query);
				System.out.println(" took =" + (System.currentTimeMillis()-testStart)/1000 +" secs");
			} catch (Exception ex) {
				throw new RuntimeException("Unable to execute query " + f.getName(), ex);
			}
		}
		long end = System.currentTimeMillis();
		
		float minutes = ((float)(end-start)/60000);
		
		System.out.println("\nPerformance test finished.");
		System.out.println(String.format("\t\t %.2f minutes", minutes));
		System.out.println(String.format("\t\t %.2f queries/minute", num/minutes));
		
		conn.close();
	}
	
	private static List<File> listGenreatedQueryFiles(String string) {
		if (string==null || string.isEmpty())
			throw new IllegalArgumentException("You must specify the directory with SQL queries");
		File generateDir = new File(string);
		if (!generateDir.exists())
			throw new IllegalArgumentException("The directory with generated SQL queries doesnt exist ["+generateDir.getAbsolutePath()+"]");
		
		List<File> sqlQueries = Arrays.asList(generateDir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith("sql");
			}
		
		}));
		if (sqlQueries.size()==0)
			throw new IllegalArgumentException("The directory with generated sql files doesnt contain ANY .sql files["+generateDir.getAbsolutePath()+"]");
		
		System.out.println("Found "+sqlQueries.size()+" queries.");
		return sqlQueries;
	}

	private static String getTextFileContent(File f) {
		StringBuffer sb = new StringBuffer();

		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append("\n"+line);
			}
		} catch (Exception ex) {
			throw new RuntimeException("Unable to read content of .sql file " + f.getName(), ex);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				throw new RuntimeException("Unable to close .sql file " + f.getName(), e);
			}
		}
		return sb.toString();
	}

	static Connection getDataSourceConnection(String host, String port, String vdb, String username, String password) throws Exception {
		TeiidDataSource ds = new TeiidDataSource();
		ds.setDatabaseName(vdb);
		ds.setUser(username);
		ds.setPassword(password);
		ds.setServerName(host);
		ds.setPortNumber(Integer.valueOf(port));
		
		try {	
			return ds.getConnection();
 		} catch (Exception ex){	
			ex.printStackTrace();
			System.out.println("Caught exception");
			Thread.sleep(20000);
			System.out.println("Retrying ");
			return ds.getConnection();
		}
	}
	
	public static void execute(Connection connection, String sql) throws Exception {
		Statement statement = null;
		ResultSet results = null;
		try {
			statement = connection.createStatement();
			results = statement.executeQuery(sql);
			//Go through the result set
			while (results.next()){				
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Error while executing a query", e);
		} finally {
			if (results != null)
				results.close();
			if (statement !=null)
				statement.close();
		}		
	}
}
