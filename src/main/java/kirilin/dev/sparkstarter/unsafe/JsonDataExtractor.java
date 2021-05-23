package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component("json")
public class JsonDataExtractor implements DataExtractor {
    @Override
    public Dataset<Row> readData(String pathToData, ConfigurableApplicationContext context) {
        context.getBean(SparkSession.class).read().json(pathToData);
        return null;
    }
}
