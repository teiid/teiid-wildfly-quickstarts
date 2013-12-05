Infinispan Local-Cache Quickstart
================================

This quickstart demonstrates how Teiid can access a cache of Java Objects.

The example can be deployed using Maven from the command line or from Eclipse using
JBoss Tools.

Assumptions:
-  Teiid has been deployed to your jboss as server.
-  Teiid user has been setup - edit standalone/configuration/teiid-security-users.properties and add your user and password

	the default is username=user   password=user

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)


####################
#   Setup
####################

Setup can be done either manually (see Manual Setup) or using maven (see Setup using the JBoss AS Maven plugin) 


#########################################
### Manual setup
#########################################

1) shutdown jbossas server

2) run:  mvn clean install

3) setup the module that contains the infinispan-quickstart-pojos.jar.
	-	under  src/module,  copy 'com' directory to <jbossas-dir>/modules/
	-   under  target, copy  infinispan-quickstart-pojos.jar to <jbossas-dir>/modules/com/client/quickstart/pojos/main

4) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml

5) run the setup.cli  script to setup Infinispan cache and resource adapter for Infinispan connector

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/infinispan-local-cache/src/scripts/setup.cli 
            
6) deploy the sample application using the management console at http://localhost:9990

	* use the management console at http://localhost:9990 to deploy infinispan-quickstart.war from the target directory
	
7) deploy the VDB: infinispan-vdb.xml

	* copy infinispancache-vdb.xml and infinispancache-vdb.xml.dodeploy to {jbossas.server.dir}/standalone/deployments	


8) [Required] Open a browser to:  http://localhost:8080/infinispan-quickstart/home.jsf
This will trigger the loading of the 10 Orders and then present that list on the page.

9) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:orders@mm://localhost:31000
-  queries 

[1] select * from OrdersView
[2] select * from OrdersView where OrderNum > 3



#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) shutdown jbossas server

2) run:  mvn clean install

3) install the pojo.jar as a module

	*  `mvn install -Pinstall-module`


4) Start the server

	*  run:  ./standalone.sh -c standalone-teiid.xml
	
5) setup the Infinispan Cache

    * `mvn -Psetup-cache jboss-as:add-resource` 
    
6) setup Infinispan as a datasource
    
    * `mvn -Psetup-datasource jboss-as:add-resource`  
    
7) deploy the sample application infinispan-quickstart.war and the infinispan-vdb.xml artifacts

	* `mvn install -Pdeploy-artifacts`
	
8) RESTART the jboss as server.  Without using CLI to configure the resources, the resource isn't activated.  
		Therefore, jboss-as requires a restart.
	
9) [Required] Open a browser to:  http://localhost:8080/infinispan-quickstart/home.jsf
This will trigger the loading of the 10 Orders and then present that list on the page.

10) Use a sql tool, like SQuirreL, to connect and issue following example query:

-  connect:  jdbc:teiid:orders@mm://localhost:31000
-  queries 

[1] select * from OrdersView
[2] select * from OrdersView where OrderNum > 3