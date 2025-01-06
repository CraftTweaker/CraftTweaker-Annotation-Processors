package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocSinceTag extends JavadocBlockTagBase {
    
    public JavadocSinceTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "since";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
