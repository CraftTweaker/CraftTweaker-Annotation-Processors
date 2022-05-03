package com.blamejared.crafttweaker.annotation.processor.validation.parameter;

import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;
import com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules.OptionalTypeValidationRule;
import com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules.OptionalsNeedToGoLastRule;
import com.blamejared.crafttweaker.annotation.processor.validation.parameter.rules.ParameterValidationRule;
import io.toolisticon.aptk.tools.ElementUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

public class ParameterValidator implements IHasPostCreationCall {
    
    private final List<ParameterValidationRule> rules = new ArrayList<>();
    private final DependencyContainer dependencyContainer;
    
    public ParameterValidator(DependencyContainer dependencyContainer) {
        
        this.dependencyContainer = dependencyContainer;
    }
    
    public void validate(Element element) {
        
        assertIsParameter(element);
        validate((VariableElement) element);
    }
    
    private void validate(VariableElement parameter) {
        
        this.rules.stream()
                .filter(rule -> rule.canValidate(parameter))
                .forEach(rule -> rule.validate(parameter));
    }
    
    private void assertIsParameter(Element element) {
        
        assertIsVariableElement(element);
        assertParentIsExecutable(element);
    }
    
    private void assertIsVariableElement(Element element) {
        
        if(!ElementUtils.CheckKindOfElement.isParameter(element)) {
            throw new IllegalArgumentException("Element not a method parameter: " + element);
        }
    }
    
    private void assertParentIsExecutable(Element element) {
        
        if(!(ElementUtils.CheckKindOfElement.isMethod(element.getEnclosingElement()) && !ElementUtils.CheckKindOfElement.isConstructor(element.getEnclosingElement()))) {
            
            throw new IllegalArgumentException("Element not a method: " + element);
        }
    }
    
    @Override
    public void afterCreation() {
        
        addRule(OptionalsNeedToGoLastRule.class);
        addRule(OptionalTypeValidationRule.class);
    }
    
    private void addRule(Class<? extends ParameterValidationRule> ruleClass) {
        
        final ParameterValidationRule rule = dependencyContainer.getInstanceOfClass(ruleClass);
        rules.add(rule);
    }
    
}
