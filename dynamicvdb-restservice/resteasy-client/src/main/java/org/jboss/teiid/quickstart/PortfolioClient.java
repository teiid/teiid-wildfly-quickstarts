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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;


public class PortfolioClient {
	
        static String HOSTNAME = "localhost";
        static String HOSTPORT = "8080";
	static String USERNAME = "restUser";
	static String PASSWORD = "password1!";
	
	static String[] apilist = {"http://${hostname}:${port}/PortfolioRest_1/Rest/foo/1", 
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStocks", 
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStockById/1007",
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStockBySymbol/IBM"}; 
        static String[] apis = new String[apilist.length];
	
	/**
	 * JAX-RS 2.0 Client API
	 */
	public void jaxrsClient() {
		
		System.out.println("JAX-RS 2.0 Client API");
		
		Client client = ClientBuilder.newBuilder().build();
		String authString = getBasicAuthentication();
		
		for(String api : apis){
			WebTarget target = client.target(api);
			Response response = target.request()
			                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
			                        .header(HttpHeaders.AUTHORIZATION, authString)
			                        .get();
			if(response.getStatus() == 200){
			    String value = response.readEntity(String.class);
	                    System.out.println(value);
	                    response.close();
			} else {
			    handleError(response);
			}
		}
		
	}

        private void handleError(Response response) {
                System.out.println(response.getStatus() + ", " + response.getStatusInfo());
                response.close();
        }
	
	/**
	 * Resteasy Client API with JAX-RS 2.0 Client API
	 */
	public void resteasyClient(){
		
		System.out.println("\nResteasy Client API with JAX-RS 2.0 Client API");
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		String authString = getBasicAuthentication();
		
		for(String api : apis){
			ResteasyWebTarget target = client.target(api);
			Response response = target.request()
                                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
                                    .header(HttpHeaders.AUTHORIZATION, authString)
                                    .get();
			if(response.getStatus() == 200){
			    String value = response.readEntity(String.class);
	                    System.out.println(value);
	                    response.close();
			} else {
			    handleError(response);
			}
		}
	}
	
	public void resteasyHttpBackendClient() {
		
		System.out.println("\nResteasy Client API with HTTP client as engine");
		
		HttpHost targetHost = new HttpHost("localhost", 8080, "http");
		
		// 1. Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		
		// 2. Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);
		
		// 3. Add AuthCache to the execution context
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		
		// 4. Create client executor and proxy
		HttpClientBuilder builder = HttpClientBuilder.create();
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		AuthScope scope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(USERNAME, PASSWORD);
		credsProvider.setCredentials(scope, credentials);
		builder.setDefaultCredentialsProvider(credsProvider);
		HttpClient httpClient = builder.build();
		
		// 5. Create ResteasyClient with http client as engine
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient, localContext);
		ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
		
		// 6. Execute Rest call via ResteasyClient
		String authString = getBasicAuthentication();
		for(String api : apis){
			ResteasyWebTarget target = client.target(api);
			Response response = target.request()
                                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
                                    .header(HttpHeaders.AUTHORIZATION, authString)
                                    .get();
			if(response.getStatus() == 200){
			    String value = response.readEntity(String.class);
	                    System.out.println(value);
	                    response.close();
			} else {
			    handleError(response);
			}
		}
	}
	
	
	private String getBasicAuthentication() {
		
		String token = USERNAME + ":" + PASSWORD ;
		String base64encoded = Base64.encodeBase64String(token.getBytes());
		return "Basic " + base64encoded ;
	}

    public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
        
        if(args.length == 4) {
                HOSTNAME = args[0];
                HOSTPORT = args[1];
                USERNAME = args[2];
        	PASSWORD = args[3];
        }
        
        PortfolioClient client = new PortfolioClient();
        for(int i = 0 ; i <  apilist.length ; i ++){
            String api = apilist[i];
            api = api.replace("${hostname}", HOSTNAME);
            api = api.replace("${port}", HOSTPORT);
            apis[i] = api;
        }                
      
        client.jaxrsClient();
        
        client.resteasyClient();
        
        client.resteasyHttpBackendClient();
    }


}
