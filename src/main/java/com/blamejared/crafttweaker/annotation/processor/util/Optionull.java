package com.blamejared.crafttweaker.annotation.processor.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class Optionull {
    
    public static <T> void ifPresent(@Nullable T thing, Consumer<T> func) {
        
        if(thing != null) {
            func.accept(thing);
        }
    }
    
    public static <T, U> void ifPresent(@Nullable T thing, Function<T, U> mapper, Consumer<U> func) {
        
        if(thing != null) {
            func.accept(mapper.apply(thing));
        }
    }
    
}
