package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocUnknownTag extends JavadocInlineTagBase {
    
    private final String name;
    
    private final String content;
    
    public JavadocUnknownTag(JavaDocContainerElement container, String name, String content) {
        
        super(container);
        this.name = name;
        this.content = content;
    }
    
    @Override
    public String name() {
        
        return name;
    }
    
    @Override
    public String content() {
        
        return content;
    }
    
    @Override
    public String toString() {
        
        return "JavadocUnknownTag{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
