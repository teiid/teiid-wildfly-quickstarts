---
Level: Advanced
Technologies: Teiid, Materialization
Target Product: DV
Product Versions: DV 6.0+
Source: https://github.com/teiid/teiid-quickstarts
---

Dynamicvdb-materialization Quickstart
================================

## What is it?

Dynamicvdb-materialization demonstrates how to configure external materialization to so that caching can be used to improve query performance. 

*  VDB:   PortfolioMaterialize  
 

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)


# PREREQUISTES

* NOTE: This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example. 
	Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.


## Setup


1) Run the setup in dynamicvdb-datafederation quick start

2)  Start the server, if not already started

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 

3) VDB Deployment:

    Copy the following files to the "<jboss.home>/standalone/deployments" directory

     * src/vdb/portfolio-mat-vdb.xml
     * src/vdb/portfolio-mat-vdb.xml.dodeploy


4)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 	


## Query Demonstrations

==== Using the simpleclient example ====

1. Change your working directory to "<quickstart.install.dir>/simpleclient"

2. Use the simpleclient example to run the following queries:

Example:   mvn exec:java -Dvdb="PortfolioMaterialize" -Dsql="example query"



> NOTE:  For the following examples,  use the vdb:  PortfolioMaterialize

*  Example a  - Query the materialized View

	select * from StocksMatModel.stockPricesMatView  (should get 18 rows)

*  Example b  - Add a row to the product table, so that when the materialized view is updated,
the new row will be picked 

First, insert a new row into Products table:

	INSERT INTO PRODUCT (ID,SYMBOL,COMPANY_NAME) VALUES(2000,'RHT','Red Hat Inc')

wait 1 minute, as defined by:  "teiid_rel:MATVIEW_TTL" 60000,  in the portfolio-vdb.xml

then re-issue query in "a" and should now see 19 rows

*  Example c  - Issue a query that bypasses the cached data, and queries the original source

	select * from StocksMatModel.stockPricesMatView option nocache  (should be the same as "b")
