package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface SparkTransformation {
    Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldNames, OrderedBag<Object> args);
}
