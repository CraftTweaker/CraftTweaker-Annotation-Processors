package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator;


import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.converter.ExpansionInfoConverter;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules.*;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class ExpansionInfoValidator implements IHasPostCreationCall {
    
    private final List<ExpansionInfoValidationRule> rules = new ArrayList<>();
    
    private final ExpansionInfoConverter converter;
    private final DependencyContainer dependencyContainer;
    
    public ExpansionInfoValidator(ExpansionInfoConverter converter, DependencyContainer dependencyContainer) {
        
        this.converter = converter;
        this.dependencyContainer = dependencyContainer;
    }
    
    public void validateAll() {
        
        converter.convertToExpansionInfos().forEach(this::validate);
    }
    
    private void validate(ExpansionInfo expansionInfo) {
        
        for(Element enclosedElement : expansionInfo.expansionType().getEnclosedElements()) {
            validateEnclosedElement(enclosedElement, expansionInfo);
        }
    }
    
    private void validateEnclosedElement(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        rules.stream()
                .filter(rule -> rule.canValidate(enclosedElement))
                .forEach(rule -> rule.validate(enclosedElement, expansionInfo));
    }
    
    @Override
    public void afterCreation() {
        
        addRule(ModifierValidationRule.class);
        addRule(FirstParameterValidationRule.class);
        addRule(ParameterCountValidationRule.class);
        addRule(OperatorParameterCountValidationRule.class);
        addRule(UnsupportedAnnotationValidationRule.class);
    }
    
    private void addRule(Class<? extends ExpansionInfoValidationRule> validationRuleClass) {
        
        final ExpansionInfoValidationRule rule = dependencyContainer.getInstanceOfClass(validationRuleClass);
        this.rules.add(rule);
    }
    
}
