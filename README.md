Teiid Quick starts
=================

Introduction
-------------------------

Quick starts for Teiid

To contribute a quick start, goto http://github.com/teiid/teiid-quickstarts

You can find the documentation at https://docs.jboss.org/author/display/teiidexamples/Teiid+Quick+Starts

The 'dist' folder contains the Maven scripts to build the quick start zip of the quick starts.

Be sure to read this entire document before you attempt to work with the quick starts. It contains the following information:

* [Available Quick starts](#availableQuickstarts): List of the available quick starts, a description and demonstrated features for each one.

* [System Requirements](#systemrequirements): List of software required to run the quick starts.

* [Run the Quick starts](#runningquickstarts): General instructions for building, deploying, and running the quick starts.


-------------------
<a id="availableQuickstarts"></a>
Available Quick starts 
---------------------

The following is a list of the currently available quick starts. The table lists each quick start name, the features it demonstrates, and it gives a brief description of the quick start. For more detailed information about a quick start, click on the quick start name.

Some quick starts are designed to enhance or extend other quick starts. These are noted in the **Prerequisites** column. If a quick start lists prerequisites, those must be installed or deployed before working with the quick start.

| **Quick start Name** | **Features Demonstrated** | **Description** | **Prerequisites** |
|:-----------|:-----------|:-----------|:-----------|:-----------|
| [datafederation](dynamicvdb-datafederation/README.md "data-federation") | Data Federation, TEXTTABLE, Native Query, VDB Reuse, External Materialization, Excel File | Shows how to expose multiple data sources for data federation and externally materialize a view to improve performance | None |
| [dataroles](dynamicvdb-dataroles/README.md "data-roles") | Data roles | Shows how to control Read/Write data access using data roles and masking | datafederation |
| [materialization](dynamicvdb-materialization/README.md "materialization") | Materialization | Demonstrates how to configure external materialization to so that caching can be used to improve query performance. | datafederation |
| [consume-webservices](webservices-as-a-datasource/README.md "consume-webservices") | 'ws' Translator, XMLTABLE, XMLPARSE, VIEW DDL | Demonstrates the use of the 'ws' translator to read a web service data source | None |
| [hibernate](hibernate-on-top-of-teiid/README.md "hibernate") | Hibernate Integration, VDB ReUse, Create VIEW, Create Trigger, TEXTTABLE | Demonstrates how a Hibernate application can add data federation capabilities at the data layer| datafederation |
| [jdg remote cache](jdg7.1-remote-cache/README.md "infinispan-remote-cache") | 'infinispan-hotrod' Translator | Demonstrates reading from a remote JDG cache running using Hot Rod client  | install JDG 7.1.1 EAP Client Module kit |
| [jdg remote cache materialization](jdg7.1-remote-cache-materialization/README.md "infinispan-remote-materialization") | 'infinispan-hotrod' Translator | Demonstrates using remote JDG cache for materialization  | install JDG 7.1.1 EAP Client Module kit |
| [simpleclient](simpleclient/README.md "simpleclient") | Teiid JDBC Connection | Demonstrates how to make a jdbc connection to Teiid using the Teiid JDBC Driver and DataSource  | None |
| [tpch](tpch/README.md "tpch") | Performance, Data Federation | Measures performance using TPC-H benchmark against two running Postgresql servers  | 2 running PostgresSQL servers, Unix machine host |
| [ldap-as-a-datasource](ldap-as-a-datasource/README.md) | 'ldap' Translator, FOREIGN TABLE DDL, OpenLDAP | Demonstrates using the ldap Translator to access data in OpenLDAP Server | OpenLDAP be installed, Groups 'HR' and Users under it be configured |
| [mongodb-as-a-datasource](mongodb-as-a-datasource/README.md) | 'mongodb' Translator, FOREIGN TABLE DDL | Demonstrates using the mongodb Translator to access documents in mongodb | MongoDB be installed, documents be inserted under 'Employee' connection |
| [hbase-as-a-datasource](hbase-as-a-datasource/README.md) | 'hbase' Translator, FOREIGN TABLE DDL | Demonstrates using the Hbase Translator via the Pheonix driver | Hbase be installed |
| [drools-integration](drools-integration/README.md) | Drools integration, UDF, FOREIGN Function DDL | Demonstrates using a simple rules invocation via a Function | |
| [socialMedia-as-a-datasource](socialMedia-as-a-datasource/README.md) | 'ws' Translator, accessing security rest service | Demonstrates using the WS Translator to call secured rest services, including Social Media(Twitter, Facebook, Weibo, etc), and transform the web service results into relational results | |

-------------------
<a id="systemrequirements"></a>
System Requirements 
-------------------

To run these quick starts with the provided build scripts, you need the following:

1. Java 1.6 or newer, to run JBoss AS and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version 

3. The JBoss Enterprise Application Platform (EAP) 6 distribution ZIP or the JBoss AS 7 distribution ZIP that's compatible with Teiid.
    * For information on how to install and run JBoss, refer to the server documentation.

4.  Set JBOSS_HOME to the root directory location of your application server, the scripts use this variable to
	determine where to deploy to.


------------------
<a id="runningquickstarts"></a>
Run the Quick Starts
------------------

The root folder of each quick start contains a README file with specific details on how to build and run the example.

Open a command line and navigate to the root directory of the quick start you want to run.
