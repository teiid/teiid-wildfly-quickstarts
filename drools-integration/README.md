Overview:
=========
  Demonstrates how to integrate drools so that business rules can be triggered
  based on the data moving thru Teiid.

  
  This quick start demonstrates one way to execute a set of business rules on
  a set of data.   In this example, a business rule is triggered to validate
  the data, and an error message is returned from calling the rule if it fails.
  Those failures will be accumulated in a temp table and will be returned in the 
  resultset to the client.
  
  The execution of the business rules will be triggered
  by calling the UDF (User Defined Function) from a virtual procedure.
  The UDF (RulesUDF.java) is written such that it can load any drools .drl file
  and process any POJO that is needed by the business rules.  The .drl file is 
  specified in the system property and is in the format of:

	-  value parameter format is:  POJO Class:.drl file[,POJO Class:.drl…]

	-  example:  <property name="org.teiid.drools.UDF" value="org.jboss.teiid.quickstart.data.MarketData:MyBusinessRules.drl"/>


  The UDF (RulesUDF.java) is written to pass in a variable number of arguments.
  <code>
	public static Object performRuleOnData(final String className, final String returnMethodName, final Object returnIfNull, final Object... arguments) {
	..
	}
  </code>
 
  where:
	-  className:  class name of the POJO to be instantiated and passed to the rules
	-  returnMethodName:  name of the method to call on the POJO to get the return value (if one)
	-  returnIfNull:  if returnMethodName returns a null value, then default to returnIfNull value
	-  arguments:  is a variable number of objects, that must match to the constructor of the POJO. 
		So all the arguments will be passed in when the "className" is instantiated.


 
  When the quick start is deployed, it will compile and build a jar that contains
  the UDF (RulesUDF) and the rules file (MyBusinessRules.drl).  This needs
  to be deployed with the server shut down in order to be included in the 
  system classpath.  

  Additionally, the drools-vdb.xml VDB, which contains the virtual procedure and
  the UDF Function model that will be used for processing the data, will also
  be deployed when the UDF jar is.

  The simple client quick start will be used for executing the test sql command.



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

1) Run the deploy in dynamicvdb-datafederation quick start

--- Don't Stop the server

2) Add the system property by running the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/drools-integration/src/scripts/setup.cli 


This property configures what drools file(s) will be loaded.
value parameter format is:  POJO Class:.drl file[,POJO Class:.drl file,..]

3) Shut Down the Server

4) Deploy VDB

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/drools-vdb.xml
     (2) src/vdb/drools-vdb.xml.dodeploy

5) Install the UDF module

    unzip the module: target/teiiddrools-udf-module-dist/zip into the <jboss.home>/modules/system/layers/base directory

5) Add module dependency to the org/jboss/teiid/main/module.xml

   Add:          <module name="org.jboss.teiid.businessrules.pojo" />
	
	
6) Start the server

 
#########################################
### Setup using the JBoss AS Maven plugin
#########################################

1) Run the deploy in dynamicvdb-datafederation quick start

=== Make sure server is up ====

2) Add the system property by running the following CLI script

	-	cd to the ${JBOSS_HOME}/bin directory
	-	execute:  ./jboss-cli.sh --connect --file={path}/drools-integration/src/scripts/setup.cli 


This property configures what drools file(s) will be loaded.
value parameter format is:  POJO Class:.drl file[,POJO Class:.drl file,..]

3)  Shut down server


4) Add the module dependency to the Teiid module.xml

	*  open command line and navigate to  <jboss.home>/modules/system/layers/base/org/jboss/teiid/main
	*  change the module.xml to add the module dependency
	
   Add:    <module name="org.jboss.teiid.businessrules.pojo" />

6) Deploy the artifacts (i.e., VDB and modules) by running the following command:

Open a command line and navigate to the root directory of this quickstart	
	
	*	`mvn package -Pdeploy`
	    	
5) Start the server

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server

	For Linux:   ./standalone.sh -c standalone-teiid.xml	
	for Windows: standalone.bat -c standalone-teiid.xml

	

7)  See "Query Demonstrations" below to demonstrate data federation.


##################################
#  Undeploy artifacts
##################################

1)  To undeploy the vdb and modules:

	*  mvn package -Pundeploy
	
2)  To undeploy the Teiid VDB, run the following command:

	*  mvn package -Pundeploy-vdb
	
	
#########################################
### Query Demonstrations
#########################################

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   

mvn install -Dvdb="PortfolioBR" -Dsql="SELECT * FROM (SELECT symbol, price, COMPANY_NAME, Stocks.performRuleOnData('org.jboss.teiid.quickstart.data.MarketData', 'getInvalidMessage', 'noMsg', COMPANY_NAME, symbol, price) AS MSG FROM Stock) As X WHERE MSG <> 'noMsg'"


You should get 4 rows in the result.  These 4 have a price >= $50

-------


