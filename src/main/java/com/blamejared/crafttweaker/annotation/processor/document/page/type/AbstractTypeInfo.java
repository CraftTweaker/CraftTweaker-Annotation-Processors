package com.blamejared.crafttweaker.annotation.processor.document.page.type;

import javax.annotation.Nonnull;

public abstract class AbstractTypeInfo implements Comparable<AbstractTypeInfo> {
    
    public abstract String getDisplayName();
    
    public abstract String getClickableMarkdown();
    
    public String getSimpleMarkdown() {
        
        return getDisplayName();
    }
    
    public int compareTo(@Nonnull AbstractTypeInfo other) {
        
        return this.getDisplayName().compareToIgnoreCase(other.getDisplayName());
    }
    
    @Override
    public String toString() {
        
        return getDisplayName();
    }
    
}
