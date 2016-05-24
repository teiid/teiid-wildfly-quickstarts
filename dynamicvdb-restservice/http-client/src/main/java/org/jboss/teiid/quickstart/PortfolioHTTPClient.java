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
package org.jboss.teiid.quickstart;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class PortfolioHTTPClient {

    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
        
        String hostname = "localhost";
        int port = 8080;
        String username = "restUser";
        String password = "password1!";
        if(args.length == 4) {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
            username = args[2];
            password = args[3];
        }
        
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpHost targetHost = new HttpHost(hostname, port);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        AuthScope scope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(scope, credentials);
        builder.setDefaultCredentialsProvider(credsProvider);
        HttpClient client = builder.build();
                        
        execute(client, new HttpGet("http://localhost:8080/PortfolioRest_1/Rest/foo/1"));
        execute(client, new HttpGet("http://localhost:8080/PortfolioRest_1/Rest/getAllStocks"));
        execute(client, new HttpGet("http://localhost:8080/PortfolioRest_1/Rest/getAllStockById/1007"));
        execute(client, new HttpGet("http://localhost:8080/PortfolioRest_1/Rest/getAllStockBySymbol/IBM"));
     
    }

    private static void execute(HttpClient client, HttpRequestBase httppost) {
        
        System.out.println("\n" + httppost.getRequestLine());

        HttpResponse response = null;
        try {
            response = client.execute(httppost);
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
         
            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
