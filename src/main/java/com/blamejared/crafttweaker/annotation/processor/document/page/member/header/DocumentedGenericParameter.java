package com.blamejared.crafttweaker.annotation.processor.document.page.member.header;

import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.file.Table;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentedGenericParameter implements Comparable<DocumentedGenericParameter> {
    
    
    private final String name;
    
    private final DocumentationComment comment;
    private final List<AbstractTypeInfo> bounds;
    
    public DocumentedGenericParameter(List<AbstractTypeInfo> bounds, String name, DocumentationComment comment) {
        
        this.bounds = bounds;
        this.name = name;
        this.comment = comment;
    }
    
    public String formatForSignatureExample() {
        
        if(bounds.isEmpty()) {
            return name;
        }
        
        final String prefix = name + " : ";
        return prefix + getDisplayNameBounds();
    }
    
    @NotNull
    private String getDisplayNameBounds() {
        
        return bounds.stream()
                .map(AbstractTypeInfo::getDisplayName)
                .collect(Collectors.joining(", "));
    }
    
    @NotNull
    private String getClickableMarkdownBounds() {
        
        return bounds.stream()
                .map(AbstractTypeInfo::getClickableMarkdown)
                .collect(Collectors.joining(", "));
    }
    
    private String getExampleName() {
        
        return "<" + name + ">";
    }
    
    public int numberOfExamples() {
        
        return comment.numberOfExamplesFor(getExampleName());
    }
    
    public String getDescription() {
        
        return comment.getMarkdownDescription();
    }
    
    public String getExample(int exampleIndex) {
        
        return comment.getExamples().getExampleFor(getExampleName()).getTextValue(exampleIndex);
    }
    
    public void writeParameterInfoIncludeOptionality(PageOutputWriter writer) {
    
        writeParameterInfo(writer).endRow();
    }
    
    public void writeParameterInfoExcludeOptionality(PageOutputWriter writer) {
        
        
        writeParameterInfo(writer).endRow();
    }
    
    private Table writeParameterInfo(PageOutputWriter writer) {
        
        return writer.currentTable()
                .rowEntry("Parameter", name)
                .rowEntry("Type", getClickableMarkdownBounds())
                .rowEntry("Description", getDescription());
    }
    
    @Override
    public int compareTo(@Nonnull DocumentedGenericParameter other) {
        
        int temp = this.name.compareTo(other.name);
        if(temp != 0) {
            return temp;
        }
        
        temp = this.bounds.size() - other.bounds.size();
        if(temp != 0) {
            return temp;
        }
        
        for(int i = 0; i < this.bounds.size(); i++) {
            temp = this.bounds.get(i).compareTo(other.bounds.get(i));
            if(temp != 0) {
                return temp;
            }
        }
        
        return 0;
    }
    
}
