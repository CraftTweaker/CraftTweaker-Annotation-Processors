package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.HTMLAwareJavadocDescription;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocAuthorTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocDeprecatedTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocParamTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocReturnTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSeeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSinceTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocUnknownBlockTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocVersionTag;

import java.util.Optional;
import java.util.stream.Collectors;

public interface JavadocBlockTag extends JavaDocContainerElement {
    
    static JavadocBlockTag from(JavaDocContainerElement container, String name, Optional<String> content) {
        
        JavadocBlockTag tag = switch(name) {
            case "author" -> new JavadocAuthorTag(container);
            case "deprecated" -> new JavadocDeprecatedTag(container);
            case "param" -> new JavadocParamTag(container);
            case "return" -> new JavadocReturnTag(container);
            case "see" -> new JavadocSeeTag(container);
            case "since" -> new JavadocSinceTag(container);
            case "version" -> new JavadocVersionTag(container);
            default -> new JavadocUnknownBlockTag(container, name);
        };
        content.ifPresent(tag::parseContent);
        return tag;
    }
    
    default void parseContent(String content) {
        
        this.addElement(HTMLAwareJavadocDescription.parse(content, false));
    }
    
    String name();
    
    default String content() {
        
        return this.elements().stream().map(JavadocElement::toText).collect(Collectors.joining());
    }
    
    @Override
    default String toText() {
        
        String content = this.content();
        if(content == null || content.isBlank()) {
            return "@" + this.name();
        }
        return "@" + this.name() + " " + this.content();
    }
    
}
