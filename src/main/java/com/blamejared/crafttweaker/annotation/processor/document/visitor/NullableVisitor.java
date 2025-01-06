package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.NullableWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
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
import java.util.List;

public class NullableVisitor implements TypeVisitor<Boolean, Void> {
    
    public static final NullableVisitor INSTANCE = new NullableVisitor();
    
    private NullableVisitor() {}
    
    private boolean visitElement(Element element) {
        
        return NullableWrapper.isAnnotated(element);
    }
    
    private boolean visitAnnotationMirrors(List<? extends AnnotationMirror> mirrors) {
        
        return mirrors
                .stream()
                .anyMatch(annotationMirror -> TypeUtils.TypeComparison.isTypeEqual(annotationMirror.getAnnotationType(), ZenCodeType.Nullable.class));
    }
    
    @Override
    public Boolean visit(TypeMirror t) {
        
        return t.accept(this, null);
    }
    
    @Override
    public Boolean visit(TypeMirror t, Void ignored) {
        
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitPrimitive(PrimitiveType t, Void ignored) {
        
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitNull(NullType t, Void ignored) {
        // This is *probably* right
        return true;
    }
    
    @Override
    public Boolean visitArray(ArrayType t, Void ignored) {
        
        return t.getComponentType().accept(this, ignored);
    }
    
    @Override
    public Boolean visitDeclared(DeclaredType t, Void ignored) {
        
        return visitElement(t.asElement());
    }
    
    @Override
    public Boolean visitError(ErrorType t, Void ignored) {
        
        return false;
    }
    
    @Override
    public Boolean visitTypeVariable(TypeVariable t, Void ignored) {
        
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitWildcard(WildcardType t, Void ignored) {
        
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitExecutable(ExecutableType t, Void ignored) {
        
        return t.getReturnType().accept(this, ignored);
    }
    
    @Override
    public Boolean visitNoType(NoType t, Void ignored) {
        // You can't annotate null, but let's just be super safe
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitUnknown(TypeMirror t, Void ignored) {
        
        return visitAnnotationMirrors(t.getAnnotationMirrors());
    }
    
    @Override
    public Boolean visitUnion(UnionType t, Void ignored) {
        
        return t.getAlternatives().stream().anyMatch(mirror -> mirror.accept(this, ignored));
    }
    
    @Override
    public Boolean visitIntersection(IntersectionType t, Void ignored) {
        
        return t.getBounds().stream().anyMatch(mirror -> mirror.accept(this, ignored));
    }
    
}
