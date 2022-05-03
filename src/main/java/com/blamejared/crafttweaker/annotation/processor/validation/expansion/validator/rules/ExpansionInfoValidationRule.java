package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;


import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;

import javax.lang.model.element.Element;

public interface ExpansionInfoValidationRule {
    
    boolean canValidate(Element enclosedElement);
    
    void validate(Element enclosedElement, ExpansionInfo expansionInfo);
    
}
