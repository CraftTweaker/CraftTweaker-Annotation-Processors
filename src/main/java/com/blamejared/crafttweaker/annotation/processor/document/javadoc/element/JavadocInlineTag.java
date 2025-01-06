package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInlineCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocDocRootTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInheritDocTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkPlainTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLiteralTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocUnknownTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocValueTag;

import java.util.Optional;

public interface JavadocInlineTag extends JavadocElement {
    
    static JavadocInlineTag from(JavaDocContainerElement container, String name, Optional<String> content) {
        
        return switch(name) {
            case "code" -> new JavadocInlineCodeTag(container, content.orElseThrow());
            case "link" -> new JavadocLinkTag(container, content.orElseThrow());
            case "linkplain" -> new JavadocLinkPlainTag(container, content.orElseThrow());
            case "literal" -> new JavadocLiteralTag(container, content.orElseThrow());
            case "docRoot" -> new JavadocDocRootTag(container);
            case "inheritDoc" -> new JavadocInheritDocTag(container);
            case "value" -> new JavadocValueTag(container, content.orElseThrow());
            default -> new JavadocUnknownTag(container, name, content.orElse(""));
        };
    }
    
    String name();
    
    String content();
    
    @Override
    default String toText() {
        String content = this.content();
        if(content == null || content.isBlank()) {
            return "{@" + this.name() + "}";
        }
        return "{@" + this.name() + " " + this.content() + "}";
    }
    
}
