package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocBrTag extends JavadocHtmlTagBase {
    
    public JavadocBrTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public String name() {
        
        return "br";
    }
    
    @Override
    public String toString() {
        
        return "JavadocBrTag{}";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
    @Override
    public String toText() {
        
        return "<" + name() + "/>";
    }
    
}
