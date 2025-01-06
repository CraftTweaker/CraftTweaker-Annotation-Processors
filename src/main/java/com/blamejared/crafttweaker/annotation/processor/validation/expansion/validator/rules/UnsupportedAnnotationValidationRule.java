package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import io.toolisticon.aptk.tools.AnnotationUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UnsupportedAnnotationValidationRule implements ExpansionInfoValidationRule {
    
    private final Set<Class<? extends Annotation>> unsupportedTypes = new HashSet<>();
    
    public UnsupportedAnnotationValidationRule() {
        
        fillUnsupportedAnnotations();
    }
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return true;
    }
    
    @Override
    public void validate(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        getInvalidAnnotationMirrors(enclosedElement).forEach(writeMessage(enclosedElement));
    }
    
    private Stream<AnnotationMirror> getInvalidAnnotationMirrors(Element enclosedElement) {
        
        return unsupportedTypes.stream()
                .filter(annotationPresentOn(enclosedElement))
                .map(getAnnotationMirrorAt(enclosedElement));
    }
    
    private Function<Class<? extends Annotation>, AnnotationMirror> getAnnotationMirrorAt(Element enclosedElement) {
        
        return annotationClass -> AnnotationUtils.getAnnotationMirror(enclosedElement, annotationClass);
    }
    
    private Consumer<AnnotationMirror> writeMessage(Element enclosedElement) {
        
        return annotationMirror -> {
            final String message = String.format("Annotation %s not allowed in expansions", annotationMirror);
            MessagerUtils.error(enclosedElement, message);
        };
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(Element enclosedElement) {
        
        return annotationClass -> enclosedElement.getAnnotation(annotationClass) != null;
    }
    
    
    private void fillUnsupportedAnnotations() {
        
        unsupportedTypes.add(ZenCodeType.Constructor.class);
        unsupportedTypes.add(ZenCodeType.Field.class);
    }
    
}
