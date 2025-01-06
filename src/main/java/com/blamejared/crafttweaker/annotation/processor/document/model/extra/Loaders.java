package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;

import java.util.Arrays;
import java.util.List;

public class Loaders {
    
    public static final Codec<Loaders> CODEC = Codec.STRING.listOf().xmap(Loaders::new, Loaders::loaders);
    private final List<String> loaders;
    
    
    public Loaders(String[] loaders) {
        
        this(Arrays.asList(loaders));
    }
    
    public Loaders(List<String> loaders) {
        
        this.loaders = loaders;
    }
    
    public List<String> loaders() {
        
        return loaders;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Loaders{");
        sb.append("loaders=").append(loaders);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Loaders loaders1 = (Loaders) o;
        return loaders.equals(loaders1.loaders);
    }
    
    @Override
    public int hashCode() {
        
        return loaders.hashCode();
    }
    
}
