Hibernate-on-top-of-teiid demonstrates how a hibernate4 application can take advantage of multiple data sources through
a single Java Object by using the data federation capabilities of Teiid.  This example will extend the Portfolio VDB, which
is deployed by the dynamicvdb-datafederation quickstart, and create a view that will be mapped to a single 
relationally mapped object in Hibernate.

Hibernate is normally a 1 object to 1 data source mapping.  By using Teiid as the data source, the integration is no longer
approached from integrating at the application layer, but done at the data layer.  Making it easier to join together related information to be exposed
through Hibernate, rather than writing application code to merge related data.

############################
#   NOTE:  This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example.
#       	Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.
############################

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

This example produces a WAR that is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.


Correct Dependencies
--------------------
Please note this example does not support working with Hibernate 3.  

 
#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) Run the setup in dynamicvdb-datafederation quick start

2) Make sure to start the server, if not already

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

	For Linux:   ./standalone.sh -c standalone-teiid.xml	
	for Windows: standalone.bat -c standalone-teiid.xml
	
3) Open a command line and navigate to the root directory of this quickstart

	*   `mvn clean install`

4) Deploy the vdb by running the following command:	
	
	*   `mvn package -Pdeploy-vdb`

3) Deploy the web application buy running the following command:

    	*   `mvn package jboss-as:deploy`
    	
5) RESTART the jboss as server.  Without using CLI to configure the resources, the resource isn't activated.  
		Therefore, jboss-as requires a restart.	

6)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	

7)  See "Query Demonstrations" below to demonstrate data federation.


##################################
#  Undeploy artifacts
##################################

1)  To undeploy the web application run the following command:

	*  mvn package jboss-as:undeploy
	
2)  To undeploy the Teiid VDB, run the following command:

	*  mvn package -Pundeploy-vdb
	
	
#########################################
### Query Demonstrations
#########################################	

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/hibernate-on-top-of-teiid/>.

The page should display a list of products.

To add (Register) a new product, enter the following:

-  Product ID  (must be unique)
-  Company Name
-  Stock Symbol

NOTE:  the Stock Symbol entered must exist in the marketdata-price.txt file.  
	   For convenience, RHT has already been added to the file that doesn't 
	   currently exist in the Products table.




