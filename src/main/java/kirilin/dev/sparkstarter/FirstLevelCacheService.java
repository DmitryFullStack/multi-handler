package kirilin.dev.sparkstarter;

import kirilin.dev.sparkstarter.unsafe.DataExtractor;
import kirilin.dev.sparkstarter.unsafe.DataExtractorResolver;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstLevelCacheService {

    private Map<Class<?>, Dataset<Row>> model2Dataset = new HashMap<>();
    @Autowired
    private DataExtractorResolver extractorResolver;

    public List getDataFor(String id, String fkName, Class<?> modelClass, String path, ConfigurableApplicationContext context) {
        if(!model2Dataset.containsKey(modelClass)){
            DataExtractor dataExtractor = this.extractorResolver.resolve(path);
            Dataset<Row> dataset = dataExtractor.readData(path, context).persist();
            model2Dataset.put(modelClass, dataset);
        }
        return model2Dataset.get(modelClass).filter(functions.col(fkName).equalTo(id)).as(Encoders.bean(modelClass)).collectAsList();
    }

}
