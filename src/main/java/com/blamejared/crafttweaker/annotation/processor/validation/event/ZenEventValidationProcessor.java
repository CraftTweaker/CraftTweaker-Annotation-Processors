package com.blamejared.crafttweaker.annotation.processor.validation.event;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.ZenEventValidator;
import com.blamejared.crafttweaker.api.event.ZenEvent;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class ZenEventValidationProcessor extends CraftTweakerProcessor {
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        return List.of(ZenEvent.class, ZenEvent.BusCarrier.class);
    }
    
    @Override
    public boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        ZenEventValidator.INSTANCE.validateAll(roundEnv.getElementsAnnotatedWithAny(Set.of(ZenEvent.class, ZenEvent.BusCarrier.class)));
        return false;
    }
    
}
