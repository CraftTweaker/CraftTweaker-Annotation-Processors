package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class BasicType extends Type {
    
    public static final Codec<BasicType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Type::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Type::displayName),
                    Codec.BOOL.fieldOf("nullable").forGetter(Type::nullable))
            .apply(instance, BasicType::new));
    
    public BasicType(String key, String displayName, boolean nullable) {
        
        super(key, displayName, TypeKind.BASIC, nullable);
    }
    
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("BasicType{");
        sb.append('}');
        return sb.toString();
    }
    
}
