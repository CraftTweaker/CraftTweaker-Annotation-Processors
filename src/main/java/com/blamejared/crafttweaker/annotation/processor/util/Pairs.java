package com.blamejared.crafttweaker.annotation.processor.util;

import com.mojang.datafixers.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Pairs {
    
    public static <F, S, U, V> Function<Pair<F, S>, Pair<U, V>> map(Function<F, U> firstFunc, Function<S, V> secondFunc) {
        
        return fsPair -> map(fsPair, firstFunc, secondFunc);
    }
    
    public static <F, S, U, V> Pair<U, V> map(Pair<F, S> pair, Function<F, U> firstFunc, Function<S, V> secondFunc) {
        
        return Pair.of(firstFunc.apply(pair.getFirst()), secondFunc.apply(pair.getSecond()));
    }
    
    public static <F, S> Predicate<Pair<F, S>> filterFirst(Predicate<F> predicate) {
        
        return fsPair -> predicate.test(fsPair.getFirst());
    }
    
    public static <F, S> Predicate<Pair<F, S>> filterSecond(Predicate<S> predicate) {
        
        return fsPair -> predicate.test(fsPair.getSecond());
    }
    
    public static <F, S> Collector<Pair<F, S>, ?, Map<F, S>> collect() {
        
        return Collectors.toMap(Pair::getFirst, Pair::getSecond, (s, s2) -> s, LinkedHashMap::new);
    }
    
    public static <F, S> Predicate<Pair<F, S>> filter(BiPredicate<F, S> pred) {
        
        return pair -> pred.test(pair.getFirst(), pair.getSecond());
    }
    
    public static <F, S> Consumer<Pair<F, S>> forEach(BiConsumer<F, S> cons) {
        
        return fsPair -> cons.accept(fsPair.getFirst(), fsPair.getSecond());
    }
    
}
