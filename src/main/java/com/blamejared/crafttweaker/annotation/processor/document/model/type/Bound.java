package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Optional;

public enum Bound implements Keyable {
    SPECIFIC("specific"),
    UPPER("upper"),
    LOWER("lower");
    
    public static final Codec<Bound> CODEC = Codec.STRING.xmap(Util.enumLookup(Bound.values()), Bound::key);
    
    private final String key;
    
    Bound(String key) {
        
        this.key = key;
    }
    
    public Optional<TypeMirror> forVariable(TypeVariable variable) {
        
        return Optional.ofNullable(switch(this) {
            case LOWER -> variable.getLowerBound();
            case SPECIFIC, UPPER -> variable.getUpperBound();
        });
    }
    
    public static Bound from(TypeVariable variable) {
        
        boolean isSpecified = TypeUtils.TypeComparison.isTypeEqual(variable.getUpperBound(), Object.class);
        if(isSpecified) {
            return SPECIFIC;
        }
        boolean isLower = !TypeUtils.getTypes().getNullType().equals(variable.getLowerBound());
        if(isLower) {
            return LOWER;
        } else {
            if(TypeUtils.getTypes().getNullType().equals(variable.getUpperBound())) {
                throw new IllegalStateException("Upper bounds cannot be null!");
            }
            return UPPER;
        }
    }
    
    @Override
    public String key() {
        
        return key;
    }
}
