package com.blamejared.crafttweaker.annotation.processor.document.page.member;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.CommentMerger;
import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.meta.IFillMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

public class PropertyMember implements IFillMeta {
    
    private final AbstractTypeInfo type;
    private final String name;
    private final boolean hasGetter;
    private final boolean hasSetter;
    private final DocumentationComment comment;
    
    public PropertyMember(String name, AbstractTypeInfo type, boolean hasGetter, boolean hasSetter, DocumentationComment comment) {
        
        this.hasGetter = hasGetter;
        this.hasSetter = hasSetter;
        this.type = type;
        this.name = name;
        this.comment = comment;
    }
    
    public static PropertyMember merge(PropertyMember left, PropertyMember right) {
        
        if(!left.name.equals(right.name)) {
            throw new IllegalArgumentException("Trying to merge different names!");
        }
        
        final boolean hasGetter = left.hasGetter || right.hasGetter;
        final boolean hasSetter = left.hasSetter || right.hasSetter;
        
        CommentMerger commentMerger = new CommentMerger();
        DocumentationComment merged = commentMerger.merge(left.comment, right.comment);
        
        return new PropertyMember(left.name, left.type, hasGetter, hasSetter, merged);
    }
    
    public void writeTableRow(PageOutputWriter writer) {
        
        writer.currentTable()
                .rowEntry("Name", name)
                .rowEntry("Type", type.getClickableMarkdown())
                .rowEntry("Has Getter", hasGetter)
                .rowEntry("Has Setter", hasSetter)
                .rowEntry("Description", comment.getMarkdownDescription())
                .endRow();
    }
    
    public String getName() {
        
        return name;
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        meta.addSearchTerms(name);
    }
    
}
