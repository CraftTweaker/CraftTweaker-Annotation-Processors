package com.blamejared.crafttweaker.annotation.processor.validation.bracket;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.api.annotation.BracketResolver;
import com.blamejared.crafttweaker.api.annotation.BracketResolverWrapper;
import com.google.auto.service.AutoService;
import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.MethodWrapper;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
public class BracketHandlerValidationProcessor extends CraftTweakerProcessor {
    
    @Override
    protected boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        for(Element element : roundEnv.getElementsAnnotatedWith(BracketResolver.class)) {
            
            final BracketResolverWrapper resolver = BracketResolverWrapper.wrap(element);
            if(resolver == null) {
                this.error("Internal Error: Element annotated with @BracketResolver does not have an annotationMirror for it", element);
                continue;
            }
            
            if(!(element instanceof ExecutableElement executableElement)) {
                this.error("How is this annotated?", element, resolver._annotationMirror());
                continue;
            }
            
            if(!MethodWrapper.isAnnotated(element)) {
                this.error("Elements annotated with @BracketResolver should also be annotated with @ZenCodeType.Method", element, resolver._annotationMirror());
            }
            
            final List<? extends VariableElement> parameters = executableElement.getParameters();
            if(parameters.size() != 1 || !TypeUtils.TypeComparison.isTypeEqual(parameters.get(0)
                    .asType(), String.class)) {
                this.error("Element is annotated with @BracketResolver but does not accept a String as its only parameter.", element, resolver._annotationMirror());
            }
            
            TypeMirror returnType = executableElement.getReturnType();
            
            if(TypeUtils.CheckTypeKind.isVoid(returnType) || TypeUtils.TypeComparison.isTypeEqual(returnType, Void.class)) {
                this.error("Element is annotated with @BracketResolver but does not have a return type.", element, resolver._annotationMirror());
            }
        }
        
        return false;
    }
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        return List.of(BracketResolver.class);
    }
    
}
