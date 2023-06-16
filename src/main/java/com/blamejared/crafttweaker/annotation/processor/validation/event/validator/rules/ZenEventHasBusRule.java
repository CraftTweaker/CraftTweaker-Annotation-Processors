package com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules;

import com.blamejared.crafttweaker.api.event.BusCarrierWrapper;
import com.blamejared.crafttweaker.api.event.BusWrapper;
import com.blamejared.crafttweaker.api.event.ZenEventWrapper;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.MessagerUtils;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Set;
import java.util.stream.Collectors;

public class ZenEventHasBusRule implements ZenEventValidationRule {
    
    @Override
    public boolean canValidate(TypeElement enclosedElement) {
        
        return ZenEventWrapper.isAnnotated(enclosedElement) || BusCarrierWrapper.isAnnotated(enclosedElement);
    }
    
    @Override
    public void validate(TypeElement enclosedElement) {
        
        Set<VariableElement> buses = ElementUtils.AccessEnclosedElements.getEnclosedFields(enclosedElement)
                .stream()
                .filter(BusWrapper::isAnnotated)
                .collect(Collectors.toSet());
        if(buses.isEmpty()) {
            MessagerUtils.error(enclosedElement, "Expected a single '@ZenEvent.Bus' annotation but none was found!");
        } else if(buses.size() > 1) {
            MessagerUtils.error(enclosedElement, "Expected a single '@ZenEvent.Bus' annotation but {0} were found!", buses.size());
        }
    }
    
}
