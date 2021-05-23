package kirilin.dev.sparkstarter.unsafe;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("orderBy")
@RequiredArgsConstructor
public class SortTransformationOfficer implements TransformationOfficer {

    private final SortTransformation sortTransformation;

    @Override
    public Pair<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames) {
        String fieldName = WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords);
        ArrayList<String> additionalFields = new ArrayList<>();
        while (!remainingWords.isEmpty() && remainingWords.get(0).equalsIgnoreCase("and")){
            remainingWords.remove(0);
            additionalFields.add(WordsMatcher.findAndRemoveMatchingPiecesIfExists(fieldNames, remainingWords));
        }
        additionalFields.add(fieldName);
        return new Pair<>(sortTransformation, additionalFields);
    }
}
