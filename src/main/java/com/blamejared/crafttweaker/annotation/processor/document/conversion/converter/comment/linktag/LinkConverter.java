package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules.ElementLinkConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules.ImportedTypeLinkConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules.QualifiedNameLinkConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules.ThisTypeConversionRule;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LinkConverter implements IHasPostCreationCall {
    
    private final List<LinkConversionRule> conversionRules = new ArrayList<>();
    private final DependencyContainer dependencyContainer;
    private final Messager messager;
    
    public LinkConverter(DependencyContainer dependencyContainer, Messager messager) {
        
        this.dependencyContainer = dependencyContainer;
        this.messager = messager;
    }
    
    public String convertLinkToClickableMarkdown(String link, Element element, LinkConversionRule.Context context) {
        
        try {
            return conversionRules.stream()
                    .filter(rule -> rule.canConvert(link))
                    .map(rule -> rule.tryConvertToClickableMarkdown(link, element, context))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot convert link: " + link));
        } catch(Exception e) {
            messager.printMessage(Diagnostic.Kind.WARNING, "Could not convert link '" + link + "': " + e.getMessage());
            return link;
        }
    }
    
    @Override
    public void afterCreation() {
        
        addConversionRule(ElementLinkConversionRule.class);
        addConversionRule(ThisTypeConversionRule.class);
        addConversionRule(ImportedTypeLinkConversionRule.class);
        addConversionRule(QualifiedNameLinkConversionRule.class);
    }
    
    private void addConversionRule(Class<? extends LinkConversionRule> ruleClass) {
        
        final LinkConversionRule rule = dependencyContainer.getInstanceOfClass(ruleClass);
        conversionRules.add(rule);
    }
    
}
