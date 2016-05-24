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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;

public class PortfolioCXFClient {
	
    static String HOSTNAME = "localhost";
    static String HOSTPORT = "8080";
	static String USERNAME = "restUser";
	static String PASSWORD = "password1!";
	
	static String[] apis = {"http://${hostname}:${port}/PortfolioRest_1/Rest/foo/1", 
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStocks", 
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStockById/1007",
	                 "http://${hostname}:${port}/PortfolioRest_1/Rest/getAllStockBySymbol/IBM"}; 
	
	
    public static void main(String[] args) throws URISyntaxException, IOException {
        
        if(args.length == 4) {
            HOSTNAME = args[0];
            HOSTPORT = args[1];
        	USERNAME = args[2];
        	PASSWORD = args[3];
        }
        
        for(int i = 0 ; i <  apis.length ; i ++){
            String api = apis[i];
            api = api.replace("${hostname}", HOSTNAME);
            api = api.replace("${port}", HOSTPORT);
            
            WebClient client = WebClient.create(api);
            client.header("Accept", MediaType.APPLICATION_XML);
            client.header("Authorization", "Basic " + Base64Utility.encode((USERNAME + ":" + PASSWORD).getBytes()));
            
            Response resp = client.get();
            if(resp.getStatus() == 200) {
                IOUtils.copy((InputStream) resp.getEntity(), System.out);
                System.out.println("\n");
            } else {
                System.out.println(resp.getStatus() + ", " + resp.getStatusInfo());
            }
            
            client.close();
        }
        
    }

}
