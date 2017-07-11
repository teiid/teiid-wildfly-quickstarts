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
