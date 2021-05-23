package kirilin.dev.sparkstarter.unsafe;

import lombok.SneakyThrows;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("collect")
public class CollectSparkFinalizer implements SparkFinalizer {
    @SneakyThrows
    @Override
    public Object doAction(Dataset<Row> dataset, Class<?> modelClass, OrderedBag<?> orderedBag) {
        Encoder<?> encoder = Encoders.bean(modelClass);

//        add fake column to spark schema for model class if this field is collection
        List<String> listFieldNames = Arrays.stream(encoder.schema().fields()).filter(structField -> structField.dataType() instanceof ArrayType)
                .map(StructField::name)
                .collect(Collectors.toList());
        for (String fieldName : listFieldNames) {
            ParameterizedType genericType = (ParameterizedType)modelClass.getDeclaredField(fieldName).getGenericType();
            Class c = (Class)genericType.getActualTypeArguments()[0];
            dataset = dataset.withColumn(fieldName, functions.lit(null).cast(DataTypes.createArrayType(DataTypes.createStructType(Encoders.bean(c).schema().fields()))));
        }

        return dataset.as(encoder).collectAsList();
    }
}
