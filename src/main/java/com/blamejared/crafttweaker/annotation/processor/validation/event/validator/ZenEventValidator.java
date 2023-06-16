package com.blamejared.crafttweaker.annotation.processor.validation.event.validator;


import com.blamejared.crafttweaker.annotation.processor.util.dependencies.DependencyContainer;
import com.blamejared.crafttweaker.annotation.processor.util.dependencies.IHasPostCreationCall;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules.CancelableEventRule;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules.ZenEventHasBusRule;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules.ZenEventValidationRule;
import io.toolisticon.aptk.tools.ElementUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ZenEventValidator implements IHasPostCreationCall {
    
    private final List<ZenEventValidationRule> rules = new ArrayList<>();
    private final DependencyContainer dependencyContainer;
    
    public ZenEventValidator(DependencyContainer dependencyContainer) {
        
        this.dependencyContainer = dependencyContainer;
    }
    
    public void validateAll(Collection<? extends Element> elements) {
        
        for(Element element : elements) {
            if(!isTypeElement(element)) {
                throw new IllegalArgumentException("Invalid typeElement annotated: " + element);
            }
            validate((TypeElement) element);
        }
    }
    
    private boolean isTypeElement(Element element) {
        
        return ElementUtils.CheckKindOfElement.isClass(element) || ElementUtils.CheckKindOfElement.isEnum(element) || ElementUtils.CheckKindOfElement.isOfKind(element, ElementKind.RECORD) || ElementUtils.CheckKindOfElement.isInterface(element);
    }
    
    private void validate(TypeElement element) {
        
        for(ZenEventValidationRule rule : rules) {
            if(rule.canValidate(element)) {
                rule.validate(element);
            }
        }
    }
    
    @Override
    public void afterCreation() {
        
        addRule(ZenEventHasBusRule.class);
        addRule(CancelableEventRule.class);
    }
    
    public void addRule(Class<? extends ZenEventValidationRule> ruleClass) {
        
        final ZenEventValidationRule rule = dependencyContainer.getInstanceOfClass(ruleClass);
        rules.add(rule);
    }
    
}
