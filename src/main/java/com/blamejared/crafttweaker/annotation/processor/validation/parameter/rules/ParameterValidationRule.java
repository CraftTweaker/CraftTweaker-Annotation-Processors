package com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules;

import javax.lang.model.element.VariableElement;

public interface ParameterValidationRule {
    
    boolean canValidate(VariableElement parameter);
    
    void validate(VariableElement parameter);
    
}
