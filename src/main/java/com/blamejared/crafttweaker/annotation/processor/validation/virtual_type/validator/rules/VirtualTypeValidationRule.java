package com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.validator.rules;

import javax.lang.model.element.Element;

public interface VirtualTypeValidationRule {
    
    boolean canValidate(Element enclosedElement);
    
    void validate(Element enclosedElement);
    
}
