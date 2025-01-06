package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class GenericType extends Type {
    
    public static final Codec<GenericType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("key").forGetter(Type::key),
            Codec.STRING.fieldOf("display_name").forGetter(Type::displayName),
            Codec.BOOL.fieldOf("nullable").forGetter(Type::nullable),
            Type.CODEC.fieldOf("type").forGetter(GenericType::genericType),
            Bound.CODEC.fieldOf("bound").forGetter(GenericType::bound)
    ).apply(instance, GenericType::new));
    
    private final Type type;
    private final Bound bound;
    
    public GenericType(String key, boolean nullable, Type type, Bound bound) {
        
        this(key, key, nullable, type, bound);
    }
    
    public GenericType(String key, String displayName, boolean nullable, Type type, Bound bound) {
        
        super(key, displayName, TypeKind.GENERIC, nullable);
        this.type = type;
        this.bound = bound;
    }
    
    public Type genericType() {
        
        return type;
    }
    
    public Bound bound() {
        
        return bound;
    }
    
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("GenericType{");
        sb.append("genericType=").append(type);
        sb.append(", bound=").append(bound);
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
        
        GenericType that = (GenericType) o;
        return type.equals(that.type) && bound == that.bound;
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + bound.hashCode();
        return result;
    }
    
}
