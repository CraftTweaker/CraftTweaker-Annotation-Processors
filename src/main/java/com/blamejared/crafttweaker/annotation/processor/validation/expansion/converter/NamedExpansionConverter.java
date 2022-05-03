package com.blamejared.crafttweaker.annotation.processor.validation.expansion.converter;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.NameConverter;
import org.openzen.zencode.java.ExpansionWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class NamedExpansionConverter {
    
    private final NameConverter nameConverter;
    
    public NamedExpansionConverter(NameConverter nameConverter) {
        
        this.nameConverter = nameConverter;
    }
    
    public ExpansionInfo convertNamedExpansion(TypeElement expansionType) {
        
        final TypeMirror expandedType = getExpandedType(expansionType);
        return new ExpansionInfo(expandedType, expansionType);
    }
    
    private TypeMirror getExpandedType(TypeElement expansionType) {
        
        final ExpansionWrapper expansion = ExpansionWrapper.wrap(expansionType);
        if(expansion == null) {
            throw new IllegalArgumentException("TypeElement " + expansionType + " does not have an @Expansion annotation!");
        }
        return nameConverter.getTypeMirrorByZenCodeName(expansion.value());
    }
    
    
}
