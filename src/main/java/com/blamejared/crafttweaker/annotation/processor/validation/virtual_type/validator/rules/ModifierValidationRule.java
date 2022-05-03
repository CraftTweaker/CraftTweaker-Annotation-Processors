package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.annotation.processor.util.annotations.AnnotationMirrorUtil;
import io.toolisticon.aptk.tools.ElementUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ModifierValidationRule implements VirtualTypeValidationRule {
    
    private final Messager messager;
    private final AnnotationMirrorUtil annotationMirrorUtil;
    private final Set<Class<? extends Annotation>> checkedAnnotations = Util.make(new HashSet<>(), set -> {
        set.add(ZenCodeType.Method.class);
        set.add(ZenCodeType.Constructor.class);
        set.add(ZenCodeType.Field.class);
    });
    
    private final Set<Class<? extends Annotation>> virtualOnlyAnnotations = Util.make(new HashSet<>(), set -> {
        set.add(ZenCodeType.Operator.class);
        set.add(ZenCodeType.Getter.class);
        set.add(ZenCodeType.Setter.class);
        set.add(ZenCodeType.Caster.class);
        set.add(ZenCodeType.Constructor.class);
    });
    
    public ModifierValidationRule(Messager messager, AnnotationMirrorUtil annotationMirrorUtil) {
        
        this.messager = messager;
        this.annotationMirrorUtil = annotationMirrorUtil;
    }
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return hasZenAnnotation(enclosedElement);
    }
    
    private boolean hasZenAnnotation(Element typeElement) {
        
        return hasVirtualOnlyAnnotation(typeElement) || hasCheckedAnnotation(typeElement);
    }
    
    private boolean hasVirtualOnlyAnnotation(Element typeElement) {
        
        return virtualOnlyAnnotations.stream().anyMatch(annotationPresentOn(typeElement));
    }
    
    private boolean hasCheckedAnnotation(Element typeElement) {
        
        return checkedAnnotations.stream().anyMatch(annotationPresentOn(typeElement));
    }
    
    private Predicate<Class<? extends Annotation>> annotationPresentOn(Element typeElement) {
        
        return annotationClass -> typeElement.getAnnotation(annotationClass) != null;
    }
    
    @Override
    public void validate(Element enclosedElement) {
        
        if(notPublic(enclosedElement)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "All exposed members must be public", enclosedElement);
        }
        if(notVirtual(enclosedElement)) {
            getVirtualOnlyMirror(enclosedElement).forEach(mirror -> {
                final String message = "Annotation requires instance member";
                messager.printMessage(Diagnostic.Kind.ERROR, message, enclosedElement, mirror);
            });
        }
    }
    
    private boolean notPublic(Element typeElement) {
        
        return !ElementUtils.CheckModifierOfElement.hasPublicModifier(typeElement);
    }
    
    private boolean notVirtual(Element typeElement) {
        
        return hasVirtualOnlyAnnotation(typeElement) && isStatic(typeElement);
    }
    
    private boolean isStatic(Element typeElement) {
        
        return ElementUtils.CheckModifierOfElement.hasStaticModifier(typeElement);
    }
    
    private Stream<AnnotationMirror> getVirtualOnlyMirror(Element typeElement) {
        
        return virtualOnlyAnnotations.stream()
                .filter(annotationPresentOn(typeElement))
                .map(annotation -> annotationMirrorUtil.getMirror(typeElement, annotation));
    }
    
}
