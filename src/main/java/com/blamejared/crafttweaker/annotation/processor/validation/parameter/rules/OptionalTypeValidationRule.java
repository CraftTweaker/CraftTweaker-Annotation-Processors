package com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class OptionalTypeValidationRule implements ParameterValidationRule {
    
    private final Map<TypeMirror, Class<? extends Annotation>> optionalAnnotations = new HashMap<>();
    
    public OptionalTypeValidationRule() {
        
        fillAnnotationMap();
    }
    
    private void fillAnnotationMap() {
        
        putAnnotation(TypeKind.INT, ZenCodeType.OptionalInt.class);
        putAnnotation(TypeKind.LONG, ZenCodeType.OptionalLong.class);
        putAnnotation(TypeKind.FLOAT, ZenCodeType.OptionalFloat.class);
        putAnnotation(TypeKind.DOUBLE, ZenCodeType.OptionalDouble.class);
        putAnnotation(TypeKind.BOOLEAN, ZenCodeType.OptionalBoolean.class);
        putAnnotation(TypeKind.CHAR, ZenCodeType.OptionalChar.class);
        
        putAnnotation(Object.class, ZenCodeType.Optional.class);
        putAnnotation(String.class, ZenCodeType.OptionalString.class);
    }
    
    private void putAnnotation(Class<?> cls, Class<? extends Annotation> annotationClass) {
        
        final TypeElement typeElement = TypeUtils.TypeRetrieval.getTypeElement(cls);
        optionalAnnotations.put(typeElement.asType(), annotationClass);
    }
    
    private void putAnnotation(TypeKind typeKind, Class<? extends Annotation> annotationClass) {
        
        final PrimitiveType primitiveType = TypeUtils.getTypes().getPrimitiveType(typeKind);
        optionalAnnotations.put(primitiveType, annotationClass);
    }
    
    @Override
    public boolean canValidate(VariableElement parameter) {
        
        return isOptional(parameter);
    }
    
    private boolean isOptional(VariableElement parameter) {
        
        return optionalAnnotations.values().stream().anyMatch(annotationPresentOn(parameter));
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(VariableElement parameter) {
        
        return annotationClass -> parameter.getAnnotation(annotationClass) != null;
    }
    
    @Override
    public void validate(VariableElement parameter) {
        
        if(shouldParameterUseSpecialAnnotation(parameter)) {
            checkIfParameterUsesProperAnnotation(parameter);
        }
    }
    
    private boolean shouldParameterUseSpecialAnnotation(VariableElement parameter) {
        
        return optionalAnnotations.containsKey(parameter.asType());
    }
    
    private void checkIfParameterUsesProperAnnotation(VariableElement parameter) {
        
        final Class<? extends Annotation> annotationClass = optionalAnnotations.get(parameter.asType());
        if(parameter.getAnnotation(annotationClass) == null) {
            final String message = "Optional type should use " + annotationClass.getSimpleName() + " annotation";
            MessagerUtils.error(parameter, message);
        }
    }
    
}
