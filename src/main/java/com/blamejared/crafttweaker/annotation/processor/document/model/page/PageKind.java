package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public enum PageKind implements Keyable {
    TYPE("type", () -> TypePage.CODEC),
    EVENT("event", () -> EventPage.CODEC),
    ENUM("enum", () -> EnumPage.CODEC),
    MARKDOWN("markdown", () -> MarkdownPage.CODEC),
    RENDERED("rendered", () -> RenderedPage.CODEC),
    EXPANSION("expansion", () -> ExpansionPage.CODEC);
    
    public static final Codec<PageKind> CODEC = Codec.STRING.xmap(Util.enumLookup(PageKind.values()), PageKind::key);
    private final String key;
    private final Supplier<Codec<? extends Page>> codec;
    
    PageKind(String key, Supplier<Codec<? extends Page>> codec) {
        
        this.key = key;
        this.codec = codec;
    }
    
    public String key() {
        
        return key;
    }
    
    public Codec<? extends Page> codec() {
        
        return codec.get();
    }
    
    @Override
    public String toString() {
        
        return "PageKind{" +
                "key='" + key + '\'' +
                '}';
    }
}
