package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.LinkConversionRule;

import javax.lang.model.element.Element;
import java.util.Optional;

public class FunctionalInterfaceRule implements LinkConversionRule {
    
    @Override
    public boolean canConvert(String link) {
        
        return link.equals("FunctionalInterface");
    }
    
    @Override
    public Optional<String> tryConvertToClickableMarkdown(String link, Element element, Context context) {
        
        return Optional.of(String.format("[%s](https://docs.oracle.com/javase/8/docs/api/java/lang/FunctionalInterface.html)", link));
    }
    
}
