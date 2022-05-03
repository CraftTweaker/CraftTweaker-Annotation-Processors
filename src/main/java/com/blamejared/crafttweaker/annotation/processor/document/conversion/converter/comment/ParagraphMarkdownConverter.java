package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment;

public class ParagraphMarkdownConverter {
    
    public String convertParagraphToMarkdown(final String docComment) {
        
        return docComment.replace("</p>", "").replace("<p>", "").replace("<br />", "\n");
    }
    
}
