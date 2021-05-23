package kirilin.dev.sparkstarter.unsafe;

import javafx.util.Pair;
import kirilin.dev.multihandler.Source;
import kirilin.dev.multihandler.SparkRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class SparkInvocationHandlerFactory {

    private final DataExtractorResolver extractorResolver;
    private final Map<String, TransformationOfficer> officerMap;
    private final Map<String, SparkFinalizer> finalizerMap;

    @Setter
    private ConfigurableApplicationContext realContext;

    public SparkInvocationHandler create(Class<? extends SparkRepository> repoInterface) {
        Class<?> modelClass = getModelClass(repoInterface);
        Set<String> allFieldNames = getAllFieldNames(modelClass);
        String pathToData = modelClass.getAnnotation(Source.class).value();
        DataExtractor dataExtractor = extractorResolver.resolve(pathToData);

        Map<Method, List<Pair<SparkTransformation, List<String>>>> transformationChain = new HashMap<>();
        Map<Method, SparkFinalizer> method2Finalizer = new HashMap<>();

        Method[] methods = repoInterface.getMethods();
        for (Method method : methods) {
            TransformationOfficer currentOfficer = null;
            List<Pair<SparkTransformation, List<String>>> sparkTransformations = new ArrayList<>();
            ArrayList<String> methodWords = new ArrayList<>(asList(method.getName().split("(?=\\p{Upper})")));
            while (methodWords.size() > 1) {
                String officerName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(officerMap.keySet(), methodWords);
                if (!officerName.isEmpty()) {
                    currentOfficer = officerMap.get(officerName);
                }
                sparkTransformations.add(currentOfficer.createTransformation(methodWords, allFieldNames));
            }
            String finalizerName = "collect";
            if (methodWords.size() == 1) {
                finalizerName = Introspector.decapitalize(methodWords.get(0));
            }
            transformationChain.put(method, sparkTransformations);
            method2Finalizer.put(method, finalizerMap.get(finalizerName));
        }

        return SparkInvocationHandlerImpl.builder()
                .modelClass(modelClass)
                .pathToData(pathToData)
                .finalizerMap(method2Finalizer)
                .transformationChain(transformationChain)
                .dataExtractor(dataExtractor)
                .finalizerPostProcessor(new LazyInitializationFinalizerPostProcessor(realContext))
                .context(realContext)
                .build();
    }

    private Class<?> getModelClass(Class<? extends SparkRepository> repoInterface) {
        ParameterizedType genericInterface = (ParameterizedType) repoInterface.getGenericInterfaces()[0];
        return (Class<?>) genericInterface.getActualTypeArguments()[0];
    }

    private Set<String> getAllFieldNames(Class<?> modelClass) {
        return Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> Arrays.stream(field.getAnnotations()).noneMatch(annotation -> annotation.annotationType().equals(Transient.class)))
                .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

}
