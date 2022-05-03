package com.blamejared.crafttweaker.annotation.processor.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {
    
    public static <T> T make(Supplier<T> supplier) {
        
        return supplier.get();
    }
    
    public static <T> T make(T instance, Consumer<T> consumer) {
        
        consumer.accept(instance);
        return instance;
    }
    
}
