package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Map;

public class Examples {
    
    public static final Codec<Examples> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf())
            .xmap(Examples::new, Examples::examples);
    
    private final Map<String, List<String>> examples;
    
    public Examples(Map<String, List<String>> examples) {
        
        this.examples = examples;
    }
    
    public Map<String, List<String>> examples() {
        
        return examples;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Examples examples1 = (Examples) o;
        return examples.equals(examples1.examples);
    }
    
    @Override
    public int hashCode() {
        
        return examples.hashCode();
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Examples{");
        sb.append("examples=").append(examples);
        sb.append('}');
        return sb.toString();
    }
    
}
