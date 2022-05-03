package com.blamejared.crafttweaker.annotation.processor.document.native_types.dependency_rule;

import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import javax.lang.model.element.TypeElement;
import java.util.Map;

public interface ModDependencyConversionRule {
    
    Map<TypeElement, AbstractTypeInfo> getAll();
    
}
