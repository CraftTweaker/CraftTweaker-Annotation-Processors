package com.blamejared.crafttweaker.annotation.processor.document.page.type;

public class PrimitiveTypeInfo extends AbstractTypeInfo {
    
    private final String name;
    
    public PrimitiveTypeInfo(String name) {
        
        this.name = name;
    }
    
    @Override
    public String getDisplayName() {
        
        return name;
    }
    
    @Override
    public String getClickableMarkdown() {
        
        return getDisplayName();
    }
    
}
