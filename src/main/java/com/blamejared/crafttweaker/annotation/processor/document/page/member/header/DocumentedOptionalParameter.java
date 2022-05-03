package com.blamejared.crafttweaker.annotation.processor.document.page.member.header;


import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import javax.annotation.Nullable;

public class DocumentedOptionalParameter extends DocumentedParameter {
    
    @Nullable
    private final String defaultValue;
    
    public DocumentedOptionalParameter(String name, AbstractTypeInfo type, DocumentationComment comment, @Nullable String defaultValue) {
        
        super(name, type, comment);
        this.defaultValue = defaultValue;
    }
    
    @Override
    public void writeParameterInfoIncludeOptionality(PageOutputWriter writer) {
        
        writer.printf("| %s | %s | %s | %s | %s |%n", name, type.getClickableMarkdown(), getDescription(), true, getDefaultValue());
    }
    
    public String getDefaultValue() {
        
        return defaultValue == null ? "Error: None given" : defaultValue;
    }
    
    @Override
    public boolean isOptional() {
        
        return true;
    }
    
}
