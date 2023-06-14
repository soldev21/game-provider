package com.megafair;


import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

@ApplicationScoped
public class CollectionHelper {

    public <T> Stream<List<T>> chunked(Collection<T> collection, int chunkSize) {
        AtomicInteger index = new AtomicInteger(0);

        return collection.stream()
            .collect(groupingBy(x -> index.getAndIncrement() / chunkSize))
            .entrySet()
            .stream()
            .sorted(comparingByKey())
            .map(Map.Entry::getValue);
    }

    public <T> Set<T> orderedSet(T... elements) {
        return Arrays.stream(elements)
            .collect(toCollection(LinkedHashSet::new));
    }
}
