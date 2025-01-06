package com.blamejared.crafttweaker.api.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ZenRegister {
    
    String[] modDeps() default {};
    
    String[] loaders() default {"crafttweaker"};
    
}
