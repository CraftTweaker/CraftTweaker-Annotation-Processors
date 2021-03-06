package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.static_member;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.AbstractEnclosedElementConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.member.MemberConverter;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.DocumentationPageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member.DocumentedStaticMembers;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;

public class StaticMemberConverter extends MemberConverter<DocumentedStaticMembers> implements IHasPostCreationCall {
    
    private final DependencyContainer dependencyContainer;
    
    public StaticMemberConverter(DependencyContainer dependencyContainer, Elements elements) {
        
        super(elements);
        this.dependencyContainer = dependencyContainer;
    }
    
    @Override
    protected DocumentedStaticMembers createResultObject(DocumentationPageInfo pageInfo) {
        
        return new DocumentedStaticMembers();
    }
    
    @Override
    protected boolean isCandidate(Element enclosedElement) {
        
        return enclosedElement.getModifiers().contains(Modifier.STATIC);
    }
    
    @Override
    public void afterCreation() {
        
        addElementConverters();
    }
    
    private void addElementConverters() {
        
        addElementConverter(ElementKind.FIELD, StaticFieldConverter.class);
        addElementConverter(ElementKind.ENUM_CONSTANT, StaticFieldConverter.class);
        addElementConverter(ElementKind.METHOD, NamedTypeStaticMethodConverter.class);
    }
    
    private void addElementConverter(ElementKind kind, Class<? extends AbstractEnclosedElementConverter<DocumentedStaticMembers>> converterClass) {
        
        addElementConverter(kind, dependencyContainer.getInstanceOfClass(converterClass));
    }
    
}
