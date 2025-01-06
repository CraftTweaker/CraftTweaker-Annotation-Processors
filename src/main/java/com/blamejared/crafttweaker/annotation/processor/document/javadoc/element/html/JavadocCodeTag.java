package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocCodeTag extends JavadocHtmlTagBase {
    
    public JavadocCodeTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "code";
    }
    
    @Override
    public String toString() {
        
        return "JavadocCodeTag{}";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
