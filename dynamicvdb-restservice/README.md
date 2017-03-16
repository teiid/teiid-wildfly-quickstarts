---
Level: Basic
Technologies: Teiid, Dynamic VDB, Native Queries, VDB reuse, reading data from JDBC, delimited file, REST Service Through VDB
Target Product: DV
Product Versions: DV 6.1+
Source: https://github.com/teiid/teiid-quickstarts
---

## What is it?

This quickstart demonstrates:

* how to define a dynamic vdb to enable a [REST Service Through VDB](https://docs.jboss.org/author/display/TEIID/REST+Service+Through+VDB).
* how to use the httpclient api to integrate VDB data via REST
* how to use the JAX-RS 2.0 Client API to integrate VDB data via REST with security authentication
* how to use the Resteasy Client API with JAX-RS 2.0 Client API to integrate VDB data via REST
* how to use the Resteasy Client API with HTTP client as engine to integrate VDB data via REST with security authentication
* how to use Swagger API Doc Page test all Rest API.

This quickstart depends upon the [dynamicvdb-datafederation](../dynamicvdb-datafederation) quickstart.  It needs to be deployed prior to running this example.

## System requirements

If you have not done so, please review the System Requirements [../README.md](../README.md)

## Setup

1)  Setup [dynamicvdb-datafederation](../dynamicvdb-datafederation)

Refer to [dynamicvdb-datafederation's README.md](../dynamicvdb-datafederation/README.md) Setup Section, make sure all setps are setup completely and correctly.

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

NOTE: [http://localhost:8080/PortfolioRest_1/api](http://localhost:8080/PortfolioRest_1/api) will list all API.

### Run http client

~~~
$ cd http-client/
$ mvn package exec:java
~~~

### Run resteasy client

~~~
$ cd resteasy-client/
$ mvn package exec:java
~~~
