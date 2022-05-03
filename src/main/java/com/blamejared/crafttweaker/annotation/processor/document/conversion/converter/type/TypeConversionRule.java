package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type;

import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import javax.annotation.Nullable;
import javax.lang.model.type.TypeMirror;

public interface TypeConversionRule {
    
    boolean canConvert(TypeMirror mirror);
    
    @Nullable
    AbstractTypeInfo convert(TypeMirror mirror);
    
}
