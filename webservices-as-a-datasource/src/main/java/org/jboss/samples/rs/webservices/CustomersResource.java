package org.jboss.samples.rs.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.core.MediaType;

/**
 * 
 * @author bmincey
 *
 */
@Path("/MyRESTApplication")
public class CustomersResource
{
    private static final String xmlFile = "customerList.xml";

    /**
     * 
     * @return
     */
    @GET
    @Path("customerList")
    @Produces({ MediaType.APPLICATION_XML })
    public String getCustomerList()
    {
        System.out.println("*** Accessing /MyRESTApplication/customerList");
        
        StringBuffer fileContents = new StringBuffer();

        try
        {
            System.out.println("*** Read file: " + CustomersResource.xmlFile);
            
            InputStream inputStream 
                = this.getClass().getClassLoader().getResourceAsStream(CustomersResource.xmlFile);
            
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            try
            {
                String line = null;

                while ((line = input.readLine()) != null)
                {
                    fileContents.append(line);
                    fileContents.append(System.getProperty("line.separator"));
                }
            }
            finally
            {
                input.close();
                
                System.out.println("*** Completed reading file: " + CustomersResource.xmlFile);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        System.out.println("*** Return file contents as application/xml");

        return fileContents.toString();
    }
}
