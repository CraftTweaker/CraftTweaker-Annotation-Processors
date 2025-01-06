package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocHtmlTag;

import java.util.LinkedList;
import java.util.List;

public abstract class JavadocHtmlTagBase implements JavadocHtmlTag {
    
    private final List<JavadocElement> elements = new LinkedList<>();
    private final JavaDocContainerElement container;
    
    public JavadocHtmlTagBase(JavaDocContainerElement container) {
        
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
