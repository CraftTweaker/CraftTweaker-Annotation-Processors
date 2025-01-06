package com.blamejared.crafttweaker.annotation.processor.validation.expansion.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.validation.expansion.info.ExpansionInfo;
import io.toolisticon.aptk.tools.MessagerUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ModifierValidationRule extends AbstractGeneralValidationRule {
    
    private final Set<Modifier> requiredModifiers = new HashSet<>();
    
    public ModifierValidationRule() {
        
        fillRequiredModifiers();
    }
    
    @Override
    public void validate(Element enclosedElement, ExpansionInfo expansionInfo) {
        
        if(hasIncorrectModifiers(enclosedElement)) {
            MessagerUtils.error(enclosedElement, "Expansion members need to be public and static");
        }
    }
    
    private boolean hasIncorrectModifiers(Element enclosedElement) {
        
        return !enclosedElement.getModifiers().containsAll(requiredModifiers);
    }
    
    private void fillRequiredModifiers() {
        
        this.requiredModifiers.add(Modifier.PUBLIC);
        this.requiredModifiers.add(Modifier.STATIC);
    }
    
}
