package kirilin.dev.sparkstarter.unsafe;

public interface FinalizerPostProcessor {

    Object postFinalize(Object retVal);
}
