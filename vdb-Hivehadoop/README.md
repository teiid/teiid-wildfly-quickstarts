| **Datasource** | **Level** | **Technologies** | **Prerequisites** | **Description** |
|:---------|:----------|:-----------------|:------------------|:----------------|
|Hadoop, Hive |Beginner |Teiid, Dynamic VDB, View, Hive Translator |Hadoop Services, HiveServer2 |Demonstrates using the Hive Translator with HiveServer2 JDBC Driver to access data in Hadoop HDFS |

## What's this

This example demonstrates using the Hive Translator with HiveServer2 JDBC Driver to access data in Hadoop HDFS. Dynamic VDB [hive-vdb.xml](src/main/resources/hive-vdb.xml) be used to define View within DDL metadata.

The examples use `java:/hiveDS` referenced with HiveServer2 data source, which will be setup automatically while the examples start running.

## Prerequisites

**1.** Hadoop Services

Refer to [../README.md](../README.md) **Prerequisites** section to install Hadoop and start Hadoop Services


**2.** HiveServer2 

Refer to [../README.md](../README.md) **Prerequisites** section to install Hive and start HiveServer2

**3.** Load Sample Data

With above 2 setps use the following commands to load [hive-sample.txt](src/main/resources/hive-sample.txt):

~~~
$ ./bin/hive
hive> CREATE TABLE IF NOT EXISTS employee (eid int, name String, salary String, destination String);
hive> LOAD DATA LOCAL INPATH '/path/to/hive-sample.txt' OVERWRITE INTO TABLE employee;
~~~

**4.** Edit JDBC Parameters

Edit [hive.properties](src/main/resources/hive.properties) make sure it point to a running HiveServer2.

## Dependencies

To add Teiid runtime, admin

~~~
<dependency>
    <groupId>org.jboss.teiid</groupId>
    <artifactId>teiid-runtime</artifactId>
    <version>${version.teiid}</version>
</dependency>
<dependency>
    <groupId>org.jboss.teiid</groupId>
    <artifactId>teiid-admin</artifactId>
    <version>${version.teiid}</version>
</dependency>
~~~

To add Translators and Resource Adapters

~~~
<dependency>
    <groupId>org.jboss.teiid.connectors</groupId>
    <artifactId>translator-hive</artifactId>
    <version>${version.teiid}</version>
</dependency>
<dependency>
    <groupId>org.jboss.teiid.connectors</groupId>
    <artifactId>translator-jdbc</artifactId>
    <version>${version.teiid}</version>
</dependency>
<dependency>
    <groupId>org.jboss.narayana.jta</groupId>
    <artifactId>narayana-jta</artifactId>
    <version>${version.narayana}</version>
</dependency>
<dependency>
    <groupId>org.jboss.ironjacamar</groupId>
    <artifactId>ironjacamar-jdbc</artifactId>
    <version>${version.ironjacamar}</version>
</dependency>
<dependency>
    <groupId>org.jboss.ironjacamar</groupId>
    <artifactId>ironjacamar-core-api</artifactId>
    <version>${version.ironjacamar}</version>
</dependency>
<dependency>
    <groupId>org.jboss.ironjacamar</groupId>
    <artifactId>ironjacamar-core-impl</artifactId>
    <version>${version.ironjacamar}</version>
</dependency>
~~~

To add thirdpart dependency

~~~
<dependency>
    <groupId>org.apache.hive</groupId>
    <artifactId>hive-jdbc</artifactId>
    <version>${version.apache.hive}</version>
</dependency>		
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-core</artifactId>
    <version>${version.apache.hadoop}</version>
</dependency>
~~~

## Run

* Run from Source code

Import source code to a IDE(Eclipse), run TeiidEmbeddedHadoopDataSource as Java Application.

* Run from mvn

~~~
$ cd teiid-embedded-examples/bigdata-integration/hadoop-integration-hive
$ mvn clean install
$ mvn exec:java
~~~
