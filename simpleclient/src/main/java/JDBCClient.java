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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.teiid.jdbc.TeiidDataSource;
import org.teiid.jdbc.TeiidStatement;

@SuppressWarnings("nls")
public class JDBCClient {
	
    public static final String HOST = "host";
    public static final String PORT = "port";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String SHOWPLAN = "showplan";
	public static final String USE_DATASOURCE = "usedatasource";
	
	public static final String HOST_DEFAULT = "localhost";
	public static final int PORT_DEFAULT = 31000;
	public static final String USERNAME_DEFAULT = "teiidUser";
	public static final String PASSWORD_DEFAULT = "password1!";
	public static final boolean SHOWPLAN_DEFAULT = false;
	public static final boolean USE_DATASOURCE_DEFAULT = false;
	
	public static void main(String[] args) throws Exception {
	    
		if (args.length < 4) {
			System.out.println("usage: JDBCClient<vdb> <sql-command>");
			System.exit(-1);
		}
		
		if(isUseDatasource()){
		    execute(getDataSourceConnection(getHost(), getPort(), args[2]), args[3]); // Executing using the TeiidDriver"
		} else {
		    execute(getDriverConnection(getHost(), getPort(), args[2]), args[3]); // Executing using the TeiidDataSource
		} 
	}
	
	static Connection getDriverConnection(String host, int port, String vdb) throws Exception {
	    
		String url = "jdbc:teiid:" + vdb + "@mm://" + host + ":" + port;
		if(isShowPlan()){  
		    url += ";showplan=on"; //note showplan setting
		}
		Class.forName("org.teiid.jdbc.TeiidDriver");	
		return DriverManager.getConnection(url,getUserName(), getPassword());		
	}
	
	static Connection getDataSourceConnection(String host, int port, String vdb) throws Exception {
		TeiidDataSource ds = new TeiidDataSource();
		ds.setDatabaseName(vdb);
		ds.setUser(getUserName());
		ds.setPassword(getPassword());
		ds.setServerName(host);
		ds.setPortNumber(port);
		if(isShowPlan()){
		    ds.setShowPlan("on"); //turn show plan on
		}	
		return ds.getConnection();
	}
	
	static String getHost() {
        return (System.getProperties().getProperty(HOST) != null ? System.getProperties().getProperty(HOST) : HOST_DEFAULT );
    }
	
	static int getPort() {
        return (System.getProperties().getProperty(PORT) != null ? Integer.parseInt(System.getProperties().getProperty(PORT)) : PORT_DEFAULT );
    }
	
	static String getUserName() {
		return (System.getProperties().getProperty(USERNAME) != null ? System.getProperties().getProperty(USERNAME) : USERNAME_DEFAULT );
	}
	
	static String getPassword() {
		return (System.getProperties().getProperty(PASSWORD) != null ? System.getProperties().getProperty(PASSWORD) : PASSWORD_DEFAULT );
	}
	
	static boolean isShowPlan () {
	    return System.getProperties().getProperty(SHOWPLAN) != null ? Boolean.parseBoolean(System.getProperties().getProperty(SHOWPLAN)) : SHOWPLAN_DEFAULT;
	}
	
	static boolean isUseDatasource() {
	    return System.getProperties().getProperty(USE_DATASOURCE) != null ? Boolean.parseBoolean(System.getProperties().getProperty(USE_DATASOURCE)) : USE_DATASOURCE_DEFAULT;
	}
	
	public static boolean execute(Connection connection, String sql) throws Exception {
        
        boolean hasRs = true;
		try {
			Statement statement = connection.createStatement();
			
			hasRs = statement.execute(sql);
			
			if (!hasRs) {
				int cnt = statement.getUpdateCount();
				System.out.println("----------------\r");
				System.out.println("Updated #rows: " + cnt);
				System.out.println("----------------\r");
			} else {
				ResultSet results = statement.getResultSet();
				ResultSetMetaData metadata = results.getMetaData();
				int columns = metadata.getColumnCount();
				System.out.println("Results");
				for (int row = 1; results.next(); row++) {
					System.out.print(row + ": ");
					for (int i = 0; i < columns; i++) {
						if (i > 0) {
							System.out.print(",");
						}
						System.out.print(results.getObject(i+1));
					}
					System.out.println();
				}
				results.close();
			}
			
			if(isShowPlan()){
			    System.out.println("Query Plan");
	            System.out.println(statement.unwrap(TeiidStatement.class).getPlanDescription());
			}
				
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return hasRs;
	}

}
