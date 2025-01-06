package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.OperatorTypeParameterCountProvider;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import org.openzen.zencode.java.OperatorWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Optional;

public class OperatorParameterCountValidationRule implements VirtualTypeValidationRule {
    
    @Override
    public boolean canValidate(Element enclosedElement) {
        
        return OperatorWrapper.isAnnotated(enclosedElement);
    }
    
    @Override
    public void validate(Element enclosedElement) {
        
        final int actualNumberOfParameters = getActualNumberOfParameters(enclosedElement);
        final ZenCodeType.OperatorType operatorType = getOperatorType(enclosedElement);
        final Optional<String> validationMessage = OperatorTypeParameterCountProvider.validateParameterCount(operatorType, actualNumberOfParameters);
        
        validationMessage.ifPresent(msg -> MessagerUtils.error(enclosedElement, msg));
    }
    
    private ZenCodeType.OperatorType getOperatorType(Element enclosedElement) {
        
        final OperatorWrapper operator = OperatorWrapper.wrap(enclosedElement);
        if(operator == null) {
            throw new IllegalArgumentException("Element " + enclosedElement + " does not have an @Operator annotation!");
        }
        return operator.value();
    }
    
    private int getActualNumberOfParameters(Element enclosedElement) {
        
        if(!ElementUtils.CheckKindOfElement.isMethod(enclosedElement)) {
            throw new IllegalArgumentException("Invalid type annotated");
        }
        return ((ExecutableElement) enclosedElement).getParameters().size();
    }
    
}
