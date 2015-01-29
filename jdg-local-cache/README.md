JDG Local-Cache (Library Mode) Quickstart
================================

Level: Intermediate
Technologies: Teiid, Infinispan, Library Mode
Target Product: DV, JDG
Product Versions: DV 6.1, JDG 6.4
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
-  JDG 6.4 eap modules kit 

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

*  [Optional] If the version of JDG modules you installed were not JDG 6.4, then update the following:

	1) Change the "jdg.slot" property in the pom.xml for this quickstart to the slot to use, and rebuild
		-  will update the com.client.quickstart.pojos pojo module
		-  will update the manifest for the jdg-quickstart.war 
			
	2) Update the following module.xml files in the server to the align the JDG slot (default set to slot="jdg-6.4"):

		-  org.jboss.teiid.translator.object
		-  org.jboss.teiid.resource-adapter.infinispan" slot="6"
	

*  [Required] Update the "your.pojo.module" reference in the translator-object module.xml

The module.xml file needs to be updated with the module name that has
the java class that's being stored in the JDG cache.  For this quickstart, 
this should be changed to - com.client.quickstart.pojos

*  [Required] the org.infinispan.commons (slot="jdg-6.3" or slot for version installed) module needs to have 
the pojo dependency added:

    <module name="com.client.quickstart.pojos"   export="true" />
    
		
5) Configure resource-adapter

	-	open the file: {jbossas.server.dir}/docs/teiid/datasources/infinispan/infinispan-ds.xml
	-	copy and paste the resource-adapter section it into the server configuration, under the section:

        <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
            <resource-adapters>


6) Start the server

-  if Teiid configuration is installed into default configuration:
	*  run:  ./standalone.sh

-  if you want to start the server using the teiid configuration file:
	*  run:  ./standalone.sh -c standalone-teiid.xml

7) Install the infinispan-cache translator

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={jbossas.server.dir}/docs/teiid/datasources/infinispan/add-infinispan-cache-translator.cli


8) deploy the sample application war (target/jdg-quickstart.war) that will be used to preload the cache

	* use the management console at http://localhost:9990 to deploy target/jdg-quickstart.war from the target directory
		or
    * copy the file:  target/jdg-quickstart.war to the deployments folder in the server
	
9) deploy the VDB: jdg-local-cache-vdb.xml

	* copy files jdg-local-cache-vdb.xml and jdg-local-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


####################
#   **** IMPORTANT **** This following step must be done before you perform Query Demonstration, 
#   so that the cache will be created and bound into JNDI to be accessed by the VDB.
#####################

10) [Required] Open a browser to:  http://localhost:8080/jdg-quickstart/home.jsf
This will trigger the loading of 10 Orders and then present that list on the page.


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
# Example Queries:
#################


[1] select orderDate, orderedBy from Orders
[2] select * from OrdersView
[3] select * from OrdersView where OrderNum > 3
[4] Insert into Orders (OrderNum, OrderedBy) Values (99, 'TestPerson')
[5] Update Orders set OrderedBy='Testperson2' where OrderNum = 99
[6] Delete From Orders where OrderNum = 9

* When running either 4, 5, or 6, rerun one of the above select's to see the results to 
verify the changed data


