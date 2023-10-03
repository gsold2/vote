package ru.bootjava.vote.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Converter {
    public static  <T, R> List<R> convert(Collection<T> t, Function<T, R> function) {
        return t.stream()
                .map(function)
                .collect(Collectors.toList());
    }
}
