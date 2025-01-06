package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.HTMLAwareJavadocDescription;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocParamTag extends JavadocBlockTagBase {
    
    private String paramName;
    
    public JavadocParamTag(JavaDocContainerElement container) {
        
        super(container);
    }
    
    @Override
    public void parseContent(String content) {
        
        String[] parts = content.split(" ", 2);
        this.paramName = parts[0];
        if(parts.length > 1) {
            this.addElement(HTMLAwareJavadocDescription.parse(parts[1], false));
        }
    }
    
    public String paramName() {
        
        return paramName;
    }
    
    @Override
    public String name() {
        
        return "version";
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
