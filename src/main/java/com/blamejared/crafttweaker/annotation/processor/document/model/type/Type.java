package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.mojang.serialization.Codec;

public class Type {
    
    public static final Codec<Type> CODEC = TypeKind.CODEC.dispatch("kind", Type::kind, TypeKind::codec);
    
    /**
     * FQN or generic name
     */
    private final String key; // FQN
    /**
     * Simplest name for this type
     */
    private final String displayName;
    
    private final TypeKind kind;
    private final boolean nullable;
    
    protected Type(String key, String displayName, TypeKind kind, boolean nullable) {
        
        this.key = key;
        this.displayName = displayName;
        this.kind = kind;
        this.nullable = nullable;
    }
    
    public String key() {
        
        return key;
    }
    
    public String displayName() {
        
        return displayName;
    }
    
    public TypeKind kind() {
        
        return kind;
    }
    
    public boolean nullable() {
        
        return nullable;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Type{");
        sb.append("key='").append(key).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", kind=").append(kind);
        sb.append(", nullable=").append(nullable);
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
        
        Type type = (Type) o;
        return nullable == type.nullable && key.equals(type.key) && displayName.equals(type.displayName) && kind == type.kind;
    }
    
    @Override
    public int hashCode() {
        
        int result = key.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + kind.hashCode();
        result = 31 * result + Boolean.hashCode(nullable);
        return result;
    }
    
}
