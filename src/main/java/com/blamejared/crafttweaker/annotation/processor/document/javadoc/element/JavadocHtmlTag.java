package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import java.util.Optional;
import java.util.stream.Collectors;

public interface JavadocHtmlTag extends JavaDocContainerElement {
    
    static JavadocHtmlTag from(String name, Optional<String> content) {
        
        return null;
        //        return switch(name) {
        //            case "code" -> new JavadocCodeTag(content.orElseThrow());
        //            case "link" -> new JavadocLinkTag(content.orElseThrow());
        //            case "linkplain" -> new JavadocLinkPlainTag(content.orElseThrow());
        //            case "literal" -> new JavadocLiteralTag(content.orElseThrow());
        //            case "docRoot" -> new JavadocDocRootTag();
        //            case "inheritDoc" -> new JavadocInheritDocTag();
        //            case "value" -> new JavadocValueTag(content.orElseThrow());
        //            default -> new JavadocUnkownTag(name, content.orElse(""));
        //        };
        //
    }
    
    String name();
    
    
    default String toText() {
        
        return "<" + name() + ">" + elements().stream().map(JavadocElement::toText).collect(Collectors.joining()) + "</" + name() + ">";
    }
    
}
