package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ParameterCountValidationRule implements ExpansionInfoValidationRule {
    
    private final Map<Class<? extends Annotation>, Integer> methodAnnotationToParameterCount = new HashMap<>();
    
    public ParameterCountValidationRule() {
        
        fillParameterMap();
    }
    
    private void fillParameterMap() {
        
        methodAnnotationToParameterCount.put(ZenCodeType.Getter.class, 1);
        methodAnnotationToParameterCount.put(ZenCodeType.Setter.class, 2);
        methodAnnotationToParameterCount.put(ZenCodeType.Caster.class, 1);
    }
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return expectedParameterCountsFor(enclosedElement).count() > 0L;
    }
    
    @Override
    public void validate(Element enclosedElement, ExpansionInfo info) {
        
        final int parameterCount = getParameterCount(enclosedElement);
        this.expectedParameterCountsFor(enclosedElement)
                .filter(countNotEqualTo(parameterCount))
                .forEach(logErrorMessage(enclosedElement));
    }
    
    private IntStream expectedParameterCountsFor(Element enclosedElement) {
        
        return methodAnnotationToParameterCount.keySet()
                .stream()
                .filter(annotationPresentOn(enclosedElement))
                .mapToInt(methodAnnotationToParameterCount::get);
        
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(Element enclosedElement) {
        
        return annotation -> enclosedElement.getAnnotation(annotation) != null;
    }
    
    private IntPredicate countNotEqualTo(int parameterCount) {
        
        return count -> count != parameterCount;
    }
    
    private IntConsumer logErrorMessage(Element enclosedElement) {
        
        return expectedCount -> {
            final String format = "Expected %s parameters for this Expansion Method";
            MessagerUtils.error(enclosedElement, String.format(format, expectedCount));
        };
    }
    
    private int getParameterCount(Element enclosedElement) {
        
        if(enclosedElement.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException("Invalid type annotated? " + enclosedElement);
        }
        
        return ((ExecutableElement) enclosedElement).getParameters().size();
    }
    
}
