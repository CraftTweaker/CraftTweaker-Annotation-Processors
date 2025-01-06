package net.minecraftforge.eventbus.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class Event {
    
    @Retention(value = RUNTIME)
    @Target(value = TYPE)
    public @interface HasResult{}
    
}
