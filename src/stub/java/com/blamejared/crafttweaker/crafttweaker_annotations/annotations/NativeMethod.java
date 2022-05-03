package com.blamejared.crafttweaker.crafttweaker_annotations.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Repeatable(NativeMethod.Holder.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeMethod {
    
    String name();
    
    Class<?>[] parameters();
    
    String getterName() default "";
    
    String setterName() default "";
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Holder {
        
        NativeMethod[] value();
        
    }
    
}
