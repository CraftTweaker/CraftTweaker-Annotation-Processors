package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.OperatorTypeParameterCountProvider;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

public class OperatorParameterCountValidationRule implements ExpansionInfoValidationRule {
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return enclosedElement.getAnnotation(ZenCodeType.Operator.class) != null;
    }
    
    @Override
    public void validate(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        if(parameterCountInvalid(enclosedElement)) {
            final int expectedNumberOfParameters = getExpectedNumberOfParameters(enclosedElement);
            final String msg = String.format("This operator requires %s parameters", expectedNumberOfParameters);
            MessagerUtils.error(enclosedElement, msg);
        }
    }
    
    private boolean parameterCountInvalid(Element enclosedElement) {
        
        final int expectedNumberOfParameters = getExpectedNumberOfParameters(enclosedElement);
        final int actualNumberOfParameters = getActualNumberOfParameters(enclosedElement);
        return actualNumberOfParameters != expectedNumberOfParameters;
    }
    
    private int getExpectedNumberOfParameters(Element enclosedElement) {
        
        final ZenCodeType.Operator annotation = enclosedElement.getAnnotation(ZenCodeType.Operator.class);
        final ZenCodeType.OperatorType operatorType = annotation.value();
        return 1 + OperatorTypeParameterCountProvider.getParameterCountFor(operatorType);
    }
    
    private int getActualNumberOfParameters(Element enclosedElement) {
        
        if(enclosedElement.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException("Invalid type annotated");
        }
        return ((ExecutableElement) enclosedElement).getParameters().size();
    }
    
}
