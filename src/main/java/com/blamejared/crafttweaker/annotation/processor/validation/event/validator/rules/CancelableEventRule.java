package com.blamejared.crafttweaker.annotation.processor.validation.event.validator.rules;

import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.visitors.CancelableTreeVisitor;
import com.blamejared.crafttweaker.api.event.BusCarrierWrapper;
import com.blamejared.crafttweaker.api.event.BusWrapper;
import com.blamejared.crafttweaker.api.event.ZenEventWrapper;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.ProcessingEnvironmentUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;
import net.minecraftforge.eventbus.api.CancelableWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Collection;

public class CancelableEventRule implements ZenEventValidationRule {
    
    @Override
    public boolean canValidate(TypeElement enclosedElement) {
        
        return (ZenEventWrapper.isAnnotated(enclosedElement) || BusCarrierWrapper.isAnnotated(enclosedElement))
                && ElementUtils.AccessEnclosedElements.getEnclosedFields(enclosedElement)
                .stream()
                .filter(BusWrapper::isAnnotated)
                .map(VariableElement::asType)
                .map(TypeMirrorWrapper::getTypeArguments)
                .flatMap(Collection::stream)
                .map(TypeUtils.TypeRetrieval::getTypeElement)
                .anyMatch(CancelableWrapper::isAnnotated);
    }
    
    @Override
    public void validate(TypeElement enclosedElement) {
        
        if(!isCancelable(enclosedElement)) {
            MessagerUtils.error(enclosedElement, "@Cancelable event is not implemented as cancelable! Use 'IEventBus.cancelable' instead!");
        }
    }
    
    public static boolean isCancelable(TypeElement element) {
        
        return ElementUtils.AccessEnclosedElements.getEnclosedFields(element)
                .stream()
                .filter(BusWrapper::isAnnotated)
                .anyMatch(variableElement -> Tools.TREES.apply(ProcessingEnvironmentUtils.getProcessingEnvironment())
                        .getTree(variableElement)
                        .accept(CancelableTreeVisitor.INSTANCE, null));
    }
    
}
