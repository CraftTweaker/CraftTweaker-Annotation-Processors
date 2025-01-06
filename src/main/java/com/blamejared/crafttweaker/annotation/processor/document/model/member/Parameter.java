package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Parameter {
    
    public static final Codec<Parameter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Parameter::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Parameter::displayName),
                    Type.CODEC.fieldOf("type").forGetter(Parameter::type),
                    Codec.STRING.optionalFieldOf("default_value").forGetter(Parameter::defaultValue)
            )
            .apply(instance, Parameter::new));
    
    private final String key;
    private final String displayName;
    private final Type type;
    private final Optional<String> defaultValue;
    
    public Parameter(String key, String displayName, Type type, Optional<String> defaultValue) {
        
        this.key = key;
        this.displayName = displayName;
        this.type = type;
        this.defaultValue = defaultValue;
    }
    
    public String key() {
        
        return key;
    }
    
    public String displayName() {
        
        return displayName;
    }
    
    public Type type() {
        
        return type;
    }
    
    public Optional<String> defaultValue() {
        
        return defaultValue;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        
        Parameter parameter = (Parameter) o;
        return key.equals(parameter.key) && displayName.equals(parameter.displayName) && type.equals(parameter.type) && defaultValue.equals(parameter.defaultValue);
    }
    
    @Override
    public int hashCode() {
        
        int result = key.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + defaultValue.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Parameter{");
        sb.append("key='").append(key).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", type=").append(type);
        sb.append(", defaultValue=").append(defaultValue);
        sb.append('}');
        return sb.toString();
    }
    
}
