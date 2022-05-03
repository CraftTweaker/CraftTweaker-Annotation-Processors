package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.VirtualTypeValidator;
import com.google.auto.service.AutoService;
import org.openzen.zencode.java.ZenCodeType;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class VirtualTypeValidationProcessor extends CraftTweakerProcessor {
    
    private VirtualTypeValidator virtualTypeValidator;
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        return List.of(ZenCodeType.Name.class);
    }
    
    @Override
    public synchronized void performInitialization() {
        
        virtualTypeValidator = dependencyContainer.getInstanceOfClass(VirtualTypeValidator.class);
    }
    
    @Override
    public boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        virtualTypeValidator.validateAll(roundEnv.getElementsAnnotatedWith(ZenCodeType.Name.class));
        return false;
    }
    
}
