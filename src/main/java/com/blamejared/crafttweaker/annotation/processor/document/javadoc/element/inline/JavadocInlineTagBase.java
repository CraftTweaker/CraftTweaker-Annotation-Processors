package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocInlineTag;

public abstract class JavadocInlineTagBase implements JavadocInlineTag {
    
    private final JavaDocContainerElement container;
    
    public JavadocInlineTagBase(JavaDocContainerElement container) {
        
        this.container = container;
    }
    
    @Override
    public JavaDocContainerElement container() {
        
        return container;
    }
    
}
