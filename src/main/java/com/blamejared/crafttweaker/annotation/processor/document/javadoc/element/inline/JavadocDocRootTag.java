package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocDocRootTag extends JavadocInlineTagBase {
    
    public JavadocDocRootTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "docRoot";
    }
    
    @Override
    public String content() {
        
        return "";
    }
    
    @Override
    public String toString() {
        
        return "JavadocDocRootTag{}";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
    
