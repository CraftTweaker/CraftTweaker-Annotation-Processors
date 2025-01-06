package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.datafixers.util.Pair;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ParameterCountValidationRule implements VirtualTypeValidationRule {
    
    private final Map<Class<? extends Annotation>, Integer> methodAnnotationToParameterCount = Util.make(new HashMap<>(), map -> {
        map.put(ZenCodeType.Getter.class, 0);
        map.put(ZenCodeType.Setter.class, 1);
        map.put(ZenCodeType.Caster.class, 0);
    });
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return expectedParameterCountsFor(enclosedElement).findAny().isPresent();
    }
    
    @Override
    public void validate(Element enclosedElement) {
        
        final int parameterCount = getParameterCount(enclosedElement);
        this.expectedParameterCountsFor(enclosedElement)
                .filter(classIntegerPair -> classIntegerPair.getSecond() != parameterCount)
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
            final String msg = format.formatted(annotationData.getSecond(), annotationData.getSecond() != 1 ? "s" : "", annotationData.getFirst()
                    .getSimpleName(), givenParams);
            MessagerUtils.error(enclosedElement, msg);
        };
    }
    
    private int getParameterCount(Element enclosedElement) {
        
        if(!ElementUtils.CheckKindOfElement.isMethod(enclosedElement)) {
            throw new IllegalArgumentException("Invalid type annotated? " + enclosedElement);
        }
        
        return ((ExecutableElement) enclosedElement).getParameters().size();
    }
    
}
