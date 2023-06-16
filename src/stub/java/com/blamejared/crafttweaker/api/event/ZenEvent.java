package com.blamejared.crafttweaker.api.event;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZenEvent {
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface BusCarrier {}
    
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Bus {
        
        Class<?> value() default Auto.class;
        
        @Target({})
        @Retention(RetentionPolicy.RUNTIME)
        @interface Auto {}
        
    }
    
}
