package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;

import java.util.Objects;

public class BracketEnum {
    
    public static final Codec<BracketEnum> CODEC = Codec.STRING.xmap(BracketEnum::new, BracketEnum::value);
    
    private final String value;
    
    public BracketEnum(String value) {
        
        this.value = value;
    }
    
    public String value() {
        
        return value;
    }
    
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        BracketEnum that = (BracketEnum) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        
        return "BracketEnum{" +
                "value='" + value + '\'' +
                '}';
    }
    
}
