package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocNewLine implements JavadocElement {
    
    private final JavaDocContainerElement container;
    
    public JavadocNewLine(JavaDocContainerElement container) {
        
        this.container = container;
    }
    
    @Override
    public String toText() {
        
        return "\n";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
    @Override
    public JavaDocContainerElement container() {
        
        return container;
    }
    
}
