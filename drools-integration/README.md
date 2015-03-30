**drools-integration** demonstrates  how a business rule can be triggered via a User Defined Function (UDF).

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md) in the root quick starts directory.

## Setup and Deployment

1)  Start the server (if not already started)

To start the server, open a command line and navigate to the "bin" directory under the root directory of the JBoss server and run:

For Linux: ./standalone.sh	
for Windows: standalone.bat

append the following to the command to indicate which configuration to use if Teiid isn't configured in the default configuration

-c standalone-teiid.xml


2) Install Modules

* Using the Maven build and copy dependencies:

~~~
$ cd teiid-quickstarts/drools-integration/
$ mvn clean install dependency:copy-dependencies -s ../settings.xml
~~~

> NOTE - This will generate `drools-integration.jar` and `dependency` under target folder.


* Install drools as a module

Copy src/modules/org to $JBOSS_HOME

~~~
$ cp -r src/modules/org/ $JBOSS_HOME/modules
~~~

Copy all dependency jars to drools module

~~~
$ cp target/dependency/*.jar $JBOSS_HOME/modules/org/drools/main
~~~

> NOTE - If you want get a Supported version of Drools libraries, please Download `Red Hat JBoss BRMS 6 Maven Repository ` from [Red Hat Customer Portal](https://access.redhat.com/jbossnetwork/restricted/listSoftware.html?product=brms&downloadType=distributions) and Install Red Hat support libraries as drools module.

* Install drools-integration module

Execute CLI command to install drools-integration module

~~~
module add --name=org.jboss.teiid.drools --resources=/path/to/drools-integration.jar --dependencies=javax.api,org.slf4j,org.drools,org.jboss.teiid.api
~~~

3) Teiid Deployment

Copy the following files to the "<jboss.home>/standalone/deployments" directory

     (1) src/vdb/drools-vdb.xml
     (2) src/vdb/drools-vdb.xml.dodeploy

4) See "Query Demonstrations" below to demonstrate data query.

## Query Demonstrations

==== Using the simpleclient example ====

1) Change your working directory to "<quickstart.install.dir>/simpleclient"

2) Use the simpleclient example to run the following queries:

Example:   mvn install -Dvdb="DroolsVDB" -Dsql="SELECT performRuleOnData('org.jboss.teiid.drools.Message', 'Hello World', 0) FROM FOO"

> NOTE - depending on your OS/Shell the quoting/escaping required to run the example can be complicated.  It would be better to install a Java client, such as SQuirreL, to run the queries.


