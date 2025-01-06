package com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules;

import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;


public class OptionalsNeedToGoLastRule implements ParameterValidationRule {
    
    private final Set<Class<? extends Annotation>> optionalAnnotations = new HashSet<>();
    {
        optionalAnnotations.add(ZenCodeType.Optional.class);
        optionalAnnotations.add(ZenCodeType.OptionalInt.class);
        optionalAnnotations.add(ZenCodeType.OptionalLong.class);
        optionalAnnotations.add(ZenCodeType.OptionalFloat.class);
        optionalAnnotations.add(ZenCodeType.OptionalString.class);
        optionalAnnotations.add(ZenCodeType.OptionalDouble.class);
        optionalAnnotations.add(ZenCodeType.OptionalBoolean.class);
        optionalAnnotations.add(ZenCodeType.OptionalChar.class);
    }
    
    @Override
    public boolean canValidate(VariableElement parameter) {
        
        return isOptional(parameter);
    }
    
    private boolean isOptional(VariableElement parameter) {
        
        return optionalAnnotations.stream().anyMatch(annotationPresentOn(parameter));
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(VariableElement parameter) {
        
        return annotationClass -> parameter.getAnnotation(annotationClass) != null;
    }
    
    @Override
    public void validate(VariableElement parameter) {
        
        if(hasNonOptionalParameterFollowing(parameter)) {
            MessagerUtils.error(parameter, "Optional parameters must go last");
        }
    }
    
    private boolean hasNonOptionalParameterFollowing(VariableElement parameter) {
        
        final ExecutableElement method = (ExecutableElement) parameter.getEnclosingElement();
        final List<? extends VariableElement> parameters = method.getParameters();
        final int parameterIndex = parameters.indexOf(parameter);
        
        return parameters.stream()
                .skip(parameterIndex)
                .anyMatch(followingParameter -> !isOptional(followingParameter));
        
    }
    
}
