package com.blamejared.crafttweaker.annotation.processor.document.conversion;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.DocumentConversionRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.element.KnownElementList;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

public class ElementConverter {
    
    private final DocumentConversionRegistry conversionRegistry;
    
    public ElementConverter(DocumentConversionRegistry conversionRegistry) {
        
        this.conversionRegistry = conversionRegistry;
    }
    
    public void handleElements(KnownElementList knownElementList) {
        
        prepareElements(knownElementList);
        convertElements(knownElementList);
    }
    
    private void prepareElements(KnownElementList knownElementList) {
        
        final Collection<TypeElement> elementsForTypeDocumentation = knownElementList.getElementsForTypeDocumentation();
        final Collection<TypeElement> elementsForExpansionDocumentation = knownElementList.getElementsForExpansionDocumentation();
        
        prepareDocumentation(elementsForTypeDocumentation);
        prepareDocumentation(elementsForExpansionDocumentation);
    }
    
    private void convertElements(KnownElementList knownElementList) {
        
        final Collection<TypeElement> elementsForTypeDocumentation = knownElementList.getElementsForTypeDocumentation();
        final Collection<TypeElement> elementsForExpansionDocumentation = knownElementList.getElementsForExpansionDocumentation();
        
        handleDocumentation(elementsForTypeDocumentation);
        handleDocumentation(elementsForExpansionDocumentation);
    }
    
    private void prepareDocumentation(Collection<TypeElement> elementsToPrepare) {
        
        for(TypeElement typeElement : elementsToPrepare) {
            conversionRegistry.prepareTypePageFor(typeElement);
        }
        for(TypeElement typeElement : elementsToPrepare) {
            conversionRegistry.setCommentInfoFor(typeElement);
        }
    }
    
    private void handleDocumentation(Collection<TypeElement> elementsForExpansionDocumentation) {
        
        for(TypeElement typeElement : elementsForExpansionDocumentation) {
            conversionRegistry.convert(typeElement);
        }
    }
    
    
}
