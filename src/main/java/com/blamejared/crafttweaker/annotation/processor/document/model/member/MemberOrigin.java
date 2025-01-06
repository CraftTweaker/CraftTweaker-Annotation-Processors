package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

public enum MemberOrigin implements Keyable {
    DECLARED("declared"),
    NATIVE("native"),
    INHERITED("inherited");
    
    public static final Codec<MemberOrigin> CODEC = Codec.STRING.xmap(Util.enumLookup(MemberOrigin.values()), MemberOrigin::key);
    
    private final String key;
    
    MemberOrigin(String key) {
        
        this.key = key;
    }
    
    public boolean isNative() {
        
        return this == NATIVE;
    }
    
    @Override
    public String key() {
        
        return key;
    }
}
