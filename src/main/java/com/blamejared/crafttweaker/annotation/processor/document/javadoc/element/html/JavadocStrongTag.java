package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocStrongTag extends JavadocHtmlTagBase {
    
    public JavadocStrongTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "strong";
    }
    
    @Override
    public String toString() {
        
        return "JavadocStrongTag{}";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
