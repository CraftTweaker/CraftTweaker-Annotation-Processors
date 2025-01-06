package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocEmTag extends JavadocHtmlTagBase {
    
    public JavadocEmTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "em";
    }
    
    @Override
    public String toString() {
        
        return "JavadocEmTag{}";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
