package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public interface JavadocElement {
    
    String toText();
    
    <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context);
    
    JavaDocContainerElement container();
    
}
