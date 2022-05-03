package com.blamejared.crafttweaker.annotation.processor.document.native_types;


import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import javax.lang.model.element.TypeElement;
import java.util.Map;

public interface NativeTypeProvider {
    
    Map<TypeElement, AbstractTypeInfo> getTypeInfos();
    
}
