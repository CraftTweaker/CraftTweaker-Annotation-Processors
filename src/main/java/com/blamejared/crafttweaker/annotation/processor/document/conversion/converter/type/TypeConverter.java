package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type;

import com.blamejared.crafttweaker.annotation.processor.document.DocumentRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.NativeConversionRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules.*;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules.generic.GenericTypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.rules.generic.MapConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypeName;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypePageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.ExistingTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.GenericTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.TypePageTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.name_converter.rules.GenericConversionRule;
import io.toolisticon.aptk.tools.MessagerUtils;

import javax.annotation.Nonnull;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class TypeConverter implements IHasPostCreationCall {
    
    private final NativeConversionRegistry nativeConversionRegistry;
    private final DocumentRegistry registry;
    private final DependencyContainer dependencyContainer;
    private final List<TypeConversionRule> rules = new ArrayList<>();
    
    public TypeConverter(NativeConversionRegistry nativeConversionRegistry, DocumentRegistry registry, DependencyContainer dependencyContainer) {
        
        this.nativeConversionRegistry = nativeConversionRegistry;
        this.registry = registry;
        this.dependencyContainer = dependencyContainer;
    }
    
    public AbstractTypeInfo convertByName(TypeName name) {
        
        final Optional<TypePageInfo> pageInfoByName = registry.getPageInfoByName(name);
        if(pageInfoByName.isPresent()) {
            return new TypePageTypeInfo(pageInfoByName.get());
        }
        
        if(hasNativePageInfo(name)) {
            return getNativePageInfo(name);
        }
        
        if(isGeneric(name)) {
            return getGeneric(name);
        }
        
        //Problem: When preparing the ATIs we already convert the comments :thinking:
        MessagerUtils.getMessager()
                .printMessage(Diagnostic.Kind.WARNING, "Unable to convert page: '" + name.getZenCodeName() + "', replacing with a dummy link. Make sure it has an @Document annotation and doesn't link to any pages that don't have the annotation!");
        return new ExistingTypeInfo(name.getZenCodeName());
        //        throw new UnsupportedOperationException("TODO: Unable to convert page: " + name.getZenCodeName() + " Make sure it has an @Document annotation!");
    }
    
    private AbstractTypeInfo getGeneric(TypeName name) {
        
        final Matcher matcher = getGenericMatcher(name);
        if(!matcher.find()) {
            throw new IllegalArgumentException("Could not extract generic types from name: " + name.getZenCodeName());
        }
        final MatchResult matchResult = matcher.toMatchResult();
        final AbstractTypeInfo baseType = getGenericBaseType(matchResult);
        final List<AbstractTypeInfo> parameters = getGenericParameters(matcher);
        return new GenericTypeInfo(baseType, parameters);
    }
    
    private AbstractTypeInfo getGenericBaseType(MatchResult matchResult) {
        
        final String zenCodeName = matchResult.group(1).trim();
        return convertByName(new TypeName(zenCodeName));
    }
    
    private List<AbstractTypeInfo> getGenericParameters(Matcher matcher) {
        
        return Arrays.stream(matcher.group(2).split(","))
                .map(String::trim)
                .map(TypeName::new)
                .map(this::convertByName)
                .collect(Collectors.toList());
    }
    
    @Nonnull
    private Matcher getGenericMatcher(TypeName name) {
        
        final Matcher matcher = GenericConversionRule.GENERIC_PATTERN.matcher(name.getZenCodeName());
        return matcher;
    }
    
    private boolean isGeneric(TypeName name) {
        
        return name.getZenCodeName().contains("<");
    }
    
    private AbstractTypeInfo getNativePageInfo(TypeName name) {
        
        return nativeConversionRegistry.getNativeTypeInfoWithName(name);
    }
    
    
    private boolean hasNativePageInfo(TypeName name) {
        
        return nativeConversionRegistry.hasNativeTypeInfoWithName(name);
    }
    
    public AbstractTypeInfo convertType(TypeMirror typeMirror) {
        
        // TODO
        return tryConvertType(typeMirror).orElse(new AbstractTypeInfo() {
            @Override
            public String getDisplayName() {
                
                return "invalid";
            }
            
            @Override
            public String getClickableMarkdown() {
                
                return "**invalid**";
            }
        });
        //                .orElseThrow(() -> new IllegalArgumentException("Could not convert " + typeMirror));
    }
    
    public Optional<AbstractTypeInfo> tryConvertType(TypeMirror typeMirror) {
        
        return rules.stream()
                .filter(rule -> rule.canConvert(typeMirror))
                .map(rule -> rule.convert(typeMirror))
                .filter(Objects::nonNull)
                .findFirst();
    }
    
    @Override
    public void afterCreation() {
        
        addConversionRules();
    }
    
    private void addConversionRules() {
        
        addConversionRule(PrimitiveWrapperParameterConversionRule.class);
        addConversionRule(NullableAnnotatedParameterConversionRule.class);
        addConversionRule(TypeParameterConversionRule.class);
        addConversionRule(VoidConversionRule.class);
        addConversionRule(MapConversionRule.class);
        addConversionRule(GenericTypeConversionRule.class);
        addConversionRule(NativeTypeConversionRule.class);
        addConversionRule(ArrayConversionRule.class);
        addConversionRule(NamedTypeConversionRule.class);
        addConversionRule(JavaLangConversionRule.class);
        addConversionRule(JavaFunctionConversionRule.class);
        addConversionRule(PrimitiveConversionRule.class);
        //addConversionRule(FallbackConversionRule.class);
    }
    
    private void addConversionRule(Class<? extends TypeConversionRule> ruleClass) {
        
        rules.add(dependencyContainer.getInstanceOfClass(ruleClass));
    }
    
}
