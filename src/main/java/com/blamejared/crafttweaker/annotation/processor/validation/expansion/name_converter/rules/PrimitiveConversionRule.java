package com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.rules;

import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.NameConversionRule;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.annotation.Nullable;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveConversionRule implements NameConversionRule {
    
    private static final Map<String, TypeKind> primitiveTypes = Util.make(new HashMap<>(), map -> {
        // signed
        map.put("bool", TypeKind.BOOLEAN);
        map.put("byte", TypeKind.BYTE);
        map.put("char", TypeKind.CHAR);
        map.put("short", TypeKind.SHORT);
        map.put("int", TypeKind.INT);
        map.put("long", TypeKind.LONG);
        map.put("float", TypeKind.FLOAT);
        map.put("double", TypeKind.DOUBLE);
        
        // unsigned
        map.put("ushort", TypeKind.SHORT);
        map.put("uint", TypeKind.INT);
        map.put("ulong", TypeKind.LONG);
    });
    
    @Nullable
    @Override
    public TypeMirror convertZenCodeName(String zenCodeName) {
        
        if(primitiveTypes.containsKey(zenCodeName)) {
            return getMirrorFromClass(primitiveTypes.get(zenCodeName));
        } else if(zenCodeName.equals("string")) {
            return getString();
        }
        return null;
    }
    
    private TypeMirror getString() {
        
        final String canonicalName = String.class.getCanonicalName();
        return TypeUtils.TypeRetrieval.getTypeMirror(canonicalName);
    }
    
    private TypeMirror getMirrorFromClass(TypeKind kind) {
        
        return TypeUtils.getTypes().getPrimitiveType(kind);
    }
    
}
