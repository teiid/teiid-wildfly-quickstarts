---
Level: Intermediate
Technologies: Teiid, Infinispan Library Mode, Materialization
Target Product: DV, JDG
Product Versions: DV 6.1+, JDG 6.5
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Local-Cache (Library Mode) Quickstart
================================

# What is it?

This quickstart demonstrates how to configure an external materialization cache to improve performance when reading the data


Assumptions:

* Teiid has been deployed to your jboss as server and a Teiid user has been setup.

# Quick Start requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

# PREREQUISTES

* JBoss application server to run Teiid
* The Teiid Jboss distribution kit
* JDG 6.5 eap modules kit 

* If you plan to use the external materialization example, then the vdb-datafederation example data (datafiles and resource adapters) must first be installed
  Read the vdb-datafederation's README.md and follow its directions before continuing.

> NOTE: can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

# JDG Setup

1) shutdown jbossas server

2) Install the JBoss Data Grid eap modules kit into the modules location for your JBoss AS - Teiid instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.

# Teiid Server Setup

1) shutdown jbossas server

2) build quickstart artifacts

	run:  mvn clean install   

This will build jdg-quickstart-jboss-as7-dist.zip in the target directory.

3)  Deploy the distribution kit

-  Unzip the jdg-quickstart-jboss-as7-dist.zip at the root of the JBOSS_HOME directory.
-  This will deploy the pojo module and the cache configuration file.

4) Update module dependencies

*  [Required] the org.infinispan.commons (slot="jdg-6.5" or slot for version installed) module needs to have 
the pojo dependency added:

    <module name="com.client.quickstart.pojos"   export="true" />

*  [Optional] If the version of JDG modules you installed was not ${jdg.slot}, then update the following modules:

		-  org.jboss.teiid.translator.infinispan.cache
		-  org.jboss.teiid.resource-adapter.infinispan" slot="6"

		
5) Configure resource-adapter

*  configure for materialization
	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-materialization-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>


6) Start the server

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 
	
7) Install the infinispan-cache translator

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../docs/teiid/datasources/infinispan/add-infinispan-cache-translator.cli
	
	
8) deploy the VDB

*  deploy VDB for materialization from directory  src/vdb
	- copy files jdg-local-cache-mat-vdb.xml and jdg-local-cache-mat-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


# Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="StocksMat" -Dsql="examplequery"  -Dusername="teiidUser" -Dpassword="pwd"


or 

3) Use a sql tool, like SQuirreL, to connect and issue following example queries:

-  connect:  jdbc:teiid:StocksMat@mm://localhost:31000


# Example Queries for using jdg-local-cache-mat-vdb.xml:

[Select]

select productId, symbol, price, companyname from StocksMatModel.stockPricesMatView 


To see materialization and confirm the process is working, do the following:
*  Insert row into Accounts.Product table

Insert into Product (ID, SYMBOL, COMPANY_NAME) Values (2000, 'RHT', 'Red Hat Inc')

*  Wait for 2 mins - the refresh is set to 60 secs, but depending on when the insert occurred on the refresh cycle, it could take close to 2 minutes.
*  Reissue query:      elect name, id, email from PersonMatModel.PersonMatView
	and should see the RHT stock. 


