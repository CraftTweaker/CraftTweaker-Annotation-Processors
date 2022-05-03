package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules.generic;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConverter;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractGenericTypeConversionRule implements TypeConversionRule {
    
    protected final TypeConverter typeConverter;
    protected final Types typeUtils;
    
    public AbstractGenericTypeConversionRule(TypeConverter typeConverter, Types typeUtils) {
        
        this.typeConverter = typeConverter;
        this.typeUtils = typeUtils;
    }
    
    protected boolean isGenericType(TypeMirror mirror) {
        
        return isDeclaredType(mirror) && isGenericMirror((DeclaredType) mirror);
        
    }
    
    private boolean isGenericMirror(DeclaredType typeElement) {
        
        return !typeElement.getTypeArguments().isEmpty();
    }
    
    private boolean isDeclaredType(TypeMirror mirror) {
        
        return mirror instanceof DeclaredType;
    }
    
    
    protected Optional<AbstractTypeInfo> convertBaseClass(TypeMirror mirror) {
        
        final TypeMirror baseTypeMirror = getBaseType(mirror);
        return typeConverter.tryConvertType(baseTypeMirror);
    }
    
    protected TypeMirror getBaseType(TypeMirror mirror) {
        
        return typeUtils.erasure(mirror);
    }
    
    protected boolean isBaseTypeOfClass(TypeMirror mirror, Class<?> cls) {
        
        return getBaseType(mirror).toString().equals(cls.getCanonicalName());
    }
    
    protected Optional<List<AbstractTypeInfo>> convertTypeArguments(TypeMirror mirror) {
        
        final List<? extends TypeMirror> typeArguments = getTypeArguments(mirror);
        final List<Optional<AbstractTypeInfo>> typeArgumentOptionalList = getTypeArgumentOptionalList(typeArguments);
        return unwrapOptionalList(typeArgumentOptionalList);
    }
    
    private Optional<List<AbstractTypeInfo>> unwrapOptionalList(List<Optional<AbstractTypeInfo>> typeArgumentOptionalList) {
        
        final List<AbstractTypeInfo> result = new ArrayList<>();
        for(Optional<AbstractTypeInfo> abstractTypeInfo : typeArgumentOptionalList) {
            if(abstractTypeInfo.isPresent()) {
                result.add(abstractTypeInfo.get());
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(result);
    }
    
    @NotNull
    private List<Optional<AbstractTypeInfo>> getTypeArgumentOptionalList(List<? extends TypeMirror> typeArguments) {
        
        return typeArguments.stream().map(typeConverter::tryConvertType).collect(Collectors.toList());
    }
    
    private List<? extends TypeMirror> getTypeArguments(TypeMirror mirror) {
        
        return ((DeclaredType) mirror).getTypeArguments();
    }
    
    
}
