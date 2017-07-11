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
