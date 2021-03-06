package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.PrimitiveTypeInfo;

import javax.annotation.Nullable;
import javax.lang.model.type.TypeMirror;
import java.util.Locale;

public class PrimitiveConversionRule implements TypeConversionRule {
    
    @Override
    public boolean canConvert(TypeMirror mirror) {
        
        return mirror.getKind().isPrimitive();
    }
    
    @Nullable
    @Override
    public AbstractTypeInfo convert(TypeMirror mirror) {
        
        return new PrimitiveTypeInfo(mirror.getKind().name().toLowerCase(Locale.ROOT));
    }
    
}
