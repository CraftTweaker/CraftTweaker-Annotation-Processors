package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.model.type.Bound;

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
import java.util.Optional;
import java.util.stream.Collectors;

public class NameVisitor implements TypeVisitor<String, Void> {
    
    public static final NameVisitor INSTANCE = new NameVisitor();
    
    private NameVisitor() {}
    
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
        
        return t.toString();
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
        
        return t.asElement().getSimpleName().toString();
    }
    
    @Override
    public String visitError(ErrorType t, Void unused) {
        
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the name of Error: " + t);
    }
    
    @Override
    public String visitTypeVariable(TypeVariable t, Void unused) {
        
        String base = t
                .asElement()
                .getSimpleName()
                .toString();
        Bound bound = Bound.from(t);
        Optional<TypeMirror> boundType = bound.forVariable(t);
        base += boundType.filter(typeMirror -> bound == Bound.UPPER)
                .map(typeMirror -> " : " + typeMirror.accept(this, unused))
                .orElse("");
        return base;
    }
    
    @Override
    public String visitWildcard(WildcardType t, Void unused) {
        //TODO should this be Object?
        return "?";
    }
    
    @Override
    public String visitExecutable(ExecutableType t, Void unused) {
        
        throw new UnsupportedOperationException("[PLEASE REPORT] Unable to get the name of Executable: " + t);
    }
    
    @Override
    public String visitNoType(NoType t, Void unused) {
        
        return "void";
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
