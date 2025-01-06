package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocBlockTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocElement;

import java.util.LinkedList;
import java.util.List;

public abstract class JavadocBlockTagBase implements JavadocBlockTag {
    
    private final List<JavadocElement> elements = new LinkedList<>();
    private final JavaDocContainerElement container;
    
    public JavadocBlockTagBase(JavaDocContainerElement container) {
        
        this.container = container;
    }
    
    @Override
    public List<JavadocElement> elements() {
        
        return elements;
    }
    
    @Override
    public JavaDocContainerElement container() {
        
        return container;
    }
    
}
