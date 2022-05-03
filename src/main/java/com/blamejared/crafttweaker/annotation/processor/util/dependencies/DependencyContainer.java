package com.blamejared.crafttweaker.annotation.processor.util.dependencies;

public interface DependencyContainer {
    
    <Type> Type getInstanceOfClass(Class<Type> cls);
    
    <Type> void addInstance(Type instance);
    
    <Type, Instance extends Type> void addInstanceAs(Instance instance, Class<Type> as);
    
}
