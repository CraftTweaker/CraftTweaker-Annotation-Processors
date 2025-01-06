package com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules;

import javax.lang.model.element.TypeElement;

public interface ZenEventValidationRule {
    
    boolean canValidate(TypeElement enclosedElement);
    
    void validate(TypeElement enclosedElement);
    
}
