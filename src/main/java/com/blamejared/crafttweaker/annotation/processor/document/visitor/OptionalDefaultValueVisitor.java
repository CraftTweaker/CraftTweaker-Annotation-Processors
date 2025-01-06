package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.OptionalBooleanWrapper;
import org.openzen.zencode.java.OptionalCharWrapper;
import org.openzen.zencode.java.OptionalDoubleWrapper;
import org.openzen.zencode.java.OptionalFloatWrapper;
import org.openzen.zencode.java.OptionalIntWrapper;
import org.openzen.zencode.java.OptionalLongWrapper;
import org.openzen.zencode.java.OptionalStringWrapper;
import org.openzen.zencode.java.OptionalWrapper;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.TypeKindVisitor6;
import java.util.Optional;

public class OptionalDefaultValueVisitor implements TypeVisitor<Optional<String>, VariableElement> {
    
    public static OptionalDefaultValueVisitor INSTANCE = new OptionalDefaultValueVisitor();
    
    private OptionalDefaultValueVisitor() {}
    
    @Override
    public Optional<String> visit(TypeMirror t, VariableElement variableElement) {
        
        return t.accept(this, variableElement);
    }
    
    @Override
    public Optional<String> visit(TypeMirror t) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitPrimitive(PrimitiveType t, VariableElement variableElement) {
        
        return t.accept(PrimitiveDefaultValueVisitor.INSTANCE, variableElement);
    }
    
    @Override
    public Optional<String> visitNull(NullType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitArray(ArrayType t, VariableElement variableElement) {
        
        OptionalWrapper optWrapper = OptionalWrapper.wrap(variableElement);
        if(optWrapper != null) {
            if(optWrapper.valueIsDefaultValue()) {
                return Optional.of("[]");
            } else {
                return Optional.of(optWrapper.value());
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitDeclared(DeclaredType t, VariableElement variableElement) {
        
        OptionalWrapper optWrapper = OptionalWrapper.wrap(variableElement);
        if(optWrapper != null) {
            if(optWrapper.valueIsDefaultValue()) {
                return Optional.of("null");
            } else {
                return Optional.of(optWrapper.value());
            }
        }
        if(TypeUtils.TypeComparison.isTypeEqual(t, String.class)) {
            OptionalStringWrapper optStringWrapper = OptionalStringWrapper.wrap(variableElement);
            if(optStringWrapper != null) {
                return Optional.of(optStringWrapper.value());
            }
        }
        try {
            PrimitiveType primitiveType = TypeUtils.getTypes().unboxedType(t);
            Optional<String> accept = primitiveType.accept(this, variableElement);
            if(accept.isPresent()) {
                return accept;
            }
        } catch(IllegalArgumentException ignored) {
        
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitError(ErrorType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitTypeVariable(TypeVariable t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitWildcard(WildcardType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitExecutable(ExecutableType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitNoType(NoType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitUnknown(TypeMirror t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitUnion(UnionType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<String> visitIntersection(IntersectionType t, VariableElement variableElement) {
        
        return Optional.empty();
    }
    
    private static class PrimitiveDefaultValueVisitor extends TypeKindVisitor6<Optional<String>, VariableElement> {
        
        public static PrimitiveDefaultValueVisitor INSTANCE = new PrimitiveDefaultValueVisitor();
        
        private PrimitiveDefaultValueVisitor() {}
        
        @Override
        protected Optional<String> defaultAction(TypeMirror e, VariableElement variableElement) {
            
            return Optional.empty();
        }
        
        @Override
        public Optional<String> visitPrimitiveAsBoolean(PrimitiveType t, VariableElement variableElement) {
            
            OptionalBooleanWrapper wrapper = OptionalBooleanWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsBoolean(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsByte(PrimitiveType t, VariableElement variableElement) {
            
            OptionalIntWrapper wrapper = OptionalIntWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsByte(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsShort(PrimitiveType t, VariableElement variableElement) {
            
            OptionalIntWrapper wrapper = OptionalIntWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsShort(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsInt(PrimitiveType t, VariableElement variableElement) {
            
            OptionalIntWrapper wrapper = OptionalIntWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsInt(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsLong(PrimitiveType t, VariableElement variableElement) {
            
            OptionalLongWrapper wrapper = OptionalLongWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsLong(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsChar(PrimitiveType t, VariableElement variableElement) {
            
            OptionalCharWrapper wrapper = OptionalCharWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsChar(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsFloat(PrimitiveType t, VariableElement variableElement) {
            
            OptionalFloatWrapper wrapper = OptionalFloatWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            
            return super.visitPrimitiveAsFloat(t, variableElement);
        }
        
        @Override
        public Optional<String> visitPrimitiveAsDouble(PrimitiveType t, VariableElement variableElement) {
            
            OptionalDoubleWrapper wrapper = OptionalDoubleWrapper.wrap(variableElement);
            if(wrapper != null) {
                return Optional.of(String.valueOf(wrapper.value()));
            }
            return super.visitPrimitiveAsDouble(t, variableElement);
        }
        
    }
    
}
