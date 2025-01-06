package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.PlaintextComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.RootComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Deprecated;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Examples;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Obtention;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.ParameterComments;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.CasterMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.ConstructorMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.EnumConstantMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.FieldMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.GetterMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.Member;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberOrigin;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MethodMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.OperatorMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.Parameter;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.SetterMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.blamejared.crafttweaker.annotation.processor.util.Optionull;
import com.blamejared.crafttweaker.annotation.processor.util.Pairs;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker_annotations.annotations.ConstructorParameterWrapper;
import com.blamejared.crafttweaker_annotations.annotations.NativeConstructorWrapper;
import com.blamejared.crafttweaker_annotations.annotations.NativeMethodWrapper;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import org.openzen.zencode.java.CasterWrapper;
import org.openzen.zencode.java.ConstructorWrapper;
import org.openzen.zencode.java.FieldWrapper;
import org.openzen.zencode.java.GetterWrapper;
import org.openzen.zencode.java.MethodWrapper;
import org.openzen.zencode.java.OperatorWrapper;
import org.openzen.zencode.java.SetterWrapper;
import org.openzen.zencode.java.StaticExpansionMethodWrapper;
import org.openzen.zencode.java.ZenCodeType;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TypeMembersVisitor extends SimpleElementVisitor8<Map<String, List<Member>>, TypeMembersVisitor.Context> {
    
    public static final TypeMembersVisitor INSTANCE = new TypeMembersVisitor();
    
    private TypeMembersVisitor() {
        
        super(Collections.emptyMap());
    }
    
    @Override
    public Map<String, List<Member>> visitVariable(VariableElement variable, Context context) {
        
        boolean isEnumConstant = ElementUtils.CheckKindOfElement.isOfKind(variable, ElementKind.ENUM_CONSTANT);
        if(variable.getKind().isField() && (isEnumConstant || Util.isExposedField(variable))) {
            String key = variable.getSimpleName().toString();
            MemberOrigin origin = Util.getOrigin(context.owner, variable, context.nativeInfo()
                    .map(nativeTypeInfo -> MemberOrigin.NATIVE)
                    .orElse(MemberOrigin.DECLARED));
            boolean isStatic = variable.getModifiers().contains(Modifier.STATIC);
            Optional<Comment> comment = JavadocCommentsVisitor.visit(variable);
            
            ExtraVisitor.Context extraContext = ExtraVisitor.Context.from(variable, Extra.EMPTY);
            variable.accept(ExtraVisitor.INSTANCE, extraContext);
            Extra extra = extraContext.extra();
            
            Optional<Type> type = variable.asType()
                    .accept(TypeBuildingVisitor.INSTANCE, TypeBuildingVisitor.Context.TYPE_ARGUMENTS);
            if(type.isPresent()) {
                return type.map(type1 -> {
                    if(isEnumConstant) {
                        return new EnumConstantMember(key, key, origin, isStatic, comment, extra, type1);
                    } else { // if it isn't an enum, just treat it as a field
                        return new FieldMember(key, key, origin, isStatic, comment, extra, type1);
                    }
                }).map(member -> Collections.singletonMap(key, Collections.singletonList(member))).get();
            }
        }
        
        return super.visitVariable(variable, context);
    }
    
    @Override
    public Map<String, List<Member>> visitExecutable(ExecutableElement method, Context context) {
        
        if(!Util.isExposedMethod(method)) {
            return super.visitExecutable(method, context);
        }
        
        String key = method.getSimpleName().toString();
        MemberOrigin origin = Util.getOrigin(context.owner, method, context.nativeInfo()
                .map(nativeTypeInfo -> MemberOrigin.NATIVE)
                .orElse(MemberOrigin.DECLARED));
        boolean isVirtualExpansionMethod = origin.isNative() && !StaticExpansionMethodWrapper.isAnnotated(method);
        boolean isStatic = method.getModifiers().contains(Modifier.STATIC) && !isVirtualExpansionMethod;
        Optional<Type> optRetType = TypeBuildingVisitor.INSTANCE.visit(method.getReturnType(), TypeBuildingVisitor.Context.TYPE_ARGUMENTS);
        int paramsToIgnore = isVirtualExpansionMethod ? 1 : 0;
        if(optRetType.isEmpty() || paramsToIgnore > method.getParameters().size()) {
            return Collections.emptyMap();
        }
        Type returnType = optRetType.get();
        Optional<Comment> comment = JavadocCommentsVisitor.visit(method);
        ExtraVisitor.Context extraContext = ExtraVisitor.Context.from(method, Extra.EMPTY);
        method.accept(ExtraVisitor.INSTANCE, extraContext);
        Extra extra = extraContext.extra();
        
        
        List<Parameter> params = method.getParameters()
                .stream()
                .skip(paramsToIgnore)
                .filter(variableElement -> !TypeUtils.TypeComparison.isTypeEqual(variableElement.asType(), Class.class))
                .map(param -> ParameterVisitor.INSTANCE.visit(param, new ParameterVisitor.Context(method)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        
        Map<String, Type> typeParams = method.getTypeParameters()
                .stream()
                .map(typeParameterElement -> Pair.of(typeParameterElement.getSimpleName()
                        .toString(), TypeBuildingVisitor.INSTANCE.visit(typeParameterElement.asType(), TypeBuildingVisitor.Context.TYPE_ARGUMENTS)))
                .filter(Pairs.filterSecond(Optional::isPresent))
                .map(pair -> pair.mapSecond(Optional::get))
                .collect(Pairs.collect());
        
        //  key, displayName, origin, isStatic, comments, extra, parameters, returnType,  typeParameters
        Map<String, List<Member>> members = new LinkedHashMap<>();
        String specifiedKey = getMemberKey(method);
        if(MethodWrapper.isAnnotated(method) || StaticExpansionMethodWrapper.isAnnotated(method)) {
            members.computeIfAbsent(specifiedKey, k -> new LinkedList<>())
                    .add(new MethodMember(specifiedKey, specifiedKey, origin, isStatic, comment, extra, params, returnType, typeParams));
        }
        if(GetterWrapper.isAnnotated(method)) {
            members.computeIfAbsent(specifiedKey, k -> new LinkedList<>())
                    .add(new GetterMember(specifiedKey, specifiedKey, origin, isStatic, comment, extra, returnType));
        }
        if(SetterWrapper.isAnnotated(method)) {
            members.computeIfAbsent(specifiedKey, k -> new LinkedList<>())
                    .add(new SetterMember(specifiedKey, specifiedKey, origin, isStatic, comment, extra, params));
        }
        if(ConstructorWrapper.isAnnotated(method)) {
            members.computeIfAbsent("new", k -> new LinkedList<>())
                    .add(new ConstructorMember(origin, comment, extra, params, typeParams));
        }
        // TODO don't inherit casters and operators if a subtype already has an caster / operator with the same values
        Optionull.ifPresent(CasterWrapper.wrap(method), caster -> {
            members.computeIfAbsent(specifiedKey, k -> new LinkedList<>())
                    .add(new CasterMember(specifiedKey, specifiedKey, origin, isStatic, comment, extra, returnType, caster.implicit()));
        });
        
        Optionull.ifPresent(OperatorWrapper.wrap(method), operator -> {
            if(operator.value() != ZenCodeType.OperatorType.COMPARE) {
                members.computeIfAbsent(specifiedKey, k -> new LinkedList<>())
                        .add(new OperatorMember(specifiedKey, specifiedKey, origin, isStatic, comment, extra, params, returnType, typeParams, operator.value()));
            }
        });
        
        
        return members;
    }
    
    private String getMemberKey(Element element) {
        //TODO scan for @docKey or maybe @docMember?
        String name = element.getSimpleName().toString();
        GetterWrapper getter = GetterWrapper.wrap(element);
        if(getter != null) {
            if(!getter.valueIsDefaultValue()) {
                return getter.value();
            } else if(name.startsWith("get")) {
                // getThing -> thing
                return name.substring(3, 4).toLowerCase() + name.substring(4);
            }
        }
        
        SetterWrapper setter = SetterWrapper.wrap(element);
        if(setter != null) {
            if(!setter.valueIsDefaultValue()) {
                return setter.value();
            } else if(name.startsWith("set")) {
                // setThing -> thing
                return name.substring(3, 4).toLowerCase() + name.substring(4);
            }
        }
        
        FieldWrapper field = FieldWrapper.wrap(element);
        if(field != null && !field.valueIsDefaultValue()) {
            return field.value();
        }
        
        MethodWrapper method = MethodWrapper.wrap(element);
        if(method != null && !method.valueIsDefaultValue()) {
            return method.value();
        }
        
        StaticExpansionMethodWrapper staticExpansionMethod = StaticExpansionMethodWrapper.wrap(element);
        if(staticExpansionMethod != null && !staticExpansionMethod.valueIsDefaultValue()) {
            return staticExpansionMethod.value();
        }
        
        return name;
    }
    
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ClassCanBeRecord"})
    public static class Context {
        
        private final TypeElement owner;
        private final Optional<NativeTypeInfo> nativeInfo;
        
        public Context(TypeElement owner, Optional<NativeTypeInfo> nativeInfo) {
            
            this.owner = owner;
            this.nativeInfo = nativeInfo;
        }
        
        public TypeElement owner() {
            
            return owner;
        }
        
        public Optional<NativeTypeInfo> nativeInfo() {
            
            return nativeInfo;
        }
        
    }
    
    public static class NativeTypeInfo {
        
        private final TypeElement registeringTypeElement;
        private final TypeMirror expandedTypeMirror;
        
        private final List<NativeConstructorInfo> constructor;
        private final List<NativeMethodInfo> methods;
        
        public NativeTypeInfo(TypeElement registeringTypeElement, TypeMirror expandedTypeMirror) {
            
            this(registeringTypeElement, expandedTypeMirror, Collections.emptyList(), Collections.emptyList());
        }
        
        public NativeTypeInfo(TypeElement registeringTypeElement, TypeMirror expandedTypeMirror, List<NativeConstructorInfo> constructor) {
            
            this(registeringTypeElement, expandedTypeMirror, constructor, Collections.emptyList());
        }
        
        public NativeTypeInfo(TypeElement registeringTypeElement, TypeMirror expandedTypeMirror, List<NativeConstructorInfo> constructor, List<NativeMethodInfo> methods) {
            
            this.registeringTypeElement = registeringTypeElement;
            this.expandedTypeMirror = expandedTypeMirror;
            this.constructor = constructor;
            this.methods = methods;
        }
        
        public TypeElement registeringTypeElement() {
            
            return registeringTypeElement;
        }
        
        public TypeMirror expandedTypeMirror() {
            
            return expandedTypeMirror;
        }
        
        public List<NativeConstructorInfo> constructor() {
            
            return constructor;
        }
        
        public List<NativeMethodInfo> methods() {
            
            return methods;
        }
        
        
        @Override
        public String toString() {
            
            final StringBuilder sb = new StringBuilder("NativeTypeInfo{");
            sb.append("registeringTypeElement=").append(registeringTypeElement);
            sb.append(", expandedTypeElement=").append(expandedTypeMirror);
            sb.append(", constructor=").append(constructor);
            sb.append(", methods=").append(methods);
            sb.append('}');
            return sb.toString();
        }
        
    }
    
    
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ClassCanBeRecord"})
    public static class NativeConstructorInfo {
        
        private final List<NativeConstructorParam> params;
        private final Optional<String> description;
        private final Optional<String> deprecationMessage;
        private final Optional<String> since;
        private final Optional<String> obtention;
        
        public NativeConstructorInfo(List<NativeConstructorParam> params, Optional<String> description, Optional<String> deprecationMessage, Optional<String> since, Optional<String> obtention) {
            
            this.params = params;
            this.description = description;
            this.deprecationMessage = deprecationMessage;
            this.since = since;
            this.obtention = obtention;
        }
        
        public static NativeConstructorInfo of(NativeConstructorWrapper wrapper) {
            
            List<NativeConstructorParam> params = Lists.transform(Arrays.asList(wrapper.value()), NativeConstructorParam::of);
            Optional<String> description = Util.optionalIf(wrapper.description(), Predicate.not(String::isBlank));
            Optional<String> deprecationMessage = Util.optionalIf(wrapper.deprecationMessage(), Predicate.not(String::isBlank));
            Optional<String> since = Util.optionalIf(wrapper.getSinceVersion(), Predicate.not(String::isBlank));
            Optional<String> obtention = Util.optionalIf(wrapper.getObtention(), Predicate.not(String::isBlank));
            return new NativeConstructorInfo(params, description, deprecationMessage, since, obtention);
        }
        
        public List<NativeConstructorParam> params() {
            
            return params;
        }
        
        public Optional<String> description() {
            
            return description;
        }
        
        public Optional<String> deprecationMessage() {
            
            return deprecationMessage;
        }
        
        public Optional<String> since() {
            
            return since;
        }
        
        public Optional<String> obtention() {
            
            return obtention;
        }
        
        public Extra getExtra() {
            
            Extra extra = Extra.EMPTY;
            Optional<Comment> deprecationMessage = this.deprecationMessage.map(s -> new RootComment(new PlaintextComment(s)));
            Optional<Comment> since = this.since.map(s -> new RootComment(new PlaintextComment(s)));
            Optional<Obtention> obtention = this.obtention.map(s -> new RootComment(new PlaintextComment(s)))
                    .map(Obtention::new);
            Map<String, Comment> parameterComments = this.params.stream()
                    .map(nativeConstructorParam -> Pair.of(nativeConstructorParam.name, nativeConstructorParam.description))
                    .filter(Pairs.filterSecond(Optional::isPresent))
                    .map(stringOptionalPair -> stringOptionalPair.mapSecond(Optional::get))
                    .map(stringStringPair -> stringStringPair.<Comment> mapSecond(s -> new RootComment(new PlaintextComment(s))))
                    .collect(Pairs.collect());
            Map<String, List<String>> examples = this.params.stream()
                    .map(nativeConstructorParam -> Pair.of(nativeConstructorParam.name, nativeConstructorParam.examples))
                    .filter(Pairs.filterSecond(Predicate.not(List::isEmpty)))
                    .collect(Pairs.collect());
            
            if(deprecationMessage.isPresent()) {
                extra = extra.withDeprecated(new Deprecated(deprecationMessage, since, false));
            }
            if(obtention.isPresent()) {
                extra = extra.withObtention(obtention.get());
            }
            if(!parameterComments.isEmpty()) {
                extra = extra.withParameterComment(new ParameterComments(parameterComments));
            }
            if(!examples.isEmpty()) {
                extra = extra.withExamples(new Examples(examples));
            }
            return extra;
        }
        
        @Override
        public String toString() {
            
            final StringBuilder sb = new StringBuilder("NativeConstructorInfo{");
            sb.append("params=").append(params);
            sb.append(", description='").append(description).append('\'');
            sb.append(", deprecationMessage='").append(deprecationMessage).append('\'');
            sb.append(", since='").append(since).append('\'');
            sb.append(", obtention='").append(obtention).append('\'');
            sb.append('}');
            return sb.toString();
        }
        
    }
    
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ClassCanBeRecord"})
    public static class NativeConstructorParam {
        
        private final TypeMirror type;
        private final String name;
        private final Optional<String> description;
        private final List<String> examples;
        
        public NativeConstructorParam(TypeMirror type, String name, Optional<String> description, List<String> examples) {
            
            this.type = type;
            this.name = name;
            this.description = description;
            this.examples = examples;
        }
        
        public static NativeConstructorParam of(ConstructorParameterWrapper wrapper) {
            
            return new NativeConstructorParam(wrapper.typeAsTypeMirror(), wrapper.name(), Util.optionalIf(wrapper.description(), Predicate.not(String::isBlank)), Arrays.asList(wrapper.examples()));
        }
        
        
        public Optional<Parameter> asParameter() {
            
            Optional<Type> paramType = TypeBuildingVisitor.INSTANCE.visit(this.type, TypeBuildingVisitor.Context.TYPE_ARGUMENTS);
            return paramType.map(type -> new Parameter(this.name, this.name, type, Optional.empty()));
        }
        
        public TypeMirror type() {
            
            return type;
        }
        
        public String name() {
            
            return name;
        }
        
        public Optional<String> description() {
            
            return description;
        }
        
        public List<String> examples() {
            
            return examples;
        }
        
        @Override
        public String toString() {
            
            final StringBuilder sb = new StringBuilder("NativeConstructorParam{");
            sb.append("type=").append(type);
            sb.append(", name='").append(name).append('\'');
            sb.append(", description=").append(description);
            sb.append(", examples=").append(examples);
            sb.append('}');
            return sb.toString();
        }
        
    }
    
    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "ClassCanBeRecord"})
    public static class NativeMethodInfo {
        
        private final String name;
        private final List<TypeMirror> parameters;
        private final Optional<String> getterName;
        private final Optional<String> setterName;
        
        public NativeMethodInfo(String name, List<TypeMirror> parameters, Optional<String> getterName, Optional<String> setterName) {
            
            this.name = name;
            this.parameters = parameters;
            this.getterName = getterName;
            this.setterName = setterName;
        }
        
        public static NativeMethodInfo of(NativeMethodWrapper method) {
            
            return new NativeMethodInfo(method.name(), Arrays.asList(method.parametersAsTypeMirror()), Util.optionalIf(method.getterName(), Predicate.not(String::isBlank)), Util.optionalIf(method.setterName(), Predicate.not(String::isBlank)));
        }
        
        public String name() {
            
            return name;
        }
        
        public Optional<ExecutableElement> match(TypeElement element) {
            
            List<ExecutableElement> result = FluentElementFilter.createFluentElementFilter(element.getEnclosedElements())
                    .applyFilter(AptkCoreMatchers.IS_METHOD)
                    .applyFilter(AptkCoreMatchers.BY_NAME)
                    .filterByOneOf(name())
                    .applyFilter(AptkCoreMatchers.BY_PARAMETER_TYPE_MIRROR)
                    .filterByOneOf(parameters().toArray(TypeMirror[]::new))
                    .getResult();
            
            if(result.size() == 1) {
                return Optional.of(result.get(0));
            }
            return Optional.empty();
        }
        
        public List<TypeMirror> parameters() {
            
            return parameters;
        }
        
        public List<Parameter> asParameters() {
            
            return this.parameters()
                    .stream()
                    .map(typeMirror -> TypeBuildingVisitor.INSTANCE.visit(typeMirror, TypeBuildingVisitor.Context.TYPE_ARGUMENTS))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(type -> new Parameter(this.name, this.name, type, Optional.empty()))
                    .collect(Collectors.toList());
        }
        
        public Optional<String> getterName() {
            
            return getterName;
        }
        
        public Optional<String> setterName() {
            
            return setterName;
        }
        
        @Override
        public String toString() {
            
            final StringBuilder sb = new StringBuilder("NativeMethod{");
            sb.append("name='").append(name).append('\'');
            sb.append(", parameters=").append(parameters);
            sb.append(", getterName=").append(getterName);
            sb.append(", setterName=").append(setterName);
            sb.append('}');
            return sb.toString();
        }
        
    }
    
}
