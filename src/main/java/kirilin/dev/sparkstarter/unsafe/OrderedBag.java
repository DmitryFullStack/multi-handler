package kirilin.dev.sparkstarter.unsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class OrderedBag <T>{

    private List<T> list;

    public OrderedBag(T [] args) {
        this.list = new ArrayList<>(nonNull(args) ? asList(args) : Collections.emptyList());
    }

    public T getAndRemove(){
        return list.remove(0);
    }

    public int size(){
        return list.size();
    }
}
