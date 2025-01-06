package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocLinkPlainTag extends JavadocInlineTagBase {
    
    private final String content;
    
    public JavadocLinkPlainTag(JavaDocContainerElement container, String content) {
        
        super(container);
        this.content = content;
    }
    
    
    @Override
    public String name() {
        
        return "linkplain";
    }
    
    @Override
    public String content() {
        
        return content;
    }
    
    @Override
    public String toString() {
        
        return "JavadocLinkPlainTag{" +
                "content='" + content + '\'' +
                '}';
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
}
