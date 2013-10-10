Overview:
=========
  The purpose of the custom_log4j_db_logger quickstart is to provide an example of using
  a database appender, instead of file based, to persist Teiid command log messages.

  This is example will do the following:
	-  compile and build appender jar, which will need to be deployed to the server
	-  use the setup.cli script to setup the H2 datasource and logging 
  	-  provide a vdb for previewing the log messages (see commandlog-vdb.xml)

  Use this quick start in conjunction with any other quick start, to see what the command log messages that will be produced
  when a query is submitted.

System requirements
-------------------

If you have not done so, please review the System Requirements (../README.md)


Perquisite: 
========================

  -  The JBoss server must be shut down in order to deploy the appender jar


####################
#   Setup
####################

1) From the command line, build the appender jar

	Run  `mvn clean install` 

2) Deploy appender jar to a module

	-  Copy the custom-log4j-db-logger-{version}.jar to your jboss server install, into the Teiid module: "org/jboss/teiid/main" folder
	-  Edit the module.xml and add <resource-root path="custom-log4j-db-logger-{version}.jar" />  to <resources>

3) Copy teiid support files

	- Copy the src/"teiidfiles" directory to the $JBOSS_HOME/ directory (should end up with  $JBOSS_HOOME/teiidfiles )

4)  Start the server

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

	For Linux:   ./standalone.sh -c standalone-teiid.xml	
	for Windows: standalone.bat -c standalone-teiid.xml

5)  Run the CLI script to setup the datasource and logging configurations

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file=../teiidfiles/setup_custom_logger.cli 


4) Deploy VDB

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/commandlog-vdb.xml
     (2) src/vdb/commandlog-vdb.xml.dodeploy
	
	
6) Submit queries so that command logging will be triggered

7) View the Command Log - See Query Demonstrations below

	
	
#########################################
### Query Demonstrations
#########################################

Now, view the command log using the CommandLog VDB. 

To preview the log messages, use the simpleclient quick start or SQL tool (like Squirrel).

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient quick start to run the following query:

Example:   

mvn install -Dvdb="CommandLog" -Dsql="SELECT * FROM LoggingView.OrderedMessages"

You should get results showing what commands have been logged.

-------


