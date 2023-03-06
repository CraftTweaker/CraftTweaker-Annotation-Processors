package com.blamejared.crafttweaker.annotation.processor.validation.expansion.converter;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.element.ClassTypeConverter;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistrationWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class NativeTypeConverter {
    
    private final ClassTypeConverter classTypeConverter;
    
    public NativeTypeConverter(ClassTypeConverter classTypeConverter) {
        
        this.classTypeConverter = classTypeConverter;
    }
    
    
    public ExpansionInfo convertNativeType(TypeElement element) {
        
        final TypeMirror expandedType = getExpandedType(element);
        return new ExpansionInfo(expandedType, element);
    }
    
    private TypeMirror getExpandedType(TypeElement expansionType) {
        
        final NativeTypeRegistrationWrapper ntr = NativeTypeRegistrationWrapper.wrap(expansionType);
        if(ntr == null) {
            throw new IllegalArgumentException("TypeElement " + expansionType + " does not have an @NativeTypeRegistration annotation!");
        }
        return ntr.valueAsTypeMirror();
    }
    
}
