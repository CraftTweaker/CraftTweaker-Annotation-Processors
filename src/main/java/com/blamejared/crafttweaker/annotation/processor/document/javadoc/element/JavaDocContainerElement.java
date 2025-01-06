package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import java.util.List;

public interface JavaDocContainerElement extends JavadocElement {
    
    List<JavadocElement> elements();
    
    default <T extends JavadocElement> T addElement(T element) {
        
        List<JavadocElement> elements = elements();
        if(!elements.isEmpty()) {
            JavadocElement last = elements.get(elements.size() - 1);
            if(last instanceof JavadocSnippet prev && element instanceof JavadocSnippet next) {
                JavadocSnippet snippet = new JavadocSnippet(this, prev.text() + next.text());
                elements.set(elements.size() - 1, snippet);
                return (T) snippet;
            } else if(last instanceof JavadocNewLine prev && element instanceof JavadocNewLine next) {
                // Don't allow multiple newlines
                return (T) prev;
            }
        }
        elements.add(element);
        return element;
    }
    
}
