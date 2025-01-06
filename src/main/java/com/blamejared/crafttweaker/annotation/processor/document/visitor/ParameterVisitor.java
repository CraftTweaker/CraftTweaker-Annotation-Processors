package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.model.member.Parameter;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.Optional;

public class ParameterVisitor extends SimpleElementVisitor8<Optional<Parameter>, ParameterVisitor.Context> {
    
    public static final ParameterVisitor INSTANCE = new ParameterVisitor();
    
    private ParameterVisitor() {
        
        super(Optional.empty());
    }
    
    @Override
    public Optional<Parameter> visitVariable(VariableElement e, ParameterVisitor.Context context) {
        
        if(e.getKind() == ElementKind.PARAMETER) {
            
            String paramKey = e.getSimpleName().toString();
            String displayName = e.getSimpleName().toString();
            Optional<String> defaultValue = e.asType().accept(OptionalDefaultValueVisitor.INSTANCE, e);
            Optional<Type> paramType = TypeBuildingVisitor.INSTANCE.visit(e.asType(), TypeBuildingVisitor.Context.TYPE_ARGUMENTS);
            return paramType.map(type -> new Parameter(paramKey, displayName, type, defaultValue));
        }
        return super.visitVariable(e, context);
    }
    
    public static class Context {
        
        private final ExecutableElement method;
        
        public Context(ExecutableElement method) {
            
            this.method = method;
        }
        
        
        public ExecutableElement method() {
            
            return method;
        }
        
    }
    
}
