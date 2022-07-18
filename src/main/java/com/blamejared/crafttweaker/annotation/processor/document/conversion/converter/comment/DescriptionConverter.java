package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.CodeTagReplacer;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterRemover;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.LinkTagReplacer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import java.util.Optional;

public class DescriptionConverter {
    
    private final LinkTagReplacer linkTagReplacer;
    private final CodeTagReplacer codeTagReplacer;
    private final ParagraphMarkdownConverter paragraphMarkdownConverter;
    private final ParameterRemover parameterRemover;
    
    public DescriptionConverter(LinkTagReplacer linkTagReplacer, CodeTagReplacer codeTagReplacer,
                                ParagraphMarkdownConverter paragraphMarkdownConverter, ParameterRemover parameterRemover) {
        
        this.linkTagReplacer = linkTagReplacer;
        this.codeTagReplacer = codeTagReplacer;
        this.paragraphMarkdownConverter = paragraphMarkdownConverter;
        this.parameterRemover = parameterRemover;
    }
    
    public Optional<String> convertFromCommentString(@Nullable String docComment, Element element) {
        
        if(docComment == null) {
            return Optional.empty();
        }
    
        docComment = this.linkTagReplacer.replaceLinkTagsFrom(docComment, element);
        docComment = this.codeTagReplacer.replaceCodeTags(docComment);
        docComment = this.paragraphMarkdownConverter.convertParagraphToMarkdown(docComment);
        // TODO: Convert tables
        docComment = parameterRemover.removeDocumentationParametersFrom(docComment);
        return Optional.of(docComment.strip());
    }
    
}
