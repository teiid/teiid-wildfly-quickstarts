---
Level: Intermmediate
Technologies: Teiid, Hibernate
Target Product: DV
Product Versions: DV 6.0+
Source: https://github.com/teiid/teiid-quickstarts
---

Hibernate-on-top-of-teiid Quickstart
================================

## What is it?

Hibernate-on-top-of-teiid demonstrates how a hibernate4 application can take advantage of multiple data sources through
a single Java Object by using the data federation capabilities of Teiid.  This example will extend the Portfolio VDB, which
is deployed by the dynamicvdb-datafederation quickstart, and create a view that will be mapped to a single 
relationally mapped object in Hibernate.

Hibernate is normally a 1 object to 1 data source mapping.  By using Teiid as the data source, the integration is no longer
approached from integrating at the application layer, but done at the data layer.  Making it easier to join together related information to be exposed
through Hibernate, rather than writing application code to merge related data.

> NOTE: This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example. Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

This example produces a WAR that is designed to be run on JBoss Enterprise Application Platform 6 or JBoss AS 7.

> NOTE: Correct Dependencies - Please note this example does not support working with Hibernate 3.  

## Build the project

- Before building, need to set the username/password in the hibernate config file

The src/main/webapp/WEB-INF/hibernate4-teiid-quickstart-ds.xml file needs to have the user-name and password changed from user/user to the
teiid user you wish to connect with.

- Open a command line and navigate to the root directory of this quickstart

	*   `mvn clean install`

## Setup

1) Run the setup in dynamicvdb-datafederation quick start

2) Make sure to start the server, if not already

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 

	
3) VDB Deployment:

-  run the following CLI script

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../quickstarts/hibernate-on-top-of-teiid/src/scripts/deploy_vdb.cli 


4) Deploy the web application

-  run the following CLI script

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../quickstarts/hibernate-on-top-of-teiid/src/scripts/deploy_war.cli 


5)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console

6)  See "Query Demonstrations" below to demonstrate data federation.


## Undeploy artifacts

1)  To undeploy the web application run the following command:

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../quickstarts/hibernate-on-top-of-teiid/src/scripts/remove_war.cli 

	
2)  To undeploy the Teiid VDB, run the following command:

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../quickstarts/hibernate-on-top-of-teiid/src/scripts/remove_vdb.cli 

	
## Query Demonstrations

### Access the application 

The application will be running at the following URL: http://localhost:8080/hibernate-on-top-of-teiid/.

The page should display a list of products.

To add (Register) a new product, enter the following:

-  Product ID  (must be unique)
-  Company Name
-  Stock Symbol

> NOTE: the Stock Symbol entered must exist in the marketdata-price.txt file. For convenience, RHT has already been added to the file that doesn't currently exist in the Products table.
