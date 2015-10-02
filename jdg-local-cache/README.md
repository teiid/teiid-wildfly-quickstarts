JDG Local-Cache (Library Mode) Quickstart
================================

Level: Intermediate
Technologies: Teiid, Infinispan, Library Mode
Target Product: DV 6.x, JDG 6.x
Source: <https://github.com/teiid/teiid-quickstarts>

What is it?
-----------

This quickstart demonstrates how Teiid can access a cache of Java Objects stored in a 
local JDG cache running library mode.

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
-  JDG 6.5 eap modules kit.  If the JDG version is different from that is defined in the root pom, then the
JDG slot and version should be updated and then rebuild the project.

NOTE: can obtain JDG kit distributions on Red Hat's Customer Portal at https://access.redhat.com/jbossnetwork/restricted/listSoftware.html


   
####################
#  JDG Setup
####################

1) shutdown jbossas server

2) Install the JBoss Data Grid eap modules kit into the modules location for your JBoss AS - Teiid instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


####################
#   Setup Teiid Server
####################

1) shutdown jbossas server

2) build quickstart artifacts

	run:  mvn clean install   

This will build jdg-quickstart-jboss-as7-dist.zip in the target directory.

3)  Deploy the distribution kit

-  Unzip the jdg-quickstart-jboss-as7-dist.zip under the server modules directory.
-  This will deploy the pojo module

4) Update module dependencies

*  [Required] Update the "your.pojo.module" reference in the translator-infinispan-cache module.xml


*  [Optional] If the version of JDG modules you installed were not ${jdg.slot}, then update the following:

		-  org.jboss.teiid.translator.infinispan.cache
		-  org.jboss.teiid.resource-adapter.infinispan" slot="6"
	

The module.xml file needs to be updated with the module name that has
the java class that's being stored in the JDG cache.  For this quickstart, 
this should be changed to - com.client.quickstart.pojos
    
		
5) Configure resource-adapter

	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-ds.xml
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


8) deploy the sample application war (target/jdg-quickstart.war) that will configure the JDG cache from a file

	* use the management console at http://localhost:9990 to deploy target/jdg-quickstart.war from the target directory
		or
    * copy the file:  target/jdg-quickstart.war to the deployments folder in the server
    
    Make sure the following is seen in the server log before trying to execute any sql:
    
21:19:26,900 INFO  [stdout] (ServerService Thread Pool -- 65)  ******* Loaded local-quickstart-cache with number of objects 200
21:19:28,511 INFO  [stdout] (ServerService Thread Pool -- 65)  *******local-quickstart-cache is setup and tested

	This means the JDG cache was configured and registered via JNDI, which will make it available to Teiid resoure-adapter.
		
		
9) deploy the VDB: jdg-local-cache-vdb.xml

	* copy files jdg-local-cache-vdb.xml and jdg-local-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1) Change your working directory to "${quickstart.install.dir}/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="Stocks" -Dsql="Insert into Stock (productId, symbol, price, companyname) Values (99, 'WMT', 45.35, 'Walmart')"  -Dusername="teiidUser" -Dpassword="pwd"


or 

3) Use a sql tool, like SQuirreL, to connect and issue following example queries:

-  connect:  jdbc:teiid:Stocks@mm://localhost:31000



#################
# Example Queries:
#################

[Selects]
 	select productId, symbol, price, companyname from Stock

 	select productId, symbol, price, companyname from Stock where symbol = 'CIS'

 	select productId, symbol, price, companyname from Stock where price < '60.50'

[Insert]
	Insert into Stock (productId, symbol, price, companyname) Values (599, 'WMT', 45.35, 'Walmart');

[Update]
	Update Stock set companyname='Apple Corp' where productId = 4

[Delete]
	Delete From Stock where productId = 3


