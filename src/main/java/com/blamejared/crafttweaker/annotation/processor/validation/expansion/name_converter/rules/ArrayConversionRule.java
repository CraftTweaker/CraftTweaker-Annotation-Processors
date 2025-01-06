package com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.NameConversionRule;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.NameConverter;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.annotation.Nullable;
import javax.lang.model.type.TypeMirror;

public class ArrayConversionRule implements NameConversionRule {
    
    private final NameConverter nameConverter;
    
    public ArrayConversionRule(NameConverter nameConverter) {
        
        this.nameConverter = nameConverter;
    }
    
    @Nullable
    @Override
    public TypeMirror convertZenCodeName(String zenCodeName) {
        
        if(isArrayType(zenCodeName)) {
            return getArrayType(zenCodeName);
        }
        return null;
    }
    
    private boolean isArrayType(String zenCodeName) {
        
        return zenCodeName.endsWith("[]");
    }
    
    private TypeMirror getArrayType(String zenCodeName) {
        
        final TypeMirror componentType = getComponentType(zenCodeName);
        return TypeUtils.getTypes().getArrayType(componentType);
    }
    
    private TypeMirror getComponentType(String zenCodeName) {
        
        final String componentString = zenCodeName.substring(0, zenCodeName.length() - 2);
        return nameConverter.getTypeMirrorByZenCodeName(componentString);
    }
    
    
}
