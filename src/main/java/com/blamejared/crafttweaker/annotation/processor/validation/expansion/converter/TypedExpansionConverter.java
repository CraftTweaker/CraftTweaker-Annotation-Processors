package com.blamejared.crafttweaker.annotation.processor.validation.expansion.converter;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.element.ClassTypeConverter;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import com.blamejared.crafttweaker.crafttweaker_annotations.annotations.TypedExpansionWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class TypedExpansionConverter {
    
    private final ClassTypeConverter classTypeConverter;
    
    public TypedExpansionConverter(ClassTypeConverter classTypeConverter) {
        
        this.classTypeConverter = classTypeConverter;
    }
    
    public ExpansionInfo convertTypedExpansion(TypeElement expansionType) {
        
        final TypeMirror expandedType = getExpandedType(expansionType);
        return new ExpansionInfo(expandedType, expansionType);
    }
    
    private TypeMirror getExpandedType(TypeElement expansionType) {
        
        final TypedExpansionWrapper typedExpansion = TypedExpansionWrapper.wrap(expansionType);
        if(typedExpansion == null) {
            throw new IllegalArgumentException("TypeElement " + expansionType + " does not have an @NativeTypeRegistration annotation!");
        }
        return typedExpansion.valueAsTypeMirror();
    }
    
}
