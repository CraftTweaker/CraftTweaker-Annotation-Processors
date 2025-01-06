package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocNewLine;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocRootElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocSnippet;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocAuthorTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocDeprecatedTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocParamTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocReturnTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSeeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSinceTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocUnknownBlockTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocVersionTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocBrTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocEmTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocLiTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocPTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocStrongTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocUlTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocDocRootTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInheritDocTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInlineCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkPlainTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLiteralTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocUnknownTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocValueTag;

public interface JavadocElementVisitor<R, C> {
    
    R visit(JavadocAuthorTag element, C context);
    
    R visit(JavadocDeprecatedTag element, C context);
    
    R visit(JavadocParamTag element, C context);
    
    R visit(JavadocReturnTag element, C context);
    
    R visit(JavadocSeeTag element, C context);
    
    R visit(JavadocSinceTag element, C context);
    
    R visit(JavadocVersionTag element, C context);
    
    R visit(JavadocBrTag element, C context);
    
    R visit(JavadocCodeTag element, C context);
    
    R visit(JavadocEmTag element, C context);
    
    R visit(JavadocStrongTag element, C context);
    
    R visit(JavadocLiTag element, C context);
    
    R visit(JavadocPTag element, C context);
    
    R visit(JavadocUlTag element, C context);
    
    R visit(JavadocDocRootTag element, C context);
    
    R visit(JavadocInheritDocTag element, C context);
    
    R visit(JavadocInlineCodeTag element, C context);
    
    R visit(JavadocLinkPlainTag element, C context);
    
    R visit(JavadocLinkTag element, C context);
    
    R visit(JavadocLiteralTag element, C context);
    
    R visit(JavadocUnknownTag element, C context);
    
    R visit(JavadocUnknownBlockTag element, C context);
    
    R visit(JavadocValueTag element, C context);
    
    R visit(JavadocSnippet element, C context);
    
    R visit(JavadocNewLine element, C context);
    
    R visit(JavadocRootElement element, C context);
    
    
}
