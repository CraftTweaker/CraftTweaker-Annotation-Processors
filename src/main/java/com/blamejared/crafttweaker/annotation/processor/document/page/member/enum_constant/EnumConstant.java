package com.blamejared.crafttweaker.annotation.processor.document.page.member.enum_constant;

import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.meta.IFillMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;

public class EnumConstant implements IFillMeta {
    
    private final String name;
    private final DocumentationComment comment;
    private final boolean isBracket;
    
    public EnumConstant(String name, DocumentationComment comment, boolean isBracket) {
        
        this.name = name;
        this.comment = comment;
        this.isBracket = isBracket;
    }
    
    public String getName() {
        
        return name;
    }
    
    public DocumentationComment getComment() {
        
        return comment;
    }

    public boolean isBracket() {
        return isBracket;
    }

    @Override
    public void fillMeta(DocumentMeta meta) {
        
        meta.addSearchTerms(name);
    }
    
}