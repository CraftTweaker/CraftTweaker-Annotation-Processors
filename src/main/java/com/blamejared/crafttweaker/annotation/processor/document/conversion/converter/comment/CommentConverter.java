package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.event.EventDataConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.example.ExampleDataConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta.DeprecationConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta.MetaConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta.ObtentionConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta.SinceInformationConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.header.ParameterDescriptionConverter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.MetaData;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.DocumentationPageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.ExampleData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import java.util.Optional;

public class CommentConverter {
    
    private final ProcessingEnvironment processingEnv;
    private final CommentMerger commentMerger;
    private final ExampleDataConverter exampleDataConverter;
    private final MetaConverter metaDataConverter;
    private final DescriptionConverter descriptionConverter;
    private final DeprecationConverter deprecationConverter;
    private final SinceInformationConverter sinceConverter;
    private final ObtentionConverter obtentionConverter;
    
    private final ParameterDescriptionConverter parameterDescriptionConverter;
    private final EventDataConverter eventDataConverter;
    
    public CommentConverter(final ProcessingEnvironment processingEnv, final CommentMerger commentMerger,
                            final ExampleDataConverter exampleDataConverter, final MetaConverter metaConverter,
                            final DescriptionConverter descriptionConverter,
                            final ParameterDescriptionConverter parameterDescriptionConverter,
                            final EventDataConverter eventDataConverter, final DeprecationConverter deprecationConverter,
                            final SinceInformationConverter sinceConverter, final ObtentionConverter obtentionConverter) {
        
        this.processingEnv = processingEnv;
        this.commentMerger = commentMerger;
        this.exampleDataConverter = exampleDataConverter;
        this.metaDataConverter = metaConverter;
        this.descriptionConverter = descriptionConverter;
        this.parameterDescriptionConverter = parameterDescriptionConverter;
        this.eventDataConverter = eventDataConverter;
        this.deprecationConverter = deprecationConverter;
        this.sinceConverter = sinceConverter;
        this.obtentionConverter = obtentionConverter;
    }
    
    public DocumentationComment convertForType(TypeElement typeElement) {
        
        DocumentationComment documentationComment = convertElement(typeElement);
        if(typeElement.getSimpleName().toString().endsWith("Event")) {
            return fastMergeComments(documentationComment, convertEvent(typeElement));
        }
        return documentationComment;
    }
    
    public DocumentationComment convertForConstructor(ExecutableElement constructor, DocumentationPageInfo pageInfo) {
        
        final DocumentationComment comment = convertElement(constructor, pageInfo.getClassComment());
        fillExampleForThisParameterFromPageInfo(comment, pageInfo);
        return comment;
    }
    
    public DocumentationComment convertForMethod(ExecutableElement method, DocumentationPageInfo pageInfo) {
        
        final DocumentationComment comment = convertElement(method, pageInfo.getClassComment());
        return fillExampleForThisParameterFromPageInfo(comment, pageInfo);
    }
    
    public DocumentationComment convertForParameter(VariableElement variableElement) {
        
        final DocumentationComment parameterDescription = convertParameterDescription(variableElement);
        final DocumentationComment comment = convertElement(variableElement.getEnclosingElement());
        return mergeComments(parameterDescription, comment);
    }
    
    private DocumentationComment convertParameterDescription(Element element) {
        
        return parameterDescriptionConverter.convertDescriptionOf(element);
    }
    
    public DocumentationComment convertForTypeParameter(TypeParameterElement typeParameterElement) {
        
        final DocumentationComment parameterDescription = convertParameterDescription(typeParameterElement);
        final DocumentationComment comment = convertElement(typeParameterElement.getEnclosingElement());
        return mergeComments(parameterDescription, comment);
    }
    
    private DocumentationComment convertElement(Element element) {
        
        return convertElement(element, DocumentationComment.empty());
    }
    
    public DocumentationComment convertElement(Element element, DocumentationComment parent) {
        
        DocumentationComment comment = getCommentForElement(element);
        comment = mergeComments(comment, parent);
        
        final DocumentationComment enclosingElementComment = getCommentFromEnclosingElement(element);
        return mergeComments(comment, enclosingElementComment);
    }
    
    private DocumentationComment convertEvent(Element element) {
        
        return eventDataConverter.getDocumentationComment(processingEnv.getElementUtils()
                .getDocComment(element), element);
    }
    
    @Nonnull
    private DocumentationComment getCommentForElement(Element element) {
        
        final String docComment = processingEnv.getElementUtils().getDocComment(element);
        final String description = extractDescriptionFrom(docComment, element).orElse(null);
        // TODO: Handle @apiNote
        final String deprecation = extractDeprecationFrom(docComment, element).orElse(null);
        final String sinceVersion = extractSinceFrom(docComment, element).orElse(null);
        final String obtention = extractObtentionFrom(docComment, element).orElse(null);
        final ExampleData exampleData = extractExampleDataFrom(docComment, element).orElseGet(ExampleData::empty);
        final MetaData metaData = extractMetaDataFrom(docComment, element).orElseGet(MetaData::empty);
        return new DocumentationComment(description, deprecation, sinceVersion, obtention, exampleData, metaData);
    }
    
    private Optional<String> extractDescriptionFrom(@Nullable String docComment, Element element) {
        
        return descriptionConverter.convertFromCommentString(docComment, element);
    }
    
    private Optional<String> extractDeprecationFrom(@Nullable final String docComment, final Element element) {
        
        return this.deprecationConverter.fromComment(docComment, element);
    }
    
    private Optional<String> extractSinceFrom(@Nullable final String docComment, final Element element) {
        
        return this.sinceConverter.fromComment(docComment, element);
    }
    
    private Optional<String> extractObtentionFrom(@Nullable final String docComment, final Element element) {
        
        return this.obtentionConverter.fromComment(docComment, element);
    }
    
    private Optional<ExampleData> extractExampleDataFrom(String docComment, Element element) {
        
        return exampleDataConverter.fromComment(docComment, element);
    }
    
    private Optional<MetaData> extractMetaDataFrom(String docComment, Element element) {
        
        return metaDataConverter.fromComment(docComment, element);
    }
    
    private DocumentationComment getCommentFromEnclosingElement(Element element) {
        
        final Element enclosingElement = element.getEnclosingElement();
        if(enclosingElement == null) {
            return DocumentationComment.empty();
        }
        
        return convertElement(enclosingElement);
    }
    
    private DocumentationComment mergeComments(DocumentationComment comment, DocumentationComment enclosingElementComment) {
        
        return commentMerger.merge(comment, enclosingElementComment);
    }
    
    private DocumentationComment fastMergeComments(DocumentationComment firstWithExamples, DocumentationComment second) {
        
        return fastMergeWithDescription(
                firstWithExamples,
                second,
                firstWithExamples.getOptionalDescription().map(it -> it + "\n\n").orElse("") + second.getDescription()
        );
    }
    
    @SuppressWarnings("unused") // second may be used later
    private DocumentationComment fastMergeWithDescription(final DocumentationComment first, final DocumentationComment second, final String description) {
        
        return new DocumentationComment(description, first.getDeprecationMessage(), first.getSinceVersion(), first.getObtention(), first.getExamples(), first.getMetaData());
    }
    
    private DocumentationComment fillExampleForThisParameterFromPageInfo(DocumentationComment comment, DocumentationPageInfo pageInfo) {
        
        final DocumentationComment classComment = pageInfo.getClassComment();
        return mergeComments(comment, classComment);
    }
    
}
