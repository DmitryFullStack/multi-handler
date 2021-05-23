package kirilin.dev.sparkstarter.unsafe;

import javafx.util.Pair;

import java.util.List;
import java.util.Set;

public interface TransformationOfficer {

    Pair<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames);

}
