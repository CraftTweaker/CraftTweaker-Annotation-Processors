package com.blamejared.crafttweaker.annotation.processor;

import com.blamejared.crafttweaker.annotation.processor.util.annotations.AnnotationMirrorUtil;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.SingletonDependencyContainer;
import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CraftTweakerProcessor extends AbstractAnnotationProcessor {
    
    protected final DependencyContainer dependencyContainer = new SingletonDependencyContainer();
    private boolean initializedCorrectly = false;
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        
        return SourceVersion.RELEASE_17;
    }
    
    @Override
    public final synchronized void init(ProcessingEnvironment processingEnv) {
        
        try {
            super.init(processingEnv);
            setupDependencyContainer();
            performInitialization();
            this.initializedCorrectly = true;
        } catch(Exception exception) {
            handleExceptionInInitializer(processingEnv, exception);
            this.initializedCorrectly = false;
        }
    }
    
    private void handleExceptionInInitializer(ProcessingEnvironment processingEnv, Exception exception) {
        
        final String className = this.getClass().getCanonicalName();
        final String exceptionMessage = exception.toString();
        final String format = String.format("Could not initialize, ignoring Annotation processor %s: %s", className, exceptionMessage);
        
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, format);
        exception.printStackTrace();
    }
    
    @Override
    public boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        if(!initializedCorrectly) {
            return false;
        }
        
        return performProcessing(annotations, roundEnv);
    }
    
    public abstract Collection<Class<? extends Annotation>> getSupportedAnnotationClasses();
    
    protected void performInitialization() {}
    
    protected abstract boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);
    
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        
        return getSupportedAnnotationClasses().stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toSet());
    }
    
    protected void setupDependencyContainer() {
        
        dependencyContainer.addInstanceAs(dependencyContainer, DependencyContainer.class);
        dependencyContainer.addInstanceAs(processingEnv, ProcessingEnvironment.class);
        dependencyContainer.addInstanceAs(processingEnv.getMessager(), Messager.class);
        dependencyContainer.addInstanceAs(processingEnv.getElementUtils(), Elements.class);
        dependencyContainer.addInstanceAs(processingEnv.getTypeUtils(), Types.class);
        dependencyContainer.addInstanceAs(new AnnotationMirrorUtil(), AnnotationMirrorUtil.class);
    }
    
    protected Messager messager() {
        
        return processingEnv.getMessager();
    }
    
    protected void error(CharSequence msg) {
        
        this.messager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
    
    protected void error(CharSequence msg, Element e) {
        
        this.messager().printMessage(Diagnostic.Kind.ERROR, msg, e);
    }
    
    protected void error(CharSequence msg, Element e, AnnotationMirror a) {
        
        this.messager().printMessage(Diagnostic.Kind.ERROR, msg, e, a);
    }
    
    protected void error(CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
        
        this.messager().printMessage(Diagnostic.Kind.ERROR, msg, e, a, v);
    }
    
    
}
