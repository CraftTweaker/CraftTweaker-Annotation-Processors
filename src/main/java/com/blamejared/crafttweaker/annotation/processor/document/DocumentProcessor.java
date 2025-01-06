package com.blamejared.crafttweaker.annotation.processor.document;

import com.blamejared.crafttweaker.annotation.processor.CraftTweakerProcessor;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.HTMLAwareJavadocDescription;
import com.blamejared.crafttweaker.annotation.processor.document.model.DocFolder;
import com.blamejared.crafttweaker.annotation.processor.document.model.DocJson;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.PlaintextComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.RootComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.ConstructorMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.GetterMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.Member;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberGroup;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberOrigin;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MethodMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.Parameter;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.SetterMember;
import com.blamejared.crafttweaker.annotation.processor.document.model.page.EnumPage;
import com.blamejared.crafttweaker.annotation.processor.document.model.page.EventPage;
import com.blamejared.crafttweaker.annotation.processor.document.model.page.PageVersion;
import com.blamejared.crafttweaker.annotation.processor.document.model.page.TypePage;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.blamejared.crafttweaker.annotation.processor.document.visitor.ExtraVisitor;
import com.blamejared.crafttweaker.annotation.processor.document.visitor.JavadocCommentsVisitor;
import com.blamejared.crafttweaker.annotation.processor.document.visitor.JavadocEventVisitor;
import com.blamejared.crafttweaker.annotation.processor.document.visitor.TypeBuildingVisitor;
import com.blamejared.crafttweaker.annotation.processor.document.visitor.TypeMembersVisitor;
import com.blamejared.crafttweaker.annotation.processor.util.Pairs;
import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.annotation.processor.validation.event.validator.visitors.CancelableTreeVisitor;
import com.blamejared.crafttweaker.api.event.BusWrapper;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.DocumentWrapper;
import com.blamejared.crafttweaker_annotations.annotations.NativeMethod;
import com.blamejared.crafttweaker_annotations.annotations.NativeMethodWrapper;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistrationWrapper;
import com.google.auto.service.AutoService;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.sun.source.util.Trees;
import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import net.minecraftforge.eventbus.api.HasResultWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.openzen.zencode.java.NameWrapper;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@AutoService(Processor.class)
public class DocumentProcessor extends CraftTweakerProcessor {
    
    private static final String defaultOutputDirectory = "docsOut";
    private static final String outputDirectoryOptionName = "crafttweaker.processor.document.output_directory";
    private static final String multiSourceOptionName = "crafttweaker.processor.document.multi_source";
    
    public static final Function<RoundEnvironment, BiMap<TypeElement, TypeElement>> NATIVE_REGISTRY = Util.cacheLatest(environment -> {
        ImmutableBiMap.Builder<TypeElement, TypeElement> map = ImmutableBiMap.builder();
        environment.getElementsAnnotatedWith(NativeTypeRegistration.class)
                .stream()
                .map(element -> Pair.of(ElementUtils.CastElement.castToTypeElement(element), Objects.requireNonNull(NativeTypeRegistrationWrapper.wrap(element))))
                .map(pair -> pair.mapSecond(NativeTypeRegistrationWrapper::valueAsTypeMirror))
                .map(pair -> pair.mapSecond(TypeUtils.TypeRetrieval::getTypeElement))
                .forEach(Pairs.forEach(map::put));
        return map.build();
    });
    
    private Path outputDirectory = Path.of(defaultOutputDirectory);
    private boolean multiSourceProject;
    
    @Override
    public Set<String> getSupportedOptions() {
        
        final Set<String> supportedOptions = new HashSet<>(super.getSupportedOptions());
        supportedOptions.add(outputDirectoryOptionName);
        supportedOptions.add(multiSourceOptionName);
        return supportedOptions;
    }
    
    @Override
    public Collection<Class<? extends Annotation>> getSupportedAnnotationClasses() {
        
        // TODO should this also read @CraftTweakerPlugin? What could we get from that
        return Set.of(Document.class);
    }
    
    @Override
    protected void setupDependencyContainer() {
        
        super.setupDependencyContainer();
        setupTrees(processingEnv);
        setupReflections();
    }
    
    @Override
    protected void performInitialization() {
        
        Map<String, String> options = processingEnv.getOptions();
        final String docsOut = options.getOrDefault(outputDirectoryOptionName, defaultOutputDirectory);
        outputDirectory = Path.of(docsOut);
        final String multiSource = processingEnv.getOptions().getOrDefault(multiSourceOptionName, "false");
        multiSourceProject = Boolean.parseBoolean(multiSource.toLowerCase(Locale.ROOT));
    }
    
    @Override
    protected boolean performProcessing(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        
        if(roundEnv.processingOver()) {
            handleLastRound();
        } else {
            handleIntermediateRound(roundEnv);
        }
        
        return false;
    }
    
    private void setupReflections() {
        
        final ConfigurationBuilder configuration = new ConfigurationBuilder().addUrls(ClasspathHelper.forJavaClassPath())
                .addClassLoaders(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader(), getClass().getClassLoader())
                .addUrls(ClasspathHelper.forClassLoader())
                .setParallel(true)
                .setExpandSuperTypes(true);
        
        final Reflections reflections = new Reflections(configuration);
        dependencyContainer.addInstanceAs(reflections, Reflections.class);
    }
    
    private void setupTrees(ProcessingEnvironment processingEnv) {
        
        final String environmentClassName = processingEnv.getClass().getName();
        
        if(!environmentClassName.equals("com.sun.tools.javac.processing.JavacProcessingEnvironment")) {
            //Let's throw the exception ourselves, since Trees.instance just throws an empty IllegalArgumentException which is harder to trace down
            throw new IllegalArgumentException("Processing environment must be JavacProcessingEnvironment, but is '%s'! Make sure you use gradle for compilation.".formatted(environmentClassName));
        }
        
        final Trees instance = Trees.instance(processingEnv);
        dependencyContainer.addInstanceAs(instance, Trees.class);
    }
    
    public void handleIntermediateRound(RoundEnvironment roundEnvironment) {
        
        roundEnvironment.getElementsAnnotatedWith(Document.class)
                .stream()
                .map(DocumentWrapper::wrap)
                .map(Objects::requireNonNull)
                .forEach(document -> {
                    TypeElement typeElement = ElementUtils.CastElement.castToTypeElement(document._annotatedElement());
                    String key = document.value();
                    String[] docPaths = key.split("/");
                    String displayName = docPaths[docPaths.length - 1];
                    String[] pageParents = ArrayUtils.subarray(docPaths, 0, docPaths.length - 1);
                    boolean isName = NameWrapper.isAnnotated(typeElement);
                    boolean isNative = NativeTypeRegistrationWrapper.isAnnotated(typeElement);
                    
                    if(isNative) {
                        NativeTypeRegistrationWrapper nativeWrapper = Objects.requireNonNull(NativeTypeRegistrationWrapper.wrap(typeElement));
                        String zenCodeName = nativeWrapper.zenCodeName();
                        TypeMirror expanded = nativeWrapper.valueAsTypeMirror();
                        Optional<Type> type = TypeBuildingVisitor.INSTANCE.visit(expanded, TypeBuildingVisitor.Context.VERBOSE);
                        
                        Optional<Comment> comment = JavadocCommentsVisitor.visit(typeElement);
                        ExtraVisitor.Context extraContext = ExtraVisitor.Context.from(typeElement, Extra.EMPTY);
                        typeElement.accept(ExtraVisitor.INSTANCE, extraContext);
                        Extra extra = extraContext.extra();
                        
                        BiMap<TypeElement, TypeElement> nativeRegistry = nativeRegistry(roundEnvironment);
                        List<TypeElement> superTypes = Arrays.stream(ElementUtils.AccessTypeHierarchy.getSuperTypeElements(nativeRegistry.get(typeElement)))
                                .filter(nativeRegistry::containsValue)
                                .map(nativeRegistry.inverse()::get).collect(Collectors.toList());
                        
                        List<TypeMembersVisitor.NativeConstructorInfo> nativeConstructors = Lists.transform(Arrays.asList(nativeWrapper.constructors()), TypeMembersVisitor.NativeConstructorInfo::of);
                        List<NativeMethodWrapper> nativeMethodAnnotations = Lists.transform(Util.getAnnotationsOfType(typeElement, NativeMethod.class, NativeMethod.Holder.class), NativeMethodWrapper::wrap);
                        List<TypeMembersVisitor.NativeMethodInfo> nativeMethods = Lists.transform(nativeMethodAnnotations, TypeMembersVisitor.NativeMethodInfo::of);
                        TypeMembersVisitor.NativeTypeInfo info = new TypeMembersVisitor.NativeTypeInfo(typeElement, expanded, nativeConstructors, nativeMethods);
                        Map<String, MemberGroup> members = getMemberGroups(typeElement, superTypes, Optional.of(info));
                        
                        type.map(foundType -> {
                            if(Util.isEvent(typeElement)) {
                                return createEventPage(typeElement, key, displayName, comment, extra, foundType, zenCodeName, members);
                            } else if(TypeMirrorWrapper.isEnum(expanded)) {
                                return new EnumPage(PageVersion.ONE, key, displayName, comment, extra, foundType, zenCodeName, members);
                            } else {
                                return new TypePage(PageVersion.ONE, key, displayName, comment, extra, foundType, zenCodeName, members);
                            }
                        }).ifPresent(typePage -> DocFolder.ROOT.child(pageParents, typePage));
                    }
                    if(isName) {
                        NameWrapper nameWrapper = Objects.requireNonNull(NameWrapper.wrap(typeElement));
                        
                        Optional<Comment> comment = JavadocCommentsVisitor.visit(typeElement);
                        ExtraVisitor.Context extraContext = ExtraVisitor.Context.from(typeElement, Extra.EMPTY);
                        typeElement.accept(ExtraVisitor.INSTANCE, extraContext);
                        Extra extra = extraContext.extra();
                        Optional<Type> type = TypeBuildingVisitor.INSTANCE.visit(typeElement.asType(), TypeBuildingVisitor.Context.VERBOSE);
                        String zenCodeName = nameWrapper.value();
                        Map<String, MemberGroup> members = getMemberGroups(typeElement, Arrays.asList(ElementUtils.AccessTypeHierarchy.getSuperTypeElements(typeElement)), Optional.empty());
                        
                        type.map(foundType -> {
                            if(Util.isEvent(typeElement)) {
                                return createEventPage(typeElement, key, displayName, comment, extra, foundType, zenCodeName, members);
                            } else if(ElementUtils.CheckKindOfElement.isEnum(typeElement)) {
                                return new EnumPage(PageVersion.ONE, key, displayName, comment, extra, foundType, zenCodeName, members);
                            } else {
                                return new TypePage(PageVersion.ONE, key, displayName, comment, extra, foundType, zenCodeName, members);
                            }
                        }).ifPresent(typePage -> DocFolder.ROOT.child(pageParents, typePage));
                        
                    }
                });
    }
    
    
    private EventPage createEventPage(TypeElement typeElement, String key, String displayName, Optional<Comment> comment, Extra extra, Type type, String zenCodeName, Map<String, MemberGroup> members) {
        
        boolean cancelable = ElementUtils.AccessEnclosedElements.getEnclosedFields(typeElement)
                .stream()
                .filter(BusWrapper::isAnnotated)
                .anyMatch(variableElement -> CancelableTreeVisitor.INSTANCE.visit(trees()
                        .getTree(variableElement)));
        boolean hasResult = ElementUtils.AccessEnclosedElements.getEnclosedFields(typeElement)
                .stream()
                .filter(BusWrapper::isAnnotated)
                .map(VariableElement::asType)
                .map(TypeMirrorWrapper::getTypeArguments)
                .flatMap(Collection::stream)
                .map(TypeUtils.TypeRetrieval::getTypeElement)
                .anyMatch(HasResultWrapper::isAnnotated);
        JavadocEventVisitor.Context context = new JavadocEventVisitor.Context();
        HTMLAwareJavadocDescription.parse(typeElement)
                .ifPresent(javadoc -> javadoc.accept(JavadocEventVisitor.INSTANCE, context));
        return new EventPage(PageVersion.ONE, key, displayName, comment, extra, type, zenCodeName, members, context.canceledInfo(), context.notCanceledInfo(), context.allowInfo(), context.defaultInfo(), context.denyInfo(), hasResult, cancelable);
    }
    
    private Map<String, MemberGroup> getMemberGroups(TypeElement typeElement, List<TypeElement> superTypes, Optional<TypeMembersVisitor.NativeTypeInfo> nativeInfo) {
        
        TypeMembersVisitor.Context context = new TypeMembersVisitor.Context(typeElement, nativeInfo);
        
        List<Element> declaredElements = new LinkedList<>(ElementUtils.AccessEnclosedElements.getEnclosedElementsOfKind(typeElement, ElementKind.FIELD, ElementKind.METHOD, ElementKind.CONSTRUCTOR, ElementKind.ENUM_CONSTANT));
        Map<TypeElement, List<Element>> inheritedElements = new LinkedHashMap<>();
        Predicate<Element> existingPred = input -> declaredElements.stream()
                .noneMatch(element -> Util.isMethodSame(element, input));
        for(TypeElement superType : superTypes) {
            List<Element> elements = inheritedElements.computeIfAbsent(superType, key -> new LinkedList<>());
            elements.addAll(Collections2.filter(ElementUtils.AccessEnclosedElements.getEnclosedElementsOfKind(superType, ElementKind.FIELD, ElementKind.METHOD, ElementKind.CONSTRUCTOR, ElementKind.ENUM_CONSTANT), existingPred));
        }
        
        if(nativeInfo.isPresent()) {
            TypeElement expandedTE = TypeUtils.TypeRetrieval.getTypeElement(nativeInfo.get().expandedTypeMirror());
            if(ElementUtils.CheckKindOfElement.isEnum(expandedTE)) {
                declaredElements.addAll(ElementUtils.AccessEnclosedElements.getEnclosedElementsOfKind(expandedTE, ElementKind.ENUM_CONSTANT));
            }
        }
        
        Map<String, List<Member>> members = new LinkedHashMap<>();
        for(Element element : declaredElements) {
            Map<String, List<Member>> accept = element.accept(TypeMembersVisitor.INSTANCE, context);
            for(String key : accept.keySet()) {
                members.merge(key, accept.get(key), (members1, members2) -> {
                    members1.addAll(members2);
                    return members1;
                });
            }
        }
        for(TypeElement owner : inheritedElements.keySet()) {
            for(Element element : inheritedElements.get(owner)) {
                Map<String, List<Member>> accept = element.accept(TypeMembersVisitor.INSTANCE, context);
                for(String key : accept.keySet()) {
                    members.merge(key, accept.get(key), (members1, members2) -> {
                        members1.addAll(members2);
                        return members1;
                    });
                }
            }
        }
        
        nativeInfo.ifPresent(info -> {
            for(TypeMembersVisitor.NativeConstructorInfo constructor : info.constructor()) {
                
                Optional<Comment> comment = constructor.description()
                        .map(s -> new RootComment(Collections.singletonList(new PlaintextComment(s))));
                Extra extra = constructor.getExtra();
                List<Parameter> params = constructor.params()
                        .stream()
                        .map(TypeMembersVisitor.NativeConstructorParam::asParameter)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
                members.computeIfAbsent("new", s -> new LinkedList<>())
                        .add(new ConstructorMember(MemberOrigin.NATIVE, comment, extra, params, Collections.emptyMap()));
            }
            //TODO replace the whole @NativeMethod system to be part of @NativeTypeRegistration, like constructors
            Optional<TypeElementWrapper> expandedTypeWrapper = TypeMirrorWrapper.getTypeElement(info.expandedTypeMirror());
            if(expandedTypeWrapper.isPresent()) {
                TypeElementWrapper expandedElement = expandedTypeWrapper.get();
                for(TypeMembersVisitor.NativeMethodInfo method : info.methods()) {
                    List<Member> nativeMembers = members.computeIfAbsent(method.name(), s -> new LinkedList<>());
                    
                    Optional<ExecutableElement> match = method.match(expandedElement.unwrap());
                    if(match.isPresent()) {
                        ExecutableElement foundMethod = match.get();
                        Optional<Type> foundReturnType = TypeBuildingVisitor.INSTANCE.visit(foundMethod.getReturnType(), TypeBuildingVisitor.Context.TYPE_ARGUMENTS);
                        if(foundReturnType.isPresent()) {
                            Type returnType = foundReturnType.get();
                            List<Parameter> params = method.asParameters();
                            // Add the method
                            nativeMembers.add(new MethodMember(method.name(), method.name(), MemberOrigin.NATIVE, false, Optional.empty(), Extra.EMPTY, params, returnType, Collections.emptyMap()));
                            // Add the getter
                            method.getterName()
                                    .map(s -> new GetterMember(s, s, MemberOrigin.NATIVE, false, Optional.empty(), Extra.EMPTY, returnType))
                                    .ifPresent(nativeMembers::add);
                            // Add the setter
                            method.setterName()
                                    .map(s -> new SetterMember(s, s, MemberOrigin.NATIVE, false, Optional.empty(), Extra.EMPTY, params))
                                    .ifPresent(nativeMembers::add);
                        }
                    }
                    
                }
            }
            
        });
        
        return Maps.transformEntries(members, MemberGroup::new);
    }
    
    public void handleLastRound() {
        
        writeDocsJsonFile();
    }
    
    private void writeDocsJsonFile() {
        
        DocFolder.ROOT.write(outputDirectory);
        Path docsJson = outputDirectory.resolve("docs.json");
        Optional<JsonObject> originalNav = Optional.empty();
        if(multiSourceProject && Files.exists(docsJson)) {
            try(final BufferedReader reader = Files.newBufferedReader(docsJson)) {
                
                originalNav = Optional.of(Tools.GSON.fromJson(reader, JsonObject.class));
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        DocJson docJson = new DocJson(DocFolder.ROOT);
        JsonElement result = DocJson.CODEC.encodeStart(JsonOps.INSTANCE, docJson)
                .getOrThrow(false, System.out::println);
        originalNav.ifPresent(jsonObject -> Util.deepMerge(jsonObject, ((JsonObject) result)));
        try(BufferedWriter writer = Files.newBufferedWriter(docsJson)) {
            Tools.GSON.toJson(Util.sortObjectsFirst(result), writer);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Trees trees() {
        
        return Tools.TREES.apply(ToolingProvider.getTooling().getProcessingEnvironment());
    }
    
    public BiMap<TypeElement, TypeElement> nativeRegistry(RoundEnvironment environment) {
        
        return NATIVE_REGISTRY.apply(environment);
    }
    
    
}
