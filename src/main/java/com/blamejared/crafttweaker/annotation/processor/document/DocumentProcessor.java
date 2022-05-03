package com.blamejared.crafttweaker.annotation.processor.document;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.ElementConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.element.KnownElementList;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.mods.KnownModList;
import com.blamejared.crafttweaker.annotation.processor.document.file.DocsJsonWriter;
import com.blamejared.crafttweaker.annotation.processor.document.file.PageWriter;
import com.blamejared.crafttweaker.crafttweaker_annotations.annotations.Document;
import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

@AutoService(Processor.class)
public class DocumentProcessor extends CraftTweakerProcessor {
    
    private static final String defaultOutputDirectory = "docsOut";
    private static final String outputDirectoryOptionName = "crafttweaker.processor.document.output_directory";
    private static final String multiSourceOptionName = "crafttweaker.processor.document.multi_source";
    
    private File outputDirectory;
    private boolean multiSourceProject;
    
    private KnownElementList knownElementList;
    private KnownModList knownModList;
    
    @Override
    public Set<String> getSupportedOptions() {
        
        final Set<String> supportedOptions = new HashSet<>(super.getSupportedOptions());
        supportedOptions.add(outputDirectoryOptionName);
        supportedOptions.add(multiSourceOptionName);
        return supportedOptions;
    }
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        // TODO
        // This should also read @CraftTweakerPlugin
        return List.of(Document.class);
    }
    
    @Override
    protected void setupDependencyContainer() {
        
        super.setupDependencyContainer();
        setupTrees(processingEnv);
        setupReflections();
    }
    
    @Override
    protected void performInitialization() {
        
        knownElementList = dependencyContainer.getInstanceOfClass(KnownElementList.class);
        knownModList = dependencyContainer.getInstanceOfClass(KnownModList.class);
        final String docsOut = processingEnv.getOptions()
                .getOrDefault(outputDirectoryOptionName, defaultOutputDirectory);
        outputDirectory = new File(docsOut);
        final String multiSource = processingEnv.getOptions().getOrDefault(multiSourceOptionName, "false");
        multiSourceProject = Boolean.parseBoolean(multiSource.toLowerCase(Locale.ROOT));
    }
    
    @Override
    protected boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        if(roundEnv.processingOver()) {
            handleLastRound();
        } else {
            handleIntermediateRound(roundEnv);
        }
        
        return false;
    }
    
    private void setupReflections() {
        
        final ConfigurationBuilder configuration = new ConfigurationBuilder().addUrls(ClasspathHelper.forJavaClassPath())
                .addClassLoaders(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader(), getClass().getClassLoader())
                .addUrls(ClasspathHelper.forClassLoader())
                .setParallel(true)
                .setExpandSuperTypes(true);
        
        final Reflections reflections = new Reflections(configuration);
        dependencyContainer.addInstanceAs(reflections, Reflections.class);
    }
    
    private void setupTrees(ProcessingEnvironment processingEnv) {
        
        final String environmentClassName = processingEnv.getClass().getName();
        
        if(!environmentClassName.equals("com.sun.tools.javac.processing.JavacProcessingEnvironment")) {
            //Let's throw the exception ourselves, since Trees.instance just throws an empty IllegalArgumentException which is harder to trace down
            throw new IllegalArgumentException("Processing environment must be JavacProcessingEnvironment, but is " + environmentClassName + "! Make sure you use gradle for compilation.");
        }
        
        final Trees instance = Trees.instance(processingEnv);
        dependencyContainer.addInstanceAs(instance, Trees.class);
    }
    
    public void handleIntermediateRound(RoundEnvironment roundEnvironment) {
        
        knownModList.fillModIdInfo(roundEnvironment);
        knownElementList.addAllForIntermediateRound(roundEnvironment);
    }
    
    public void handleLastRound() {
        
        convertPages();
        writePages();
        writeDocsJsonFile();
    }
    
    private void writeDocsJsonFile() {
        
        final DocumentRegistry documentRegistry = dependencyContainer.getInstanceOfClass(DocumentRegistry.class);
        final DocsJsonWriter docsJsonWriter = new DocsJsonWriter(outputDirectory, multiSourceProject, documentRegistry);
        try {
            docsJsonWriter.write();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
    
    private void convertPages() {
        
        final ElementConverter elementConverter = dependencyContainer.getInstanceOfClass(ElementConverter.class);
        elementConverter.handleElements(knownElementList);
    }
    
    private void writePages() {
        
        final DocumentRegistry documentRegistry = dependencyContainer.getInstanceOfClass(DocumentRegistry.class);
        final PageWriter pageWriter = new PageWriter(documentRegistry, new File(outputDirectory, "docs"), multiSourceProject);
        try {
            pageWriter.write();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }
    
}
