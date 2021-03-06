package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment;

import com.blamejared.crafttweaker.annotation.processor.document.meta.MetaData;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.Example;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.ExampleData;

import java.util.HashMap;
import java.util.Map;

public class CommentMerger {
    
    public DocumentationComment merge(DocumentationComment childComment, DocumentationComment parentComment) {
        
        final String description = mergeDescription(childComment);
        final String deprecation = mergeDeprecation(childComment, parentComment);
        final String sinceVersion = mergeSince(childComment, parentComment);
        final String obtention = mergeObtention(childComment, parentComment);
        final ExampleData exampleData = mergeCommentExamples(childComment, parentComment);
        // We don't care about the parent description.
        final MetaData metaData = childComment.getMetaData();
        
        return new DocumentationComment(description, deprecation, sinceVersion, obtention, exampleData, metaData);
    }
    
    private String mergeDescription(DocumentationComment childComment) {
        
        return childComment.getOptionalDescription().orElse(null);
    }
    
    private String mergeDeprecation(final DocumentationComment child, final DocumentationComment parent) {
        // TODO("Does it make sense to mark the child as deprecated if the parent is?")
        return child.getOptionalDeprecationMessage().orElseGet(parent::getDeprecationMessage);
    }
    
    private String mergeSince(final DocumentationComment child, final DocumentationComment parent) {
        
        return child.getOptionalSince().orElseGet(parent::getSinceVersion);
    }
    
    private String mergeObtention(final DocumentationComment child, final DocumentationComment parent) {
        
        return child.getOptionalObtention().orElseGet(parent::getObtention);
    }
    
    private ExampleData mergeCommentExamples(DocumentationComment childComment, DocumentationComment parentComment) {
        
        final ExampleData childExamples = childComment.getExamples();
        final ExampleData parentExamples = parentComment.getExamples();
        
        return mergeExampleData(childExamples, parentExamples);
    }
    
    private ExampleData mergeExampleData(ExampleData childExamples, ExampleData parentExamples) {
        
        final Map<String, Example> childExampleMap = childExamples.getExampleMap();
        final Map<String, Example> parentExampleMap = parentExamples.getExampleMap();
        final HashMap<String, Example> resultExampleMap = mergeExampleDataMap(childExampleMap, parentExampleMap);
        return new ExampleData(resultExampleMap);
    }
    
    private HashMap<String, Example> mergeExampleDataMap(Map<String, Example> childExampleMap, Map<String, Example> parentExampleMap) {
        
        final HashMap<String, Example> result = new HashMap<>(parentExampleMap);
        result.putAll(childExampleMap);
        
        return result;
    }
    
}
