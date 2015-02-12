JDG Local-Cache (Library Mode) Materialization Quickstart
================================

Level: Intermediate
Technologies: Teiid, Materialization, Infinispan, Library Mode
Target Product: DV, JDG
Product Versions: DV 6.1, JDG 6.4
Source: <https://github.com/teiid/teiid-quickstarts>

What is it?
-----------

This quickstart demonstrates how the Teiid materialization caching feature can use JDG as
the materialized target.


############################
#   NOTE:  This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example.
#       	Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.
############################


Assumptions:
-  Teiid has been deployed to your jboss as server and a Teiid user has been setup.

-------------------
Quick Start requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

####################
#  PREREQUISTES
####################

-  JBoss application server to run Teiid
-  The Teiid Jboss distribution kit
-  JDG 6.4 eap modules kit 

NOTE: can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html

############################
#   NOTE:  This example relies upon the dynamicvdb-datafederation example and that it needs to be deployed prior to running this example.
#       	Therefore, read the dynamicvdb-datafederation's README.md and follow its directions before continuing.
############################

   
####################
#  JDG Installation
####################

1) shutdown jbossas server

2) Install (unzip) the JBoss Data Grid eap modules kit into the server modules directory for your JBoss AS - Teiid instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


####################
#   Setup
####################

1) Run the setup in dynamicvdb-datafederation quick start

2) shutdown jbossas server

3) [Optional] If the version of JDG modules you installed was not JDG 6.4, then perform the following steps, otherwise skip to step 3

	1) Change the "jdg.slot" property in the pom.xml for this quickstart to the slot to use, and rebuild
		-  will update the com.client.quickstart.pojos pojo module
		-  will update the manifest for the jdg-quickstart.war 
			
	2) Update the following module.xml files in the server to the align the JDG slot (default set to slot="jdg-6.4"):

		-  org.jboss.teiid.translator.object
		-  org.jboss.teiid.resource-adapter.infinispan" slot="6"

4) build quickstart artifacts

	run:  mvn clean install   

This will build jdg-quickstart-jboss-as7-dist.zip in the target directory.

5)  Deploy the distribution kit

-  Unzip the jdg-quickstart-jboss-as7-dist.zip under the server modules directory.
-  This will install the pojo module

6) Update module dependencies

-  org.jboss.teiid.translator.object module.xml, change "your.pojo.module" reference to "com.client.quickstart.pojos"

The module.xml file needs to be updated with the module name that has
the java class that's being stored in the JDG cache.  For this quickstart, 
it's - com.client.quickstart.pojos

- the org.infinispan.commons (slot="jdg-6.4" or slot for version installed) module needs to have 
the pojo dependency added.  Copy the following module reference:

    <module name="com.client.quickstart.pojos"   export="true" />
    
		
7) Configure resource-adapter

	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>

	-   then replace the CacheTypeMap property with the following:
	
			local-quickstart-cache:com.client.quickstart.pojo.Stock;productId

8) Start the server

	*  run:  ./standalone.sh 

		or run the following if Teiid isn't configured in the default configuration
	*  ./standalone.sh -c standalone-teiid.xml 


9) Install the infinispan-cache translator

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh (.bat) --connect --file=../docs/teiid/datasources/infinispan/add-infinispan-cache-translator.cli


10) deploy the sample application war (target/jdg-quickstart.war) that will be used to preload the cache

	* use the management console at http://localhost:9990 to deploy target/jdg-quickstart.war from the target directory
		or
    * copy the file:  target/jdg-quickstart.war to the deployments folder in the server


####################
#   **** IMPORTANT **** This following step must be done before you deploy the VDB is deployed because
#   for this example, the war application is what creates and registers the cache into JNDI
#####################

11) [Required] Open a browser to:  http://localhost:8080/jdg-quickstart/home.jsf
This will trigger the creation of the cache and registering it into JNDI


	
13) deploy the VDB: jdg-local-cache-vdb.xml

	* copy folloing files to {jbossas.server.dir}/standalone/deployments	
 		- jdg-local-cache-materialization-vdb.xml
 		- jdg-local-cache-materialization-vdb.dodeploy


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="orders" -Dsql="example query"  -Dusername="teiidUser" -Dpassword="pwd"


or 

3) Use a sql tool, like SQuirreL, to connect and issue following example queries:

-  connect:  jdbc:teiid:orders@mm://localhost:31000


#################
# Example Loading and refreshing cache:
#################

-  Load the cache:

Insert Into StockCache.Stock (productId, symbol, price, companyname) Select product_id, symbol, price, company_name from Stocks.stock

-  View the cache:

select productId, symbol, price from StockCache.Stock
select productId, symbol, price from StockCache.Stock where productId > 1003
select productId, symbol, price from StockCache.Stock where productId = 1002


-  Clear cache so it can be re-loaded:

delete from StockCache.Stock




