package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public enum TypeKind implements Keyable {
    ARRAY("array", () -> ArrayType.CODEC),
    BASIC("basic", () -> BasicType.CODEC),
    GENERIC("generic", () -> GenericType.CODEC),
    JAVA("java", () -> JavaType.CODEC);
    
    public static final Codec<TypeKind> CODEC = Codec.STRING.xmap(Util.enumLookup(TypeKind.values()), TypeKind::key);
    
    private final String key;
    private final Supplier<Codec<? extends Type>> codec;
    
    TypeKind(String key, Supplier<Codec<? extends Type>> codec) {
        
        this.key = key;
        this.codec = codec;
    }
    
    @Override
    public String key() {
        
        return key;
    }
    
    public Codec<? extends Type> codec() {
        
        return codec.get();
    }
}
