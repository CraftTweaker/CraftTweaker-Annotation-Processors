package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.expansion;

import com.blamejared.crafttweaker.annotation.processor.document.DocumentRegistry;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.DocumentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.CommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.expansion.member.ExpansionStaticMethodConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.expansion.member.ExpansionVirtualMemberConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.static_member.StaticMemberConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.type.TypeConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.mods.KnownModList;
import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.DocumentationPageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypeName;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypePageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.Example;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.ExampleData;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member.DocumentedStaticMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member.DocumentedVirtualMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.page.DocumentationPage;
import com.blamejared.crafttweaker.annotation.processor.document.page.page.ExpansionPage;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.GenericTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.TypePageTypeInfo;
import com.blamejared.crafttweaker_annotations.annotations.TypedExpansionWrapper;
import org.openzen.zencode.java.ExpansionWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.TypeElement;
import java.util.Optional;

public class ExpansionConverter extends DocumentConverter {
    
    private final StaticMemberConverter staticMemberConverter;
    private final ExpansionVirtualMemberConverter virtualMemberConverter;
    private final TypeConverter typeConverter;
    private final DocumentRegistry documentRegistry;
    private final ExpansionStaticMethodConverter expansionStaticMethodConverter;
    
    public ExpansionConverter(KnownModList knownModList, CommentConverter commentConverter, DocumentRegistry documentRegistry, TypeConverter typeConverter, StaticMemberConverter staticMemberConverter, ExpansionVirtualMemberConverter virtualMemberConverter, ExpansionStaticMethodConverter expansionStaticMethodConverter) {
        
        super(knownModList, commentConverter);
        this.staticMemberConverter = staticMemberConverter;
        this.virtualMemberConverter = virtualMemberConverter;
        this.typeConverter = typeConverter;
        this.documentRegistry = documentRegistry;
        this.expansionStaticMethodConverter = expansionStaticMethodConverter;
    }
    
    @Override
    public boolean canConvert(TypeElement typeElement) {
        
        return ExpansionWrapper.isAnnotated(typeElement) || TypedExpansionWrapper.isAnnotated(typeElement);
    }
    
    @Override
    public DocumentationPage convert(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        final AbstractTypeInfo expandedType = getExpandedType(typeElement);
        
        final DocumentedVirtualMembers virtualMembers = getVirtualMembers(typeElement, expandedType);
        final DocumentedStaticMembers staticMembers = getStaticMembers(typeElement, pageInfo);
        expansionStaticMethodConverter.convertAndAddTo(typeElement, staticMembers, pageInfo, expandedType);
        
        return new ExpansionPage(expandedType, pageInfo, virtualMembers, staticMembers);
    }
    
    private AbstractTypeInfo getExpandedType(TypeElement typeElement) {
        
        if(ExpansionWrapper.isAnnotated(typeElement)) {
            final ZenCodeType.Expansion expansion = typeElement.getAnnotation(ZenCodeType.Expansion.class);
            
            final TypeName expandedName = new TypeName(expansion.value());
            return typeConverter.convertByName(expandedName);
        }
        TypedExpansionWrapper typedExpansion = TypedExpansionWrapper.wrap(typeElement);
        if(typedExpansion != null) {
            return typeConverter.convertType(typedExpansion.valueAsTypeMirror());
        }
        throw new IllegalArgumentException("Type element: '" + typedExpansion + "' is not annotated with any known expansion!");
    }
    
    private DocumentedVirtualMembers getVirtualMembers(TypeElement typeElement, AbstractTypeInfo expandedType) {
        
        final DocumentationPageInfo pageInfo = getPageInfoForType(expandedType);
        return virtualMemberConverter.convertFor(typeElement, pageInfo);
    }
    
    private TypePageInfo getPageInfoForType(AbstractTypeInfo expandedType) {
        
        if(expandedType instanceof TypePageTypeInfo) {
            return getPageInfo((TypePageTypeInfo) expandedType);
        }
        if(expandedType instanceof GenericTypeInfo) {
            return getPageInfoForType(((GenericTypeInfo) expandedType).getBaseClass());
        }
        throw new IllegalArgumentException("Invalid expanded type! " + expandedType + " " + expandedType.getClass());
    }
    
    private TypePageInfo getPageInfo(TypePageTypeInfo expandedType) {
        
        final TypeName zenCodeName = expandedType.getZenCodeName();
        final Optional<TypePageInfo> pageInfoByName = documentRegistry.getPageInfoByName(zenCodeName);
        return pageInfoByName.orElseGet(() -> new DummyTypePageInfo(zenCodeName));
    }
    
    private DocumentedStaticMembers getStaticMembers(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        return staticMemberConverter.convertFor(typeElement, pageInfo);
    }
    
    @Override
    public void setDocumentationCommentTo(TypeElement typeElement, DocumentationPageInfo pageInfo) {
        
        super.setDocumentationCommentTo(typeElement, pageInfo);
    }
    
    @Override
    protected Example getFallbackThisInformationFor(TypeElement typeElement) {
        
        return getExpandedTypeThisExample(typeElement);
    }
    
    private Example getExpandedTypeThisExample(TypeElement typeElement) {
        
        final TypePageInfo pageInfoForType = getPageInfoForType(getExpandedType(typeElement));
        final DocumentationComment classComment = pageInfoForType.getClassComment();
        ExampleData examples = classComment.getExamples();
        return examples.tryGetExampleFor("this").orElseGet(() -> new Example("my" + pageInfoForType.getSimpleName()));
    }
    
    public static class DummyTypePageInfo extends TypePageInfo {
        
        public DummyTypePageInfo(TypeName zenCodeName) {
            
            super("crafttweaker", "", zenCodeName);
        }
        
        @Override
        public boolean shouldOutput() {
            
            return false;
        }
        
    }
    
}