package kirilin.dev.sparkstarter.unsafe;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("findBy")
@RequiredArgsConstructor
public class FilterTransformationOfficer implements TransformationOfficer {

    private final Map<String, FilterTransformation> transformationMap;

    @Override
    public Pair<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames) {
        String fieldName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords);
        String filterName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(transformationMap.keySet(), remainingWords);
        return new Pair<>(transformationMap.get(filterName), Collections.singletonList(fieldName));
    }
}
