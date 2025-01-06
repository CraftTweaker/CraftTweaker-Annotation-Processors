package com.blamejared.crafttweaker.annotation.processor.document.model;

import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class DocJson {
    
    public static final Encoder<DocJson> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DocFolder.NAV_CODEC.fieldOf("nav").forGetter(DocJson::nav)
    ).apply(instance, DocJson::new));
    
    private final DocFolder nav;
    
    public DocJson(DocFolder nav) {
        
        this.nav = nav;
    }
    
    public DocFolder nav() {
        
        return nav;
    }
    
}
