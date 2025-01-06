package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JavadocRootElement implements JavaDocContainerElement {
    
    private final List<JavadocElement> elements = new LinkedList<>();
    
    public JavadocRootElement() {
    
    }
    
    public JavadocRootElement(Collection<JavadocElement> elements) {
        
        this.elements.addAll(elements);
    }
    
    
    @Override
    public List<JavadocElement> elements() {
        
        return elements;
    }
    
    @Override
    public String toText() {
        
        return elements.stream().map(JavadocElement::toText).collect(Collectors.joining());
    }
    
    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {
        
        return visitor.visit(this, context);
    }
    
    @Override
    public JavaDocContainerElement container() {
        
        return null;
    }
    
    
}
