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
