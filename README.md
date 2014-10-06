Teiid Quickstarts
=================

Introduction
-------------------------

Quick starts for Teiid

To contribute a quick start, goto http://github.com/teiid/teiid-quickstarts

You can find the documentation at https://docs.jboss.org/author/display/teiidexamples/Teiid+Quick+Starts

The 'dist' folder contains the Maven scripts to build the quick start zip of the quickstarts.

Be sure to read this entire document before you attempt to work with the quickstarts. It contains the following information:

* [Available Quickstarts](#availableQuickstarts): List of the available quickstarts, a description and demonstrated features for each one.

* [System Requirements](#systemrequirements): List of software required to run the quickstarts.

* [Run the Quickstarts](#runningquickstarts): General instructions for building, deploying, and running the quickstarts.


-------------------
<a id="availableQuickstarts"></a>
Available Quickstarts 
---------------------

The following is a list of the currently available quickstarts. The table lists each quickstart name, the features it demonstrates, and it gives a brief description of the quickstart. For more detailed information about a quickstart, click on the quickstart name.

Some quickstarts are designed to enhance or extend other quickstarts. These are noted in the **Prerequisites** column. If a quickstart lists prerequisites, those must be installed or deployed before working with the quickstart.

| **Quickstart Name** | **Features Demonstrated** | **Description** | **Prerequisites** |
|:-----------|:-----------|:-----------|:-----------|:-----------|
| [datafederation](dynamicvdb-datafederation/README.md "data-federation") | Data Federation, TEXTTABLE, Native Query, External Materialization, Excel File | Shows how to expose multiple data sources for data federation and externally materialize a view to improve performance | None |
| [dataroles](dynamicvdb-dataroles/README.md "data-roles") | Data roles | Shows how to control Read/Write data access using data roles and masking | datafederation |
| [consume-webservices](webservices-as-a-datasource/README.md "consume-webservices") | 'ws' Translator, XMLTABLE, XMLPARSE, VIEW DDL | Demonstrates the use of the 'ws' translator to read a web service data source | None |
| [hibernate](hibernate-on-top-of-teiid/README.md "hibernate") | Hibernate Integration, VDB ReUse, Create VIEW, Create Trigger, TEXTTABLE | Demonstrates how a Hibernate application can add data federation capabilities at the data layer| datafederation |
| [jdg local cache](jdg-local-cache/README.md "infinispan-cache") | 'infinispan' Object Translator, OBJECTTABLE | Demonstrates reading reading a JDG cache running in library mode  | None |
| [jdg remote cache using dsl](jdg-remote-cache/README_JDG_REMOTE_QUERY_DSL.md "infinispan-cache-dsl") | 'infinispan' DSL Translator | Demonstrates reading from a remote JDG cache using hot rod client that's configured using protobuffers  | None |
| [simpleclient](simpleclient/README.md "simpleclient") | Teiid JDBC Connection | Demonstrates how to make a jdbc connection to Teiid using the Teiid JDBC Driver and DataSource  | None |


-------------------
<a id="systemrequirements"></a>
System Requirements 
-------------------

To run these quickstarts with the provided build scripts, you need the following:

1. Java 1.6 or better, to run JBoss AS and Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to build and deploy the examples
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

            mvn --version 

3. The JBoss Enterprise Application Platform (EAP) 6 Alpha distribution ZIP or the JBoss AS 7 distribution ZIP.
    * For information on how to install and run JBoss, refer to the server documentation.

4.  Set JBOSS_HOME to the root directory location of your application server, the scripts use this variable to
	determine where to deploy to.


------------------
<a id="runningquickstarts"></a>
Run the Quick Starts
------------------

The root folder of each quickstart contains a README file with specific details on how to build and run the example.

Open a command line and navigate to the root directory of the quickstart you want to run.
