package com.blamejared.crafttweaker.annotation.processor.document.native_types;


import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.ExistingTypeInfo;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StdLibsNativeTypeProvider implements NativeTypeProvider {
    
    private final Map<TypeElement, AbstractTypeInfo> typeInfos = new HashMap<>();
    private final Elements elementUtils;
    
    public StdLibsNativeTypeProvider(Elements elementUtils) {
        
        this.elementUtils = elementUtils;
        addNativeClasses();
    }
    
    @Override
    public Map<TypeElement, AbstractTypeInfo> getTypeInfos() {
        
        return typeInfos;
    }
    
    private void addNativeClasses() {
        addClass(Set.class, "collections.Set");
        addClass(HashSet.class, "collections.HashSet");
        addClass(Comparable.class, "stdlib.Comparable");
        addClass(IllegalArgumentException.class, "stdlib.IllegalArgumentException");
        addClass(Exception.class, "stdlib.Exception");
        addClass(UUID.class, "stdlib.UUID");
        addClass(List.class, "stdlib.List");
        addClass(Iterable.class, "stdlib.Iterable");
        addClass(Iterator.class, "stdlib.Iterator");
        addClass(StringBuilder.class, "stdlib.StringBuilder");
    }
    
    private void addClass(Class<?> cls, String stdLibName) {
        
        final TypeElement typeElement = getElementFor(cls);
        final AbstractTypeInfo typeInfo = getTypeInfoFor(stdLibName);
        typeInfos.put(typeElement, typeInfo);
    }
    
    private TypeElement getElementFor(Class<?> cls) {
        
        return elementUtils.getTypeElement(cls.getCanonicalName());
    }
    
    private AbstractTypeInfo getTypeInfoFor(String stdLibName) {
        
        return new ExistingTypeInfo(stdLibName);
    }
    
}
