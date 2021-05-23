package kirilin.dev.sparkstarter.unsafe;

import java.beans.Introspector;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordsMatcher {
    public static String findAndRemoveMatchingPiecesIfExists(Set<String> options, List<String> pieces){
        StringBuilder match = new StringBuilder(pieces.remove(0));
        List<String> remainingOptions = options.stream().filter(option -> option.toLowerCase().startsWith(match.toString().toLowerCase()))
                .collect(Collectors.toList());
        if(remainingOptions.isEmpty()){
            return "";
        }
        while(remainingOptions.size() > 1){
            match.append(pieces.remove(0));
            remainingOptions.removeIf(matchingOption -> !matchingOption.toLowerCase().startsWith(match.toString().toLowerCase()));
        }
        while (!remainingOptions.get(0).equalsIgnoreCase(match.toString())){
            match.append(pieces.remove(0));
        }
        return Introspector.decapitalize(match.toString());
    }
}
