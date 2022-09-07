package com.blamejared.crafttweaker.annotation.processor.validation.keyword;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.annotation.processor.validation.util.ZenCodeKeywordUtil;
import com.google.auto.service.AutoService;
import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.*;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AutoService(Processor.class)
public class KeywordValidationProcessor extends CraftTweakerProcessor {
    
    private final Map<String, Function<Element, Optional<Set<String>>>> annotationConverters = Util.make(new HashMap<>(), map -> {
        
        map.put(ZenCodeType.Method.class.getCanonicalName(), element -> Optional.ofNullable(MethodWrapper.wrap(element))
                .map(MethodWrapper::value).map(Set::of));
        
        map.put(ZenCodeType.Getter.class.getCanonicalName(), element -> Optional.ofNullable(GetterWrapper.wrap(element))
                .map(GetterWrapper::value).map(Set::of));
        
        map.put(ZenCodeType.Setter.class.getCanonicalName(), element -> Optional.ofNullable(SetterWrapper.wrap(element))
                .map(SetterWrapper::value).map(Set::of));
        
        map.put(ZenCodeType.Field.class.getCanonicalName(), element -> Optional.ofNullable(FieldWrapper.wrap(element))
                .map(FieldWrapper::value).map(Set::of));
        
        map.put(ZenCodeType.Name.class.getCanonicalName(), element -> Optional.ofNullable(NameWrapper.wrap(element))
                .map(NameWrapper::value)
                .map(s -> Arrays.stream(s.split("\\.")))
                .map(stringStream -> stringStream.collect(Collectors.toSet())));
    });
    private ZenCodeKeywordUtil keywordUtil;
    
    @Override
    protected void performInitialization() {
        
        this.keywordUtil = dependencyContainer.getInstanceOfClass(ZenCodeKeywordUtil.class);
    }
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        return List.of(ZenCodeType.Method.class, ZenCodeType.Getter.class, ZenCodeType.Setter.class, ZenCodeType.Field.class, ZenCodeType.Name.class);
    }
    
    @Override
    protected boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        for(TypeElement annotation : annotations) {
            for(Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                verifyElementNameUseNoKeywordAsName(annotation, element);
            }
        }
        return false;
    }
    
    private void verifyElementNameUseNoKeywordAsName(TypeElement annotation, Element element) {
        
        annotationConverters.get(TypeUtils.TypeConversion.convertToFqn(annotation.asType()))
                .apply(element)
                .filter(Predicate.not(Set::isEmpty))
                .ifPresentOrElse(names -> {
                    keywordUtil.checkName(names, element, this.messager());
                }, () -> keywordUtil.checkName(element, this.messager()));
    }
    
}
