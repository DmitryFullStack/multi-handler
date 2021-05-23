package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortTransformation implements SparkTransformation {
    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldNames, OrderedBag<Object> args) {
        return dataset.orderBy(fieldNames.remove(0), fieldNames.stream().skip(1).toArray(String[]::new));
    }
}
