package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.event;

import com.blamejared.crafttweaker.annotation.processor.document.NativeConversionRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.DocumentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.CommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.EnumConstantConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.header.GenericParameterConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.static_member.StaticMemberConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.named_type.*;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.native_registration.member.NativeTypeVirtualMemberConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.element.ClassTypeConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.mods.KnownModList;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.*;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.enum_constant.DocumentedEnumConstants;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.DocumentedGenericParameter;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.Example;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member.DocumentedStaticMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member.DocumentedVirtualMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.page.*;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.*;
import com.blamejared.crafttweaker.api.event.ZenEventWrapper;
import com.blamejared.crafttweaker_annotations.annotations.*;

import javax.annotation.Nonnull;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Collectors;

public class EventConverter extends DocumentConverter {
    
    private final StaticMemberConverter staticMemberConverter;
    private final NativeTypeVirtualMemberConverter virtualMemberConverter;
    private final SuperTypeConverter superTypeConverter;
    private final ImplementationConverter implementationConverter;
    private final GenericParameterConverter genericParameterConverter;
    private final NativeConversionRegistry nativeConversionRegistry;
    private final Types typeUtils;
    private final ClassTypeConverter classTypeConverter;
    private final EnumConstantConverter enumConstantConverter;
    
    public EventConverter(KnownModList knownModList, CommentConverter commentConverter, StaticMemberConverter staticMemberConverter, NativeTypeVirtualMemberConverter virtualMemberConverter, SuperTypeConverter superTypeConverter, ImplementationConverter implementationConverter, GenericParameterConverter genericParameterConverter, NativeConversionRegistry nativeConversionRegistry, Types typeUtils, ClassTypeConverter classTypeConverter, EnumConstantConverter enumConstantConverter) {
        
        super(knownModList, commentConverter);
        this.staticMemberConverter = staticMemberConverter;
        this.virtualMemberConverter = virtualMemberConverter;
        this.superTypeConverter = superTypeConverter;
        this.implementationConverter = implementationConverter;
        this.genericParameterConverter = genericParameterConverter;
        this.nativeConversionRegistry = nativeConversionRegistry;
        this.typeUtils = typeUtils;
        this.classTypeConverter = classTypeConverter;
        this.enumConstantConverter = enumConstantConverter;
    }
    
    @Override
    public boolean canConvert(TypeElement typeElement) {
        
        return getNativeAnnotation(typeElement) != null && ZenEventWrapper.isAnnotated(typeElement);
    }
    
    private NativeTypeRegistrationWrapper getNativeAnnotation(TypeElement typeElement) {
        
        return NativeTypeRegistrationWrapper.wrap(typeElement);
    }
    
    @Override
    protected TypePageInfo prepareConversion(TypeElement element) {
        
        final TypePageInfo typePageInfo = createTypePageInfo(element);
        registerNativeType(element, typePageInfo);
        return typePageInfo;
    }
    
    @Override
    protected Example getFallbackThisInformationFor(TypeElement typeElement) {
        
        final String text = "my" + getName(typeElement).getSimpleName();
        return new Example("this", text);
    }
    
    private void registerNativeType(TypeElement element, TypePageInfo typePageInfo) {
        
        final AbstractTypeInfo typeInfo = new TypePageTypeInfo(typePageInfo);
        final TypeElement nativeType = getNativeType(element);
        
        nativeConversionRegistry.addNativeConversion(nativeType, typeInfo);
    }
    
    private TypeElement getNativeType(TypeElement element) {
        
        final NativeTypeRegistrationWrapper annotation = getNativeAnnotation(element);
        final TypeMirror nativeType = annotation.valueAsTypeMirror();
        return getTypeElementFromMirror(nativeType);
    }
    
    private TypeElement getTypeElementFromMirror(TypeMirror nativeType) {
        
        final Element element = typeUtils.asElement(nativeType);
        if(element instanceof TypeElement) {
            return (TypeElement) element;
        }
        
        throw new IllegalArgumentException("Could not get typeElement from mirror: " + nativeType);
    }
    
    @Nonnull
    private TypePageInfo createTypePageInfo(TypeElement element) {
        
        final DocumentationPageInfo documentationPageInfo = super.prepareConversion(element);
        
        final TypeName name = getName(element);
        final String declaringModId = documentationPageInfo.declaringModId;
        final String outputPath = documentationPageInfo.getOutputPath();
        return new TypePageInfo(declaringModId, outputPath, name);
    }
    
    @Nonnull
    private TypeName getName(TypeElement element) {
        
        return new TypeName(getNativeAnnotation(element).zenCodeName());
    }
    
    @Override
    public DocumentationPage convert(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        final DocumentedVirtualMembers virtualMembers = convertVirtualMembers(typeElement, pageInfo);
        final AbstractTypeInfo superType = convertSuperType(typeElement);
        final List<AbstractTypeInfo> implementedInterfaces = convertImplementedInterfaces(typeElement);
        final DocumentedStaticMembers staticMembers = convertStaticMembers(typeElement, pageInfo);
        final List<DocumentedGenericParameter> genericParameters = convertGenericParameters(typeElement);
        return new EventPage((TypePageInfo) pageInfo, virtualMembers, superType, implementedInterfaces, staticMembers, genericParameters);
    }
    
    protected DocumentedVirtualMembers convertVirtualMembers(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        return virtualMemberConverter.convertFor(typeElement, pageInfo);
    }
    
    protected AbstractTypeInfo convertSuperType(TypeElement typeElement) {
        
        if(isNativeEnum(typeElement)) {
            // we should not print redundant information "extending Enum<E>"
            return null;
        }
        final TypeElement nativeType = getNativeType(typeElement);
        return superTypeConverter.convertSuperTypeFor(nativeType).orElse(null);
    }
    
    protected List<AbstractTypeInfo> convertImplementedInterfaces(TypeElement typeElement) {
        
        final TypeElement nativeType = getNativeType(typeElement);
        return implementationConverter.convertInterfacesFor(nativeType);
    }
    
    protected AbstractTypeInfo convertThisType(TypeElement typeElement) {
        
        return superTypeConverter.getTypeConverter().convertByName(getName(typeElement));
    }
    
    protected DocumentedStaticMembers convertStaticMembers(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        return staticMemberConverter.convertFor(typeElement, pageInfo);
    }
    
    protected void convertNativeEnumMembers(TypeElement typeElement, DocumentedEnumConstants documentedEnumConstants) {
        
        enumConstantConverter.convertAndAddTo(getNativeType(typeElement), documentedEnumConstants, typeElement.getAnnotation(BracketEnum.class));
    }
    
    private List<DocumentedGenericParameter> convertGenericParameters(TypeElement typeElement) {
        
        return typeElement.getTypeParameters()
                .stream()
                .map(genericParameterConverter::convertGenericParameter)
                .collect(Collectors.toList());
    }
    
    private boolean isNativeEnum(TypeElement typeElement) {
        
        return getNativeType(typeElement).getKind() == ElementKind.ENUM;
    }
    
}