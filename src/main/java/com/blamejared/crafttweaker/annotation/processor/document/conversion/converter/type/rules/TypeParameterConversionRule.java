package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.TypeParameterTypeInfo;

import javax.annotation.Nullable;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class TypeParameterConversionRule implements TypeConversionRule {
    
    @Override
    public boolean canConvert(TypeMirror mirror) {
        
        return mirror.getKind() == TypeKind.TYPEVAR;
    }
    
    @Nullable
    @Override
    public AbstractTypeInfo convert(TypeMirror mirror) {
        
        return new TypeParameterTypeInfo(mirror.toString());
    }
    
}
