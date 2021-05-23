package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

@Component("count")
public class CountSparkFinalizer implements SparkFinalizer {
    @Override
    public Object doAction(Dataset<Row> dataset, Class<?> modelClass, OrderedBag<?> orderedBag) {
        return dataset.count();
    }
}
