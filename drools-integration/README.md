---
Level: Intermediate 
Technologies: Teiid, BRMS, Drools, User Defined Function
Target Product: DV, BRMS
Product Versions: DV 6.1+
Source: https://github.com/teiid/teiid-quickstarts
---


## What is it?

**drools-integration** demonstrates  how a business rule can be triggered via a User Defined Function (UDF).

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

## Setup and Deployment

1)  Start the server (if not already started)

To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:

For Linux: ./standalone.sh	
for Windows: standalone.bat


To use one of the high-available (ha) configurations for clustering, append the following arguments to the command to specify the configuration
		
	-c {configuration.file} 

[source,xml]
.*Example*
----
./standalone.sh -c standalone-ha.xml
----


2) Install Modules

* Using the Maven build and copy dependencies:

~~~
$ cd ${JBOSS_HOME}/quickstarts/drools-integration/
$ mvn clean install dependency:copy-dependencies -s ../settings.xml
~~~

> NOTE - This will generate `drools-integration.jar` and `dependency` under target folder.


* Install drools as a module

Copy src/modules/org to $JBOSS_HOME by executing following:

~~~
$ cp -r src/modules/org/ $JBOSS_HOME/modules
~~~

Copy all dependency jars to drools module by executing the following:

~~~
$ cp target/dependency/*.jar $JBOSS_HOME/modules/org/drools/main
~~~

> NOTE - If you want to get a Supported version of Drools libraries, please Download `Red Hat JBoss BRMS 6 Maven Repository ` from [Red Hat Customer Portal](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=brms&downloadType=distributions) and Install Red Hat support libraries as drools module.

* Install drools-integration module

Execute CLI command to install drools-integration module

~~~
module add --name=org.jboss.teiid.drools --resources=/path/to/drools-integration.jar --dependencies=javax.api,org.slf4j,org.drools,org.jboss.teiid.api
~~~

3)  Teiid VDB Deployment:

[source]
.*VDB deployment*
----
cd to the $JBOSS_HOME/bin directory
execute the following CLI script:

	./jboss-cli.sh --connect --file=../quickstarts/drools-integration/src/scripts/deploy_vdb.cli 
----

or can manually deploy the vdb by doing the following:

Copy the following files to the "$JBOSS_HOME/standalone/deployments" directory

     (1) drools-integration/src/vdb/drools-vdb.xml
     (2) drools-integration/src/vdb/drools-vdb.xml.dodeploy

4) See "Query Demonstrations" below to demonstrate data query.

# 3. Query Demonstrations

NOTE: before querying, if not already, will need to add user/pasword.

1. Change your working directory to "${JBOSS_HOME}/quickstarts/simpleclient"
2. Use the simpleclient example to run the following queries:

Example: mvn exec:java -Dvdb="DroolsVDB" -Dsql="SELECT performRuleOnData('org.jboss.teiid.drools.Message', 'Hello World', 0) FROM FOO" -Dusername="teiidUser" -Dpassword="pwd"

> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries.

or

Use a sql tool, like SQuirreL, to connect and issue following example query:

Queries for accessing DroolsVDB

URL connect: jdbc:teiid:DroolsVDB@mm://{host}:31000 

[source,sql]
.*Example Query SQL*
----
SELECT performRuleOnData('org.jboss.teiid.drools.Message', 'Hello World', 0) FROM FOO


