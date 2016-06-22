package org.teiid.quickstarts.SparkAccessTeiid;

import java.util.List;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumeTeiid {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeTeiid.class);
	// Teiid Username
	private static final String TEIID_USERNAME = "odataUser";
	// Teiid password
	private static final String TEIID_PWD = "password1!";

	private static final String TEIID_CONNECTION_URL = "jdbc:teiid:Portfolio@mm://127.0.0.1:31000";

	private static final JavaSparkContext sc = new JavaSparkContext(
			new SparkConf().setAppName("SparkJdbcFromTeiid").setMaster("local[*]"));

	private static final SQLContext sqlContext = new SQLContext(sc);

	public static void main(String[] args) {

		Properties properties = new Properties();
		properties.put("user", TEIID_USERNAME);
		properties.put("Password", TEIID_PWD);
		DataFrame jdbcDF = sqlContext.read().jdbc(TEIID_CONNECTION_URL,
				"product", properties);

		List<Row> productsInfo = jdbcDF.collectAsList();

		for (Row productInf : productsInfo) {
			LOGGER.info(productInf.toString());
		}
	}

}
