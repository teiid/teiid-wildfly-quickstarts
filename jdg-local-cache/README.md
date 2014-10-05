JDG Local-Cache (Library Mode) Quickstart
================================

This quickstart demonstrates how Teiid can access a cache of Java Objects stored in a 
local JDG 6.3.x cache running library mode.

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

Assumptions:
-  Teiid has been deployed to your jboss as server and a Teiid user has been setup.

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)

####################
#  PREREQUISTES
####################

-  Teiid has to be deployed to your jboss EAP server and a Teiid user has been setup.
   
####################
#  JDG Requirements
####################

1) shutdown jbossas server

2) Install the JBoss Data Grid 6.3.x version of the library modules kit for EAP into 
	the modules location for your Teiid/EAP instance.
   See Red Hat:   http://access.redhat.com  to obtain the kit.


####################
#   QuickStart Deployment
####################

1) shutdown jbossas server

2) build quickstart artifacts

	run:  mvn clean install   

This will build a jboss-as7 dist zip in the target directory.

3)  Deploy the distribution kit

-  Unzip the jdg-quickstart-jboss-as7-dist.zip under the server modules directory.
-  This will deploy the pojo module

4) Update module dependencies

-  translator-object

a)  depending on the version of JDG modules you installed, the module.xml may need to be
updated to the correct slot (default set to slot="jdg-6.3")

NOTE:  if you change the slot, then the pom.xml needs the maven-war-plugin dependencies updated (then rebuild quickstart)

b)  The "your.pojo.module" reference in the translator-object needs to be replaced with the module name that has
the java class that's being stored in the JDG cache
		
5) Configure resource-adapter

-  open the src/scripts/setup_resource_adapter.txt file and copy-n-paste the resource-adapter
segment into the server configuration (i.e., standalone.xml), in the  <subsystem xmlns="urn:jboss:domain:resource-adapters:1.1">
section.  There is a current bug that will not allow CLI script to be run that configures a resource-adapter that has a "slot" defined.


4) Start the server

-  if Teiid configuration is installed into default configuration:
	*  run:  ./standalone.sh

-  if you want to start the server using the teiid configuration file:
	*  run:  ./standalone.sh -c standalone-teiid.xml

5) run the add-infinispan-cache-translator.cli script to install translator infinispan-cache.

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/infinispan-local-cache/src/scripts/add-infinispan-cache-translator.cli 

6) deploy the sample application war using the management console at http://localhost:9990

	* use the management console at http://localhost:9990 to deploy infinispan-quickstart.war from the target directory

    or copy the file:  target/jdg-quickstart.war to the deployments folder in the server
	
7) deploy the VDB: jdg-local-cache-vdb.xml

	* copy jdg-local-cache-vdb.xml and jdg-local-cache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


####################
#   **** IMPORTANT **** This following be done before you move on to next step (9), 
#   so that the cache will be created and bound into JNDI to used by the VDB.
#####################

8) [Required] Open a browser to:  http://localhost:8080/jdg-quickstart/home.jsf
This will trigger the loading of the 10 Orders and then present that list on the page.



9) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:orders@mm://localhost:31000
-  queries 

[1] select orderDate, orderedBy from Order
[2] select * from OrdersView
[3] select * from OrdersView where OrderNum > 3
[4] Insert into Orders (OrderNum, OrderedBy) Values (100, 'TestPerson')


