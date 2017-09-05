---
Level: Beginners
Technologies: Teiid, WS Translator
Target Product: DV
Product Versions: DV 6.0+
Source: https://github.com/teiid/teiid-quickstarts
---

webservices-as-a-datasource Quickstart
================================

# What is it?

webservices-as-a-datasource demonstrates using the WS Translator to call a web services and
transform the web service results into relational results.

The CustomerRESTWebSvc war will be deployed and accessed as a data source using the WS Translator.  This war contains
customer information, so that no external web service is required in order to demonstrate the WS feature.

# System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

# Build

Run the maven build to compile and create the web service war.

-  mvn clean compile war:war

# Setup and Deployment

1)  Start the server (if not already started)

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	To use one of the high-available (ha) configurations for clustering, append the following arguments to the command to specify the configuration
		
	-c {configuration.file} 
	
	Example: -c standalone-ha.xml 


2)  Deploy the war file that will be used as the web service resource to be accessed as a data source by Teiid

    -  copy the target/CustomerRESTWebSvc.war to the ${JBOSS_HOME}/standalone/deployments directory

	-  Test the war by opening a browser at the following URL:

http://localhost:8080/CustomerRESTWebSvc/MyRESTApplication/customerList


3) Install the Customer web service datasource to be referenced by the Teiid VDB

-  run the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../quickstarts/webservices-as-a-datasource/src/scripts/setup.cli 


4)  Teiid WebService VDB Deployment:

Copy the following files to the "${JBOSS_HOME}/standalone/deployments" directory

     (1) src/vdb/webservice-vdb.xml
     (2) src/vdb/webservice-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data federation.


# 3. Query Demonstrations

NOTE: before querying, if not already, will need to add user/pasword.

1. Change your working directory to "${JBOSS_HOME}/quickstarts/simpleclient"
2. Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="webservice" -Dsql="select * from CustomersView" -Dusername="teiidUser" -Dpassword="pwd"

or

Use a sql tool, like SQuirreL, to connect and issue following example query:

Queries for accessing the webservice VDB

URL connect: jdbc:teiid:webservice@mm://{host}:31000 

[source,sql]
.*Example Query SQL*
----
select * from CustomersView


