webservices-as-a-datasource Quickstart
================================

Level: Beginners
Technologies: Teiid, WS Translator
Target Product: DV
Product Versions: DV 6.1
Source: <https://github.com/teiid/teiid-quickstarts>

What is it?
-----------

webservices-as-a-datasource demonstrates using the WS Translator to call a web services and
transform the web service results into relational results.

The CustomerRESTWebSvc war will be deployed and accessed as a data source using the WS Translator.  This war contains
customer information, so that no external web service is required in order to demonstrate the WS feature.

-------------------
System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md) in the root quick starts directory.


####################
#   Build
####################

Run the maven build to compile and create the web service war.

-  mvn clean compile war:war



#########################################
### Setup and Deployment
#########################################

1)  Start the server (if not already started)

	To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:
	
	For Linux:   ./standalone.sh	
	for Windows: standalone.bat

	If Teiid isn't configured in the default configuration, append the following arguments to the command to specify the configuration
		
	-c {configuration.file}  
	
	Example: -c standalone-teiid.xml 


2)  Deploy the war file that will be used as the web service resource to be accessed as a data source by Teiid

    -  copy the target/CustomerRestWebSvc.war to the <jboss.home>/standalone/deployments directory

	-  Test the war by opening a browser at the following URL:

http://localhost:8080/CustomerRESTWebSvc/MyRESTApplication/customerList


3) Install the Customer web service datasource to be referenced by the Teiid VDB

-  run the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect file={path}/webservice-as-a-datasource/src/scripts/setup.cli 


4)  Teiid WebService VDB Deployment:

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/webservice-vdb.xml
     (2) src/vdb/webservice-vdb.xml.dodeploy

5)  See "Query Demonstrations" below to demonstrate data federation.


#########################################
### Query Demonstrations
#########################################	

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="webservice" -Dsql="select * from CustomersView" -Dusername="xx" -Dpassword="xx"


NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be
complicated.  It would be better to install a Java client, such as SQuirreL, to run the 
queries below. 