package com.blamejared.crafttweaker.annotation.processor.document.meta;

public class MetaData {
    
    private String shortDescription;
    
    public MetaData() {
    
    }
    
    public MetaData(String shortDescription) {
        
        this.shortDescription = shortDescription;
    }
    
    public String getShortDescription() {
        
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        
        if(shortDescription == null || shortDescription.isEmpty()) {
            return;
        }
        
        this.shortDescription = shortDescription;
    }
    
    public static MetaData empty() {
        
        return new MetaData();
    }
    
}
