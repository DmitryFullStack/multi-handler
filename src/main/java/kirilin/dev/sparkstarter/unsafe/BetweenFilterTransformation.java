package kirilin.dev.sparkstarter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("between")
public class BetweenFilterTransformation implements FilterTransformation {
    @Override
    public Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldNames, OrderedBag<Object> args) {
        return dataset.filter(functions.col(fieldNames.get(0)).between(args.getAndRemove(), args.getAndRemove()));
    }
}
