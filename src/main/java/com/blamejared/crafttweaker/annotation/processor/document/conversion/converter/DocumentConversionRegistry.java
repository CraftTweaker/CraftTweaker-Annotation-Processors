package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter;

import com.blamejared.crafttweaker.annotation.processor.document.DocumentRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.event.NamedEventConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.event.NativeEventConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.expansion.ExpansionConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.named_type.NamedTypeConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.native_registration.NativeRegistrationConverter;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;

import javax.annotation.Nonnull;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class DocumentConversionRegistry {
    
    private final List<DocumentConverter> converters = new ArrayList<>();
    private final DocumentRegistry documentRegistry;
    
    public DocumentConversionRegistry(DocumentRegistry documentRegistry, DependencyContainer dependencyContainer) {
        
        this.documentRegistry = documentRegistry;
        //
        // TODO: Add converters
        converters.add((dependencyContainer.getInstanceOfClass(NativeEventConverter.class)));
        converters.add((dependencyContainer.getInstanceOfClass(NamedEventConverter.class)));
        converters.add(dependencyContainer.getInstanceOfClass(NativeRegistrationConverter.class));
        converters.add(dependencyContainer.getInstanceOfClass(ExpansionConverter.class));
        converters.add(dependencyContainer.getInstanceOfClass(NamedTypeConverter.class));
    }
    
    public void prepareTypePageFor(TypeElement typeElement) {
        
        setPageInfo(typeElement);
    }
    
    public void setCommentInfoFor(TypeElement typeElement) {
        
        setCommentData(typeElement);
    }
    
    public void convert(TypeElement typeElement) {
        
        convertAndAddToRegistry(typeElement);
    }
    
    private void convertAndAddToRegistry(TypeElement typeElement) {
        
        converters.stream()
                .filter(converter -> documentRegistry.hasPageInfoFor(typeElement))
                .filter(converter -> converter.canConvert(typeElement))
                .map(converter -> converter.convert(typeElement, documentRegistry.getPageInfoFor(typeElement)))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(documentRegistry::addDocumentationPage);
    }
    
    private void setCommentData(TypeElement typeElement) {
        
        converters.stream()
                .filter(converter -> documentRegistry.hasPageInfoFor(typeElement))
                .filter(converter -> converter.canConvert(typeElement))
                .forEach(setCommentInfo(typeElement));
        
    }
    
    @Nonnull
    private Consumer<DocumentConverter> setCommentInfo(TypeElement typeElement) {
        
        return converter -> converter.setDocumentationCommentTo(typeElement, documentRegistry.getPageInfoFor(typeElement));
    }
    
    private void setPageInfo(TypeElement typeElement) {
        
        converters.stream()
                .filter(converter -> converter.canConvert(typeElement))
                .map(converter -> converter.prepareConversion(typeElement))
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(pageInfo -> documentRegistry.addInfo(typeElement, pageInfo));
    }
    
}
