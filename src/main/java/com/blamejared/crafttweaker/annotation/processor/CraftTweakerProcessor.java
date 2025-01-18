package com.blamejared.crafttweaker.annotation.processor;

import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.SingletonDependencyContainer;
import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;
import io.toolisticon.aptk.tools.ProcessingEnvironmentUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CraftTweakerProcessor extends AbstractAnnotationProcessor {
    
    protected final DependencyContainer dependencyContainer = new SingletonDependencyContainer();
    private boolean initializedCorrectly = false;
    
    @Override
    public final synchronized void init(ProcessingEnvironment processingEnv) {
        
        try {
            ToolingProvider.setTooling(processingEnv);
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
        Instant start = Instant.now();
        
        boolean b = performProcessing(annotations, roundEnv);
        
        String duration = Duration.between(start, Instant.now()).toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
        ProcessingEnvironmentUtils.getMessager()
                .printMessage(Diagnostic.Kind.NOTE, "(" + this.getClass()
                        .getSimpleName() + ") Processing took: " + duration);
        return b;
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
    }
    
}
