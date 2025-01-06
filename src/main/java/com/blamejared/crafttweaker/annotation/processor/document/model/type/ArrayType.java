package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ArrayType extends Type {
    
    public static final Codec<ArrayType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Type::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Type::displayName),
                    Codec.BOOL.fieldOf("nullable").forGetter(Type::nullable),
                    Type.CODEC.fieldOf("component_type").forGetter(ArrayType::componentType))
            .apply(instance, ArrayType::new));
    
    private final Type componentType;
    
    public ArrayType(String key, String displayName, boolean nullable, Type componentType) {
        
        super(key, displayName, TypeKind.ARRAY, nullable);
        this.componentType = componentType;
    }
    
    public Type componentType() {
        
        return componentType;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("ArrayType{");
        sb.append("componentType=").append(componentType);
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
        if(!super.equals(o)) {
            return false;
        }
        
        ArrayType arrayType = (ArrayType) o;
        return componentType.equals(arrayType.componentType);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + componentType.hashCode();
        return result;
    }
    
}
