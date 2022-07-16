package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.LinkConversionRule;

import javax.lang.model.element.Element;
import java.util.Optional;

public class ThisTypeConversionRule implements LinkConversionRule {
    
    @Override
    public boolean canConvert(String link) {
        
        return link.isEmpty();
    }
    
    @Override
    public Optional<String> tryConvertToClickableMarkdown(String link, Element element, LinkConversionRule.Context context) {
        
        return Optional.of("[this](.)");
    }
    
}
