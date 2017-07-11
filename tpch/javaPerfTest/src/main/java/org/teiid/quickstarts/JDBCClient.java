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
package org.teiid.quickstarts;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        Connection conn = getDataSourceConnection("localhost", "31000", "tpch", args[1], args[2]);
        try {
            List<File> files = listGenreatedQueryFiles(args[0]);
            float num = files.size();

            System.out.println("Starting query execution");

            long start = System.currentTimeMillis();
            for (File f : files) {
                String query = getTextFileContent(f);
                try {
                    System.out.print(f.getName() + "==>");
                    long testStart = System.currentTimeMillis();
                    long rowCount = execute(conn, query);
                    double executionTime = (System.currentTimeMillis() - testStart) * 0.001;
                    System.out.println(String.format("\t %.2f secs, rows = %d", executionTime, rowCount));
                } catch (Exception ex) {
                    throw new RuntimeException("Unable to execute query " + f.getName(), ex);
                }
            }
            long end = System.currentTimeMillis();

            float minutes = ((float) (end - start) / 60000);

            System.out.println("\nPerformance test finished.");
            System.out.println(String.format("\t\t %.2f minutes", minutes));
            System.out.println(String.format("\t\t %.2f queries/minute", num / minutes));
        } finally {
            conn.close();
        }
    }

    private static List<File> listGenreatedQueryFiles(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("You must specify the directory with SQL queries");
        }
        File generateDir = new File(string);
        if (!generateDir.exists()) {
            throw new IllegalArgumentException("The directory with generated SQL queries doesnt exist ["
                    + generateDir.getAbsolutePath() + "]");
        }

        List<File> sqlQueries = Arrays.asList(generateDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith("sql");
            }

        }));
        if (sqlQueries.size() == 0) {
            throw new IllegalArgumentException("The directory with generated sql files doesnt contain ANY .sql files["
                    + generateDir.getAbsolutePath() + "]");
        }

        System.out.println("Found " + sqlQueries.size() + " queries.");
        Collections.sort(sqlQueries, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return Integer.compare(Integer.parseInt(o1.getName().substring(0, o1.getName().indexOf('.'))),
                        Integer.parseInt(o2.getName().substring(0, o2.getName().indexOf('.'))));
            }
        });
        return sqlQueries;
    }

    private static String getTextFileContent(File f) {
        StringBuffer sb = new StringBuffer();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append("\n" + line);
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

    static Connection getDataSourceConnection(String host, String port, String vdb, String username, String password)
            throws Exception {
        TeiidDataSource ds = new TeiidDataSource();
        ds.setDatabaseName(vdb);
        ds.setUser(username);
        ds.setPassword(password);
        ds.setServerName(host);
        ds.setPortNumber(Integer.valueOf(port));

        try {
            return ds.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Caught exception");
            Thread.sleep(20000);
            System.out.println("Retrying ");
            return ds.getConnection();
        }
    }

    public static long execute(Connection connection, String sql) throws Exception {
        Statement statement = null;
        ResultSet results = null;
        long count = 0;
        try {
            statement = connection.createStatement();
            results = statement.executeQuery(sql);
            // Go through the result set
            while (results.next()) {
                count++;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while executing a query", e);
        } finally {
            if (results != null) {
                results.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return count;
    }
}
