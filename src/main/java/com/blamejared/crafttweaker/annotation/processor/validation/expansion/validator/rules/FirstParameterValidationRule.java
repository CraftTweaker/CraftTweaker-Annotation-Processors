package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.List;

public class FirstParameterValidationRule extends AbstractGeneralValidationRule {
    
    private final Messager messager;
    private final Types typeUtils;
    
    public FirstParameterValidationRule(Messager messager, Types typeUtils) {
        
        this.messager = messager;
        this.typeUtils = typeUtils;
    }
    
    @Override
    public void validate(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        if(!firstParameterIsExpandedType(enclosedElement, expansionInfo)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Element is not of the expanded type", enclosedElement);
        }
    }
    
    private boolean firstParameterIsExpandedType(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        return enclosedElement.getKind() == ElementKind.METHOD && firstParameterIsExpandedType((ExecutableElement) enclosedElement, expansionInfo);
    }
    
    private boolean firstParameterIsExpandedType(ExecutableElement method, ExpansionInfo expansionInfo) {
        
        final List<? extends VariableElement> parameters = method.getParameters();
        if(parameters.isEmpty()) {
            return false;
        }
        
        final TypeMirror expandedType = expansionInfo.expandedType();
        final TypeMirror methodParameter = parameters.get(0).asType();
        return isSecondParameterSubTypeOfFirst(expandedType, methodParameter);
    }
    
    private boolean isSecondParameterSubTypeOfFirst(TypeMirror expandedType, TypeMirror methodParameter) {
        
        // This gets rid of the explicit type params, Foo<Bar> gets turned into Foo
        TypeElement expanded = TypeUtils.TypeRetrieval.getTypeElement(expandedType);
        TypeElement parameter = TypeUtils.TypeRetrieval.getTypeElement(methodParameter);
        if(expanded == null || parameter == null) {
            return typeUtils.isSubtype(expandedType, methodParameter);
        }
        
        return TypeUtils.TypeComparison.isAssignableTo(expanded, parameter);
    }
    
}
