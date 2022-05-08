package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConverter;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.NullableTypeInfo;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class NullableAnnotatedParameterConversionRule implements TypeConversionRule {
    
    private final TypeConverter converter;
    
    public NullableAnnotatedParameterConversionRule(final TypeConverter converter) {
        
        this.converter = converter;
    }
    
    @Override
    public boolean canConvert(final TypeMirror mirror) {
        
        return mirror.getAnnotationMirrors().stream().anyMatch(this::isNullableAnnotation);
    }
    
    private boolean isNullableAnnotation(final AnnotationMirror mirror) {
        
        final DeclaredType type = mirror.getAnnotationType();
        final Element element = type.asElement();
        final Element enclosingElement = element.getEnclosingElement();
        final Name elementName = element.getSimpleName();
        final Name enclosingElementName = enclosingElement.getSimpleName();
        return elementName.contentEquals("Nullable") && enclosingElementName.contentEquals("ZenCodeType");
    }
    
    @Nullable
    @Override
    public AbstractTypeInfo convert(final TypeMirror mirror) {
        
        TypeElement underlyingType = TypeUtils.TypeRetrieval.getTypeElement(mirror);
        if(underlyingType == null) {
            return null;
        }
        final AbstractTypeInfo typeInfo = this.converter.convertType(underlyingType.asType());
        return new NullableTypeInfo(typeInfo);
    }
    
}
