package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag;

import javax.lang.model.element.Element;
import java.util.Optional;

public interface LinkConversionRule {
    
    boolean canConvert(String link);
    
    Optional<String> tryConvertToClickableMarkdown(String link, Element element);
    
}
