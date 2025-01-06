package com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.KnownTypeRegistry;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.NameConversionRule;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistrationWrapper;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Predicate;

public class NativeTypeConversionRule implements NameConversionRule {
    
    private final KnownTypeRegistry knownTypeRegistry;
    
    public NativeTypeConversionRule(KnownTypeRegistry knownTypeRegistry) {
        
        this.knownTypeRegistry = knownTypeRegistry;
    }
    
    @Nullable
    @Override
    public TypeMirror convertZenCodeName(String zenCodeName) {
        
        return knownTypeRegistry.getAllNativeTypes()
                .filter(nameMatches(zenCodeName))
                .map(this::getExpandedType)
                .findFirst()
                .orElse(null);
    }
    
    private TypeMirror getExpandedType(TypeElement typeElement) {
        
        NativeTypeRegistrationWrapper wrap = NativeTypeRegistrationWrapper.wrap(typeElement);
        if(wrap == null) {
            throw new IllegalStateException("Expected '" + typeElement + "' to have an @NativeTypeRegistration but it did not");
        }
        return wrap.valueAsTypeMirror();
    }
    
    
    private Predicate<TypeElement> nameMatches(String zenCodeName) {
        
        return typeElement -> typeElement.getAnnotation(NativeTypeRegistration.class)
                .zenCodeName()
                .equals(zenCodeName);
    }
    
}
