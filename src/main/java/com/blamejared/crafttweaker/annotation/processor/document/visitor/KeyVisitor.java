package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import java.util.stream.Collectors;

public class KeyVisitor implements TypeVisitor<String, Void> {
    
    public static final KeyVisitor INSTANCE = new KeyVisitor();
    
    private KeyVisitor() {}
    
    @Override
    public String visit(TypeMirror t) {
        
        return t.accept(this, null);
    }
    
    @Override
    public String visit(TypeMirror t, Void unused) {
        
        return t.accept(this, unused);
    }
    
    @Override
    public String visitPrimitive(PrimitiveType t, Void unused) {
        
        switch(t.getKind()) {
            case BOOLEAN -> {
                return "boolean";
            }
            case BYTE -> {
                return "byte";
            }
            case SHORT -> {
                return "short";
            }
            case INT -> {
                return "int";
            }
            case LONG -> {
                return "long";
            }
            case CHAR -> {
                return "char";
            }
            case FLOAT -> {
                return "float";
            }
            case DOUBLE -> {
                return "double";
            }
            default ->
                    throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the key of Primitive: " + t);
        }
        
    }
    
    @Override
    public String visitNull(NullType t, Void unused) {
        
        return "null";
    }
    
    @Override
    public String visitArray(ArrayType t, Void unused) {
        
        String base = t.getComponentType().accept(this, unused);
        return base + "[]";
    }
    
    @Override
    public String visitDeclared(DeclaredType t, Void unused) {
        
        if(t.asElement() instanceof QualifiedNameable) {
            return ((QualifiedNameable) t.asElement()).getQualifiedName().toString();
        }
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the key DeclaredType: " + t);
    }
    
    @Override
    public String visitError(ErrorType t, Void unused) {
        
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the key of Error: " + t);
    }
    
    @Override
    public String visitTypeVariable(TypeVariable t, Void unused) {
        
        return t
                .asElement()
                .getSimpleName()
                .toString();
    }
    
    @Override
    public String visitWildcard(WildcardType t, Void unused) {
        
        return "?";
    }
    
    @Override
    public String visitExecutable(ExecutableType t, Void unused) {
        
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the key of Executable: " + t);
    }
    
    @Override
    public String visitNoType(NoType t, Void unused) {
        
        if(t.getKind() == TypeKind.VOID) {
            return "void";
        }
        return "";
    }
    
    @Override
    public String visitUnknown(TypeMirror t, Void unused) {
        
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the key of Unknown: " + t);
    }
    
    @Override
    public String visitUnion(UnionType t, Void unused) {
        
        return t.getAlternatives().stream().map(this::visit).collect(Collectors.joining(" | "));
    }
    
    @Override
    public String visitIntersection(IntersectionType t, Void unused) {
        
        return t.getBounds().stream().map(this::visit).collect(Collectors.joining(" | "));
    }
    
}
