package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocLinkTag extends JavadocInlineTagBase {
    
    private final String content;
    
    public JavadocLinkTag(JavaDocContainerElement container, String content) {
        
        super(container);
        this.content = content;
    }
    
    
    @Override
    public String name() {
        
        return "link";
    }
    
    @Override
    public String content() {
        
        return content;
    }
    
    @Override
    public String toString() {
        
        return "JavadocLinkTag{" +
                "content='" + content + '\'' +
                '}';
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
