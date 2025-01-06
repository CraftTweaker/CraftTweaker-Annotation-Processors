package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;

public class EventParent {
    
    public static final EventParent INSTANCE = new EventParent();
    
    public static final Codec<EventParent> CODEC = Codec.unit(EventParent::new);
    
    private EventParent() {
        
    }
    
}
