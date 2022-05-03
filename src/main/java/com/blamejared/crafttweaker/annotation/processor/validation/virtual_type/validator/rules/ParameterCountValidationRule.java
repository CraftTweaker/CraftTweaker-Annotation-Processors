package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.Pair;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import io.toolisticon.aptk.tools.AnnotationValueUtils;
import io.toolisticon.aptk.tools.ElementUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ParameterCountValidationRule implements VirtualTypeValidationRule {
    
    private final Messager messager;
    private final Map<Class<? extends Annotation>, Integer> methodAnnotationToParameterCount = Util.make(new HashMap<>(), map -> {
        map.put(ZenCodeType.Getter.class, 0);
        map.put(ZenCodeType.Setter.class, 1);
        map.put(ZenCodeType.Caster.class, 0);
    });
    
    public ParameterCountValidationRule(Messager messager) {
        
        this.messager = messager;
    }
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return expectedParameterCountsFor(enclosedElement).findAny().isPresent();
    }
    
    @Override
    public void validate(Element enclosedElement) {
        
        final int parameterCount = getParameterCount(enclosedElement);
        this.expectedParameterCountsFor(enclosedElement)
                .filter(classIntegerPair -> classIntegerPair.second() != parameterCount)
                .forEach(logErrorMessage(enclosedElement, parameterCount));
    }
    
    private Stream<Pair<Class<? extends Annotation>, Integer>> expectedParameterCountsFor(Element enclosedElement) {
        
        return methodAnnotationToParameterCount.keySet()
                .stream()
                .filter(annotationPresentOn(enclosedElement))
                .map(aClass -> new Pair<>(aClass, methodAnnotationToParameterCount.get(aClass)));
        
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(Element enclosedElement) {
        
        return annotation -> enclosedElement.getAnnotation(annotation) != null;
    }
    
    private Consumer<Pair<Class<? extends Annotation>, Integer>> logErrorMessage(Element enclosedElement, int givenParams) {
        
        return annotationData -> {
            final String format = "Expected '%s' parameter%s for %s method but received '%s'";
            messager.printMessage(Diagnostic.Kind.ERROR, format.formatted(annotationData.second(), annotationData.second() != 1 ? "s" : "", annotationData.first()
                    .getSimpleName(), givenParams), enclosedElement);
        };
    }
    
    private int getParameterCount(Element enclosedElement) {
        
        if(!ElementUtils.CheckKindOfElement.isMethod(enclosedElement)) {
            throw new IllegalArgumentException("Invalid type annotated? " + enclosedElement);
        }
        
        return ((ExecutableElement) enclosedElement).getParameters().size();
    }
    
}
