---
Level: Basic
Technologies: Teiid, VDB, Native Queries, VDB reuse, reading data from JDBC, delimited file, REST Service Through VDB
Target Product: DV
Product Versions: DV 6.1, DV 6.2, DV 6.3
Source: https://github.com/teiid/teiid-quickstarts
---

## What is it?

This quickstart demonstrates:

* how to define a vdb to enable a [REST Service Through VDB](https://docs.jboss.org/author/display/TEIID/REST+Service+Through+VDB).
* how to use the httpclient api to integrate VDB data via REST
* how to use the Resteasy Client to integrate VDB data via REST
* how to use the Apache CXF WebClient to integrate VDB data via REST
* how to use Swagger API Doc Page test all Rest API.

This quickstart depends upon the [vdb-datafederation](../vdb-datafederation) quickstart.  It needs to be deployed prior to running this example.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

## Setup

1)  Setup [vdb-datafederation](../vdb-datafederation)

Refer to [vdb-datafederation's README.md](../vdb-datafederation/README.md) Setup Section, make sure all setps are setup completely and correctly.

2) Create a Application User under group `rest`

Navigate to JBOSS_HOME, execute below command will create user `restUser` under group `rest`:

~~~
$ ./bin/add-user.sh -a -u restUser -p password1! -g rest
~~~ 

> NOTE: By default, REST services through a VDB are secured with security domain "teiid-security" and with security role "rest".
		
3)  Deploy VDB

Copy (deploy) the following VDB related files to the $JBOSS_HOME/standalone/deployments directory

~~~
src/vdb/portfolio-rest-vdb.xml
src/vdb/portfolio-rest-vdb.xml.dodeploy
~~~

You should see the server log indicate the VDB is active with a message like:  TEIID40003 VDB PortfolioRest.1 is set to ACTIVE

4) See **Query Demonstrations** below to demonstrate REST Service Through VDB.

## Query Demonstrations

The following Rest API will extract data from the VDB:

* http://localhost:8080/PortfolioRest_1/Rest/foo/1
* http://localhost:8080/PortfolioRest_1/Rest/getAllStocks
* http://localhost:8080/PortfolioRest_1/Rest/getAllStockById/1007
* http://localhost:8080/PortfolioRest_1/Rest/getAllStockBySymbol/IBM

> NOTE: Above Query Demonstrations assuming DV server run on localhost.

> NOTE: [http://localhost:8080/PortfolioRest_1/api](http://localhost:8080/PortfolioRest_1/api) will list all API.

### Run http client

~~~
$ cd http-client/
$ mvn package exec:java
~~~

> NOTE: If your teiid Server not run on localhost:8080, edit [http-client/pom.xml](http-client/pom.xml), to make sure arguments point to a correct hostname and port.

### Run resteasy client

~~~
$ cd resteasy-client/
$ mvn package exec:java
~~~

> NOTE: If your teiid Server not run on localhost:8080, edit [resteasy-client/pom.xml](resteasy-client/pom.xml), to make sure arguments point to a correct hostname and port.

### Run CXF Client

~~~
$ cd cxf-client/
$ mvn package exec:java
~~~

> NOTE: If your teiid Server not run on localhost:8080, edit [cxf-client/pom.xml](cxf-client/pom.xml), to make sure arguments point to a correct hostname and port.
