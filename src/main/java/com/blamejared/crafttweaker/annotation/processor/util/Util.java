package com.blamejared.crafttweaker.annotation.processor.util;

import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberOrigin;
import com.blamejared.crafttweaker.api.event.BusCarrierWrapper;
import com.blamejared.crafttweaker.api.event.ZenEventWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeUtils;
import org.openzen.zencode.java.CasterWrapper;
import org.openzen.zencode.java.ConstructorWrapper;
import org.openzen.zencode.java.FieldWrapper;
import org.openzen.zencode.java.GetterWrapper;
import org.openzen.zencode.java.MethodWrapper;
import org.openzen.zencode.java.OperatorWrapper;
import org.openzen.zencode.java.SetterWrapper;
import org.openzen.zencode.java.StaticExpansionMethodWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Util {
    
    /**
     * Merge "source" into "target". If fields have equal name, merge them recursively.
     * Null values in source will remove the field from the target.
     * Override target values with source values
     * Keys not supplied in source will remain unchanged in target
     *
     * @return the merged object (target).
     */
    public static JsonObject deepMerge(JsonObject source, JsonObject target) {
        
        for(Map.Entry<String, JsonElement> sourceEntry : source.entrySet()) {
            String key = sourceEntry.getKey();
            JsonElement value = sourceEntry.getValue();
            if(!target.has(key)) {
                //target does not have the same key, so perhaps it should be added to target
                if(!value.isJsonNull()) //well, only add if the source value is not null
                {
                    target.add(key, value);
                }
            } else {
                if(!value.isJsonNull()) {
                    if(value.isJsonObject()) {
                        //source value is json object, start deep merge
                        deepMerge(value.getAsJsonObject(), target.get(key).getAsJsonObject());
                    } else {
                        target.add(key, value);
                    }
                } else {
                    target.remove(key);
                }
            }
        }
        return target;
    }
    
    
    public static JsonElement sortObjectsFirst(JsonElement element) {
        
        if(element.isJsonArray()) {
            JsonArray array = new JsonArray();
            element.getAsJsonArray().forEach(item -> array.add(sortObjectsFirst(item)));
            return array;
        } else if(element.isJsonObject()) {
            JsonObject sortedObject = new JsonObject();
            Comparator<Map.Entry<String, JsonElement>> objectFirstComparator = (entry1, entry2) -> {
                boolean firstObject = entry1.getValue().isJsonObject();
                boolean secondObject = entry2.getValue().isJsonObject();
                if(firstObject && !secondObject) {
                    return -1;
                } else if(!firstObject && secondObject) {
                    return 1;
                } else {
                    return entry1.getKey().compareTo(entry2.getKey());
                }
            };
            
            element.getAsJsonObject().entrySet().stream()
                    .sorted(objectFirstComparator)
                    .forEach(entry -> sortedObject.add(entry.getKey(), sortObjectsFirst(entry.getValue())));
            return sortedObject;
        }
        return element; // for other types such as JsonPrimitive, we return as is
    }
    
    public static JsonElement sort(JsonElement element) {
        
        if(element.isJsonArray()) {
            JsonArray array = new JsonArray();
            element.getAsJsonArray().forEach(item -> array.add(sort(item)));
            return array;
        } else if(element.isJsonObject()) {
            JsonObject sortedObject = new JsonObject();
            Comparator<String> kind = (a, b) -> Boolean.compare(b.equals("kind"), a.equals("kind"));
            
            element.getAsJsonObject().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(kind.thenComparing(a -> a)))
                    .forEach(entry -> sortedObject.add(entry.getKey(), sort(entry.getValue())));
            return sortedObject;
        }
        return element; // for other types such as JsonPrimitive, we return as is
    }
    
    public static <T> T make(Supplier<T> supplier) {
        
        return supplier.get();
    }
    
    public static <T> T make(T instance, Consumer<T> consumer) {
        
        consumer.accept(instance);
        return instance;
    }
    
    /**
     * Quotes the given String in double quotes ({@code  "}) and escapes any control character.
     *
     * @param str String to quote and escape.
     *
     * @return a new String with the String quoted and escaped.
     */
    public static String quoteAndEscape(String str) {
        
        return wrap(str, "\"", true);
    }
    
    /**
     * Wraps the given String in another String and also optionally escapes control characters in the String.
     *
     * @param str    String to wrap
     * @param with   String to wrap with
     * @param escape Should control characters be escaped
     *
     * @return a new String that is wrapped with the given String and optionally escaped.
     */
    public static String wrap(String str, String with, @ZenCodeType.OptionalBoolean boolean escape) {
        
        StringBuilder stringbuilder = new StringBuilder(with);
        
        for(int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            
            if(escape) {
                if(c == '\\' || c == '"') {
                    stringbuilder.append('\\');
                }
            }
            
            stringbuilder.append(c);
        }
        
        return stringbuilder.append(with).toString();
    }
    
    public static <T> T uncheck(final Object o) {
        
        return (T) o;
    }
    
    public static <P, R> Function<P, R> notNull(Function<P, R> func) {
        
        return p -> Objects.requireNonNull(func.apply(p));
    }
    
    
    public static <P, R> Function<P, R> cacheLatest(final Function<P, R> func) {
        
        return new Function<>() {
            private P key;
            private R value;
            
            @Override
            public R apply(P p) {
                
                this.value = p.equals(this.key) ? this.value : func.apply(p);
                this.key = p;
                return this.value;
            }
        };
    }
    
    public static <T extends Enum<T> & Keyable> Function<String, T> enumLookup(T[] values) {
        
        return s -> {
            for(T value : values) {
                if(value.key().equals(s)) {
                    return value;
                }
            }
            throw new IllegalStateException();
        };
    }
    
    public static boolean isSubTypeOrGeneric(TypeMirror first, TypeMirror second) {
        //TODO uncomment? Make sure this actually works as expected
        //                if(TypeUtils.CheckTypeKind.isTypeVar(first)) {
        //                    return TypeUtils.Generics.genericIsAssignableTo(first, TypeUtils.Generics.createGenericType(second));
        //                }
        //                if(TypeUtils.CheckTypeKind.isTypeVar(second)) {
        //                    return TypeUtils.Generics.genericIsAssignableTo(second, TypeUtils.Generics.createGenericType(first));
        //                }
        return TypeUtils.getTypes()
                .isSubtype(TypeUtils.getTypes().erasure(first), TypeUtils.getTypes().erasure(second));
    }
    
    public static boolean isMethodSame(Element firstElem, Element secondElem) {
        
        if(firstElem instanceof ExecutableElement && secondElem instanceof ExecutableElement) {
            ExecutableElement first = (ExecutableElement) firstElem;
            ExecutableElement second = (ExecutableElement) secondElem;
            return first.getSimpleName().equals(second.getSimpleName()) // Names are the same
                    && isSubTypeOrGeneric(first.getReturnType(), second.getReturnType()) // Return types are the same or covariant
                    && first.getParameters().size() == second.getParameters().size() // Same amount of params
                    && IntStream.range(0, first.getParameters()
                            .size()) // Parameters are the same, or are a generic subtype
                    .mapToObj(i -> Pair.of(first.getParameters().get(i), second.getParameters().get(i)))
                    .map(Pairs.map(VariableElement::asType, VariableElement::asType))
                    .allMatch(Pairs.filter(Util::isSubTypeOrGeneric));
        }
        return false;
    }
    
    public static MemberOrigin getOrigin(Element element, ExecutableElement method, MemberOrigin defaultOrigin) {
        
        //TODO this needs to handle native stuff
        Optional<ExecutableElement> isInherited = Arrays.stream(ElementUtils.AccessTypeHierarchy.getSuperTypeElements(ElementUtils.CastElement.castToTypeElement(element)))
                .filter(typeElement -> !TypeUtils.TypeComparison.isTypeEqual(typeElement, Object.class))
                .filter(typeElement -> !TypeUtils.TypeComparison.isTypeEqual(typeElement, Enum.class))
                .flatMap(typeElement -> ElementUtils.AccessEnclosedElements.getEnclosedMethods(typeElement).stream())
                .filter(otherMethod -> method.getSimpleName().equals(otherMethod.getSimpleName())) // Names are the same
                .filter(otherMethod -> isSubTypeOrGeneric(method.getReturnType(), otherMethod.getReturnType())) // Return types are the same or covariant
                .filter(otherMethod -> method.getParameters().size() == otherMethod.getParameters()
                        .size()) // Params are the same size
                .filter(otherMethod -> IntStream.range(0, method.getParameters().size())
                        .mapToObj(i -> Pair.of(method.getParameters().get(i), otherMethod.getParameters().get(i)))
                        .map(Pairs.map(VariableElement::asType, VariableElement::asType))
                        .allMatch(Pairs.filter(Util::isSubTypeOrGeneric)))
                .findFirst();
        return isInherited.isPresent() ? MemberOrigin.INHERITED : defaultOrigin;
    }
    
    //TODO make sure that fields are inherited correctly
    public static MemberOrigin getOrigin(Element element, VariableElement field, MemberOrigin defaultOrigin) {
        
        //TODO is any of this actually needed??
        TypeElement definingType = (TypeElement) field.getEnclosingElement();
        boolean isInherited = !TypeUtils.TypeComparison.isTypeEqual(element.asType(), definingType.asType()) || Arrays.stream(ElementUtils.AccessTypeHierarchy.getSuperTypeElements(ElementUtils.CastElement.castToTypeElement(element)))
                .filter(typeElement -> !TypeUtils.TypeComparison.isTypeEqual(typeElement, Object.class))
                .filter(typeElement -> !TypeUtils.TypeComparison.isTypeEqual(typeElement, Enum.class))
                .flatMap(typeElement -> ElementUtils.AccessEnclosedElements.getEnclosedFields(typeElement).stream())
                .anyMatch(otherMethod -> field.getSimpleName()
                        .equals(otherMethod.getSimpleName())); // Names are the same
        return isInherited ? MemberOrigin.INHERITED : defaultOrigin;
    }
    
    public static boolean isExposedField(Element element) {
        
        return FieldWrapper.isAnnotated(element);
    }
    
    public static boolean isExposedMethod(Element element) {
        
        return CasterWrapper.isAnnotated(element) || ConstructorWrapper.isAnnotated(element) || GetterWrapper.isAnnotated(element) || MethodWrapper.isAnnotated(element) || OperatorWrapper.isAnnotated(element) || SetterWrapper.isAnnotated(element) || StaticExpansionMethodWrapper.isAnnotated(element);
    }
    
    
    public static <T> Optional<T> optionalIf(T thing, Predicate<T> predicate) {
        
        return Optional.of(thing).filter(predicate);
    }
    
    public static boolean isEvent(TypeElement typeElement) {
        
        return ZenEventWrapper.isAnnotated(typeElement) || BusCarrierWrapper.isAnnotated(typeElement);
    }
    
    public static List<AnnotationMirror> getAnnotationsOfType(TypeElement element, Class<?> annotationClass, Class<?> repeatableHolder) {
        
        List<AnnotationMirror> result = new LinkedList<>();
        String annotationName = annotationClass.getCanonicalName();
        String holderName = repeatableHolder.getCanonicalName();
        
        for(AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            String annotationType = annotationMirror.getAnnotationType().toString();
            if(annotationType.equals(annotationName)) {
                result.add(annotationMirror);
            }
            if(annotationType.equals(holderName)) {
                for(AnnotationValue value : annotationMirror.getElementValues().values()) {
                    if(value.getValue() instanceof Collection<?>) {
                        Collection<?> valueValue = (Collection<?>) value.getValue();
                        for(Object o : valueValue) {
                            result.add((AnnotationMirror) o);
                        }
                    }
                }
            }
            
        }
        return result;
    }
    
}
