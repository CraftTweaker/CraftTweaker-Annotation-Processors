package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.ExistingTypeInfo;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

public class JavaFunctionConversionRule implements TypeConversionRule {
    
    private final Types typeUtils;
    
    public JavaFunctionConversionRule(Types typeUtils) {
        
        this.typeUtils = typeUtils;
    }
    
    @Override
    public boolean canConvert(TypeMirror mirror) {
        
        return mirror.toString().startsWith("java.util.function");
    }
    
    @Nullable
    @Override
    public AbstractTypeInfo convert(TypeMirror mirror) {
        
        final Element element = typeUtils.asElement(mirror);
        return new ExistingTypeInfo(element.getSimpleName().toString());
    }
    
}
