package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

import java.util.Objects;

public class JavadocSnippet implements JavadocElement {
    
    private final JavaDocContainerElement container;
    private final String text;
    
    public JavadocSnippet(JavaDocContainerElement container, String text) {
        
        this.container = container;
        this.text = text;
    }
    
    public String text() {
        
        return text;
    }
    
    @Override
    public String toString() {
        
        return "JavadocSnippet{" +
                "text='" + text + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        JavadocSnippet that = (JavadocSnippet) o;
        return Objects.equals(text, that.text);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(text);
    }
    
    @Override
    public String toText() {
        
        return text();
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
