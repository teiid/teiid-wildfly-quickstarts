Hibernate-on-top-of-teiid Quickstart
================================

Level: Intermmediate
Technologies: Teiid, Hibernate
Target Product: DV
Product Versions: DV 6.1
Source: <https://github.com/teiid/teiid-quickstarts>

What is it?
-----------

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

##########################
###  Build the project
##########################

- Open a command line and navigate to the root directory of this quickstart

	*   `mvn clean install`


#########################################
### Manual setup
#########################################

1) Run the setup in dynamicvdb-datafederation quick start

2) Make sure to start the server, if not already

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 

	
3) VDB Deployment:

    Copy the following files to the "<jboss.home>/standalone/deployments" directory

     * src/vdb/hibernate-portfolio-vdb.xml
     * src/vdb/hibernate-portfolio-vdb.xml.dodeploy

4) Deploy the web application buy running the following command:

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
	
2)  To undeploy the Teiid VDB, delete the 

	*  delete the vdb, hibernate-portfolio-vdb.xml, from the directory "<jboss.home>/standalone/deployments"
	
	
#########################################
### Query Demonstrations
#########################################	

Access the application 
---------------------

The application will be running at the following URL: http://localhost:8080/hibernate-on-top-of-teiid/.

The page should display a list of products.

To add (Register) a new product, enter the following:

-  Product ID  (must be unique)
-  Company Name
-  Stock Symbol

NOTE:  the Stock Symbol entered must exist in the marketdata-price.txt file.  
	   For convenience, RHT has already been added to the file that doesn't 
	   currently exist in the Products table.




