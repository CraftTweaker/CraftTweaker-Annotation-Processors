package com.blamejared.crafttweaker.annotation.processor.document.file;

import com.blamejared.crafttweaker.annotation.processor.document.page.page.DocumentationPage;
import com.google.gson.annotations.SerializedName;

public class Navigation {
    
    @SerializedName("nav")
    private final TableOfContent tableOfContent = new TableOfContent();
    
    public void add(DocumentationPage page) {
        
        tableOfContent.add(page);
    }
    
}
