package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member;

import com.blamejared.crafttweaker.annotation.processor.document.page.info.DocumentationPageInfo;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public abstract class AbstractEnclosedElementConverter<T> {
    
    
    protected AbstractEnclosedElementConverter() {
    
    }
    
    protected boolean isAnnotationPresentOn(Class<? extends Annotation> annotationClass, Element element) {
        
        return element.getAnnotation(annotationClass) != null;
    }
    
    public abstract boolean canConvert(Element enclosedElement);
    
    public abstract void convertAndAddTo(Element enclosedElement, T result, DocumentationPageInfo pageInfo);
    
}
