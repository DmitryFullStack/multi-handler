package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface SparkFinalizer {
    Object doAction(Dataset<Row> dataset, Class<?> modelClass, OrderedBag<?> orderedBag);
}
