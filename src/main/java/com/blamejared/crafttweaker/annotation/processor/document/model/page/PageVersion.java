package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

public enum PageVersion implements Keyable {
    ONE("1");
    
    public static final Codec<PageVersion> CODEC = Codec.STRING.xmap(Util.enumLookup(PageVersion.values()), PageVersion::key);
    
    private final String key;
    
    PageVersion(String key) {
        
        this.key = key;
    }
    
    @Override
    public String key() {
        
        return key;
    }
}
