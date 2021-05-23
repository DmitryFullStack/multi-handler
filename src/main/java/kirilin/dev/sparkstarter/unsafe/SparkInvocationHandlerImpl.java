package kirilin.dev.sparkstarter.unsafe;

import javafx.util.Pair;
import lombok.Builder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Builder
public class SparkInvocationHandlerImpl implements SparkInvocationHandler {

    private Class<?> modelClass;
    private String pathToData;
    private DataExtractor dataExtractor;
    private FinalizerPostProcessor finalizerPostProcessor;
    private Map<Method, List<Pair<SparkTransformation, List<String>>>> transformationChain;
    private Map<Method, SparkFinalizer> finalizerMap;

    private ConfigurableApplicationContext context;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        OrderedBag<Object> orderedBag = new OrderedBag<>(args);
//        File file = new File("data/employees.csv");
//        Scanner scanner = new Scanner(new FileInputStream(file));
//        if(scanner.hasNext()){
//            System.out.println(scanner.nextLine());
//        }
        Dataset<Row> dataset = dataExtractor.readData(pathToData, context);

        List<Pair<SparkTransformation, List<String>>> sparkTransformations = transformationChain.get(method);
        SparkFinalizer finalizer = finalizerMap.get(method);

        for (Pair<SparkTransformation, List<String>> transformationWithFields : sparkTransformations) {
            dataset = transformationWithFields.getKey().transform(dataset, transformationWithFields.getValue(), orderedBag);
        }
        Object result = finalizer.doAction(dataset, modelClass, orderedBag);

        return finalizerPostProcessor.postFinalize(result);
    }
}
