package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.model.type.ArrayType;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.BasicType;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Bound;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.GenericType;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.JavaType;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.annotation.processor.util.ZenCodeKeywordUtil;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TypeBuildingVisitor extends SimpleTypeVisitor8<Optional<Type>, TypeBuildingVisitor.Context> {
    
    public static final TypeBuildingVisitor INSTANCE = new TypeBuildingVisitor();
    
    private static final Set<String> IGNORED_INTERFACES = Util.make(() -> {
        Set<String> ignored = new HashSet<>();
        ignored.add(Constable.class.getCanonicalName());
        ignored.add(Serializable.class.getCanonicalName());
        ignored.add(AutoCloseable.class.getCanonicalName());
        
        // Doesn't provide anything useful for scripters
        ignored.add("net.minecraftforge.common.IExtensibleEnum");
        // only exists to act as a mix of 2 other interfaces so forge can have a smaller patch size.
        ignored.add("net.minecraftforge.common.capabilities.ICapabilitySerializable");
        //This doesn't make sense to be exposed
        ignored.add("net.minecraftforge.common.util.INBTSerializable");
        
        //TODO add more
        
        return ignored;
    });
    private static final Predicate<TypeMirror> IGNORED_INTERFACES_PREDICATE = typeMirror -> {
        String fqn = TypeUtils.TypeConversion.convertToFqn(typeMirror);
        return !IGNORED_INTERFACES.contains(fqn);
    };
    
    private TypeBuildingVisitor() {
        
        super(Optional.empty());
    }
    
    public String key(TypeMirror mirror) {
        
        return KeyVisitor.INSTANCE.visit(mirror);
    }
    
    public boolean nullable(TypeMirror mirror) {
        
        return NullableVisitor.INSTANCE.visit(mirror);
    }
    
    private Optional<Type> visitBasic(TypeMirror mirror) {
        
        String key = key(mirror);
        boolean nullable = nullable(mirror);
        return Optional.of(new BasicType(key, ZenCodeKeywordUtil.convertToZenCode(key), nullable));
    }
    
    @Override
    public Optional<Type> visitPrimitive(PrimitiveType t, Context context) {
        
        return visitBasic(t);
    }
    
    @Override
    public Optional<Type> visitArray(javax.lang.model.type.ArrayType t, Context context) {
        
        String key = key(t);
        boolean nullable = nullable(t);
        Optional<Type> componentType = t.getComponentType().accept(this, Context.TYPE_ARGUMENTS);
        String displayName = TypeMirrorWrapper.getTypeDeclaration(t);
        return componentType.map(type -> new ArrayType(key, displayName, nullable, type));
    }
    
    @Override
    public Optional<Type> visitDeclared(DeclaredType t, Context context) {
        
        if(TypeUtils.TypeComparison.isTypeEqual(t, String.class)) {
            return visitBasic(t);
        }
        String key = key(t);
        boolean nullable = nullable(t);
        String displayName = NameVisitor.INSTANCE.visit(t);
        if(!t.getTypeArguments().isEmpty()) {
            displayName += t.getTypeArguments()
                    .stream()
                    .map(NameVisitor.INSTANCE::visit)
                    .collect(Collectors.joining(", ", "<", ">"));
        }
        
        Optional<String> packageName = Util.getPackageName(t);
        Optional<String> className = Util.getSimpleName(t);
        Map<String, Type> typeParameters = collectTypeParameters(t, context);
        Optional<Type> superType = getSuperType(t.asElement(), context);
        List<Type> interfaces = collectInterfaces(t.asElement(), context);
        if(!packageName.isPresent() || !className.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new JavaType(key, displayName, nullable, packageName.get(), className.get(), typeParameters, superType, interfaces));
    }
    
    @Override
    public Optional<Type> visitTypeVariable(TypeVariable t, Context context) {
        
        String key = key(t);
        boolean nullable = nullable(t);
        Bound bound = Bound.from(t);
        Optional<Type> boundType = bound.forVariable(t)
                .orElseGet(() -> TypeUtils.TypeRetrieval.getTypeMirror(Object.class))
                .accept(this, Context.SLIM);
        return boundType.map(type -> new GenericType(key, nullable, type, bound));
    }
    
    @Override
    public Optional<Type> visitWildcard(WildcardType t, Context context) {
        
        String key = key(t);
        boolean nullable = nullable(t);
        Optional<Type> bound = TypeUtils.TypeRetrieval.getTypeMirror(Object.class).accept(this, Context.SLIM);
        return bound.map(type -> new GenericType(key, nullable, type, Bound.SPECIFIC));
    }
    
    @Override
    public Optional<Type> visitNoType(NoType t, Context context) {
        
        if(t.getKind() == TypeKind.VOID) {
            return visitBasic(t);
        }
        return Optional.empty();
    }
    
    private Optional<Type> getSuperType(Element element, Context context) {
        
        if(context.shouldIncludeHierarchy() && element instanceof TypeElement) {
            TypeElement typeElement = (TypeElement) element;
            if(typeElement.getSuperclass().getKind() != TypeKind.NONE) {
                return typeElement.getSuperclass().accept(this, Context.TYPE_ARGUMENTS);
            }
        }
        return Optional.empty();
    }
    
    private List<Type> collectInterfaces(Element element, Context context) {
        
        Set<TypeMirror> interfaces = new LinkedHashSet<>();
        if(context.shouldIncludeHierarchy() && element instanceof TypeElement) {
            collectInterfaces((TypeElement) element, interfaces);
        }
        return interfaces.stream()
                .filter(TypeBuildingVisitor.IGNORED_INTERFACES_PREDICATE)
                .map(typeMirror -> typeMirror.accept(this, Context.TYPE_ARGUMENTS))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(LinkedList::new));
    }
    
    private void collectInterfaces(TypeElement element, Set<TypeMirror> interfaces) {
        
        interfaces.addAll(element.getInterfaces());
        TypeMirrorWrapper.getTypeElement(element.getSuperclass())
                .map(ElementWrapper::unwrap)
                .ifPresent(typeElement -> collectInterfaces(typeElement, interfaces));
        element.getInterfaces()
                .forEach(elementInterface -> TypeMirrorWrapper.getTypeElement(elementInterface)
                        .map(ElementWrapper::unwrap)
                        .ifPresent(typeElement -> collectInterfaces(typeElement, interfaces)));
    }
    
    private Map<String, Type> collectTypeParameters(DeclaredType t, Context context) {
        
        Map<String, Type> typeParameters = new LinkedHashMap<>();
        if(context.shouldIncludeTypeArguments()) {
            TypeElement typeElement = (TypeElement) t.asElement();
            for(int i = 0; i < typeElement.getTypeParameters().size(); i++) {
                TypeParameterElement typeParamEl = typeElement.getTypeParameters().get(i);
                String typeArgKey = typeParamEl.getSimpleName().toString();
                TypeMirror paramOrArgument = typeParamEl.asType();
                if(t.getTypeArguments() != null && !t.getTypeArguments().isEmpty()) {
                    paramOrArgument = t.getTypeArguments().get(i);
                }
                
                Optional<Type> type = paramOrArgument.accept(this, Context.TYPE_ARGUMENTS);
                type.ifPresent(type1 -> {
                    typeParameters.put(typeArgKey, type1);
                });
            }
        }
        return typeParameters;
    }
    
    public static class Context {
        
        
        public static final Context VERBOSE = new TypeBuildingVisitor.Context(true, true);
        public static final Context SLIM = new TypeBuildingVisitor.Context(false, false);
        public static final Context TYPE_ARGUMENTS = new TypeBuildingVisitor.Context(false, true);
        
        private final boolean includeHierarchy;
        private final boolean includeTypeArguments;
        
        public Context(boolean includeHierarchy, boolean includeTypeArguments) {
            
            this.includeHierarchy = includeHierarchy;
            this.includeTypeArguments = includeTypeArguments;
        }
        
        public boolean shouldIncludeHierarchy() {
            
            return includeHierarchy;
        }
        
        public boolean shouldIncludeTypeArguments() {
            
            return includeTypeArguments;
        }
        
        public Context includeHierarchy(boolean includeHierarchy) {
            
            return new TypeBuildingVisitor.Context(includeHierarchy, this.includeTypeArguments);
        }
        
        public Context includeTypeArguments(boolean includeTypeArguments) {
            
            return new Context(this.includeHierarchy, includeTypeArguments);
        }
        
    }
    
}
