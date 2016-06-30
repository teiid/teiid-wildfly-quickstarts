---
Level: Basic
Technologies: Teiid, VDB, VDB reuse, and federating an Excel File
Target Product: DV
Product Versions: DV 6.0+
Source: https://github.com/teiid/teiid-quickstarts
---

vdb-datafederation is the 'Hello World' example for Teiid.
================================

## VDB: 

* PortfolioExcel   -  source models, view's


## What is it?

This quickstart demonstrates how to read an Excel file and include it in 
the  data federation across multiple data sources (i.e., relational, text file and excel file).
This will demonstrate the following: 

-  how to federate data from an EXCEL File
-  how to define the source model using DDL
-  how to define a second vdb that reuses (extends) another vdb


## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)


# PREREQUISTES

* NOTE: This example relies upon the vdb-datafederation example and that it needs to be deployed prior to running this example. 
	Therefore, read the vdb-datafederation's README.md and follow its directions before continuing.

## Setup

1) Run the setup in vdb-datafederation quick start

2) Shutdown the server
		
3)  Copy Excel support files
	
- Copy the "teiidfiles" directory to the $JBOSS_HOME/ directory

	The src/teiidfiles/excelFiles contains otherholdings.xml
	
when complete, you should see $JBOSS_HOME/teiidfiles

4) Setup the Excel datasource

-  run the following CLI script

	-	cd to the $JBOSS_HOME/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/excel-as-a-datasource/src/scripts/setup.cli 

5)  Teiid Deployment:

Copy (deploy) the following VDB related files to the $JBOSS_HOME/standalone/deployments directory

	* Portfolio VDB
    	- src/vdb/portfolio-excel-vdb.xml
     	- src/vdb/portfolio-excel-vdb.xml.dodeploy


You should see the server log indicate the VDB is active with a message like:  TEIID40003 VDB PortfolioExcel.1 is set to ACTIVE

6)  Open the admin console to make sure the VDB is deployed

	*  open a brower to http://localhost:9990/console 

7)  See "Query Demonstrations" below to demonstrate data federation.

## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn exec:java -Dvdb="PortfolioExcel" -Dsql="example query" -Dusername="xx" -Dpassword="xx"

## Examples:

###  Source and Federated Queries

> NOTE:  For the following examples,  use the vdb:  Portfolio


*  Example a  - queries the Excel file

	select * from PersonalValuations.Sheet1


*  Example b  -  queries the view that joins the EXCEL file with the H2 table, Account, to retrieve other personal holdings valuations

	select * from OtherHoldings.PersonalHoldings



