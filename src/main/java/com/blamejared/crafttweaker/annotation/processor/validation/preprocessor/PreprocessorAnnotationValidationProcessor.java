package com.blamejared.crafttweaker.annotation.processor.validation.preprocessor;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.api.annotation.Preprocessor;
import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
import com.google.auto.service.AutoService;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class PreprocessorAnnotationValidationProcessor extends CraftTweakerProcessor {
    
    @Override
    protected boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        for(TypeElement annotation : annotations) {
            for(Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if(!(element instanceof TypeElement)) {
                    this.messager().printMessage(Diagnostic.Kind.ERROR, "How is this annotated?", element);
                    continue;
                }
                
                if(!TypeUtils.TypeComparison.isAssignableTo(element.asType(), IPreprocessor.class)) {
                    this.messager()
                            .printMessage(Diagnostic.Kind.ERROR, "Element is annotated as Preprocessor but is not assignable to " + IPreprocessor.class.getCanonicalName() + "!", element);
                }
                //Not sure if the implicit no-arg constructor is already present here,
                //so let's just check no other constructor was found
                boolean anyConstructorFound = false;
                boolean noArgPublicConstructorFound = false;
                boolean instanceFieldFound = false;
                
                for(Element enclosedElement : element.getEnclosedElements()) {
                    if(enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                        anyConstructorFound = true;
                        final ExecutableElement constructor = (ExecutableElement) enclosedElement;
                        final boolean noArg = constructor.getParameters().isEmpty();
                        final boolean isPublic = constructor.getModifiers().contains(Modifier.PUBLIC);
                        if(noArg && isPublic) {
                            noArgPublicConstructorFound = true;
                            break;
                        }
                    }
                    if(enclosedElement.getKind() == ElementKind.FIELD) {
                        final VariableElement field = (VariableElement) enclosedElement;
                        if(!field.getModifiers().contains(Modifier.PUBLIC) ||
                                !field.getModifiers().contains(Modifier.STATIC)) {
                            continue;
                        }
                        
                        if(field.asType().equals(element.asType())) {
                            instanceFieldFound = true;
                        }
                    }
                }
                
                if(!noArgPublicConstructorFound && anyConstructorFound && !instanceFieldFound) {
                    this.processingEnv.getMessager()
                            .printMessage(Diagnostic.Kind.ERROR, "Element is annotated as Preprocessor but has no INSTANCE field nor a public no-arg constructor!", element);
                }
            }
        }
        return false;
    }
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        return List.of(Preprocessor.class);
    }
    
    
}
