---
Level: Intermediate
Technologies: Teiid, Infinispan Library Mode
Target Product: DV, JDG
Product Versions: DV 6.1+, JDG 6.5+
Source: https://github.com/teiid/teiid-quickstarts
---

JDG Local-Cache (Library Mode) Quickstart
================================

# What is it?

This quickstart demonstrates the following:

* how Teiid can access a cache of Java Objects stored in a local JDG cache running library mode. 


# Quick Start requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

# PREREQUISTES

* JBoss application server to run Teiid
* The Teiid Jboss distribution kit
* JDG 6.5+ eap modules kit 
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

-  Unzip the target/jdg-quickstart-jboss-as7-dist.zip at the root of the JBOSS_HOME directory.
-  This will deploy the pojo module and the cache configuration file.

4) Update module dependencies

*  [Required] the org.infinispan.commons (slot="${jdg.slot}" or slot for version installed) module needs to have 
the pojo dependency added:

    <module name="com.client.quickstart.pojos"   export="true" />

*  [Optional] If the version of JDG modules you installed was not ${jdg.slot}, then update the following modules:

		-  org.jboss.teiid.translator.infinispan.cache
		-  org.jboss.teiid.resource-adapter.infinispan" slot="6"

		
5) Configure resource-adapter

	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>

Note:  the Infinispan cache will be configured using the infinispan.xml configuration file that's defined in the resource-adapter.  Make sure the file name and the "ConfigurationFilenamForLocalCache" property match.

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

*  deploy VDB for reading the cache from directory  src/vdb
	- copy files jdg-local-cache-vdb.xml and jdg-local-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


# Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn exec:java -Dvdb="Stocks" -Dsql="Insert into Stock (productId, symbol, price, companyname) Values (99, 'WMT', 45.35, 'Walmart')"  -Dusername="teiidUser" -Dpassword="pwd"


or 

3) Use a sql tool, like SQuirreL, to connect and issue following example queries:

-  connect:  jdbc:teiid:Stocks@mm://localhost:31000

# Example Queries for using jdg-local-cache-vdb.xml:

[Selects]
 	select productId, symbol, price, companyname from Stock

 	select productId, symbol, price, companyname from Stock where symbol = 'RHT50'

 	select productId, symbol, price, companyname from Stock where price < '60.50'

[Insert]
	Insert into Stock (productId, symbol, price, companyname) Values (99, 'WMT', 45.35, 'Walmart');

[Update]
	Update Stock set companyname='Apple Corp' where productId = 4

[Delete]
	Delete From Stock where productId = 3
