package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.HTMLAwareJavadocDescription;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocNewLine;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocRootElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocSnippet;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocAuthorTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocDeprecatedTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocParamTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocReturnTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSeeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocSinceTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocUnknownBlockTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block.JavadocVersionTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocBrTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocEmTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocLiTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocPTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocStrongTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocUlTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocDocRootTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInheritDocTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocInlineCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkPlainTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLinkTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocLiteralTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocUnknownTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.inline.JavadocValueTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.PlaintextComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.BracketEnum;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Deprecated;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.EventParent;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Examples;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Loaders;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Obtention;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.ParameterComments;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.RequiredMods;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Returns;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.See;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Since;
import com.blamejared.crafttweaker.annotation.processor.util.Optionull;
import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.blamejared.crafttweaker.api.annotation.ZenRegisterWrapper;
import com.blamejared.crafttweaker_annotations.annotations.BracketEnumWrapper;
import com.blamejared.crafttweaker_annotations.annotations.DocumentWrapper;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.ProcessingEnvironmentUtils;
import net.minecraftforge.eventbus.api.Event;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ExtraVisitor implements ElementVisitor<Void, ExtraVisitor.Context>, TypeVisitor<Void, ExtraVisitor.Context>, JavadocElementVisitor<Void, ExtraVisitor.Context> {
    
    public static final ExtraVisitor INSTANCE = new ExtraVisitor();
    
    private ExtraVisitor() {
    
    }
    
    private void visitContainer(JavaDocContainerElement element, ExtraVisitor.Context context) {
        
        element.elements().forEach(javadocElement -> javadocElement.accept(this, context));
    }
    
    public Optional<Comment> commentFromElements(JavaDocContainerElement element, ExtraVisitor.Context context) {
        
        return context.pkg().flatMap(pkg -> {
            List<JavadocElement> elements = element.elements();
            JavadocElement javadocElement = null;
            if(elements.isEmpty()) {
                return Optional.empty();
            }
            if(elements.size() == 1) {
                javadocElement = elements.get(0);
            } else {
                javadocElement = new JavadocRootElement(elements);
            }
            return javadocElement.accept(JavadocCommentsVisitor.INSTANCE, new JavadocCommentsVisitor.Context(pkg, context.imports()));
        });
    }
    
    @Override
    public Void visit(JavadocAuthorTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocDeprecatedTag element, Context context) {
        
        Optional<Comment> reason = commentFromElements(element, context);
        context.add(extra -> extra.withDeprecated(new Deprecated(reason, Optional.empty(), false), Deprecated::merge));
        return null;
    }
    
    @Override
    public Void visit(JavadocParamTag element, Context context) {
        
        Optional<Comment> description = commentFromElements(element, context);
        description.ifPresent(comment -> {
            context.addParameterComment(element.paramName(), comment);
        });
        return null;
    }
    
    @Override
    public Void visit(JavadocReturnTag element, Context context) {
        
        Optional<Comment> what = commentFromElements(element, context);
        what.map(Returns::new).ifPresent(returns -> {
            context.add(extra -> extra.withReturns(returns));
        });
        return null;
    }
    
    @Override
    public Void visit(JavadocSeeTag element, Context context) {
        
        Optional<Comment> what = commentFromElements(element, context);
        what.map(See::new).ifPresent(see -> context.add(extra -> extra.withSee(see, See::merge)));
        return null;
    }
    
    
    @Override
    public Void visit(JavadocSinceTag element, Context context) {
        
        Optional<Comment> when = commentFromElements(element, context);
        when.map(Since::new).ifPresent(since -> context.add(extra -> extra.withSince(since)));
        return null;
    }
    
    @Override
    public Void visit(JavadocVersionTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocBrTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocCodeTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocEmTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocStrongTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocLiTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocPTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocUlTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocDocRootTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocInheritDocTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocInlineCodeTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLinkPlainTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLinkTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLiteralTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocUnknownTag element, Context context) {
        
        switch(element.name()) {
            case "docObtention":
                //TODO this should ideally be fancy and not just a string
                context.add(extra -> extra.withObtention(new Obtention(new PlaintextComment(element.content()))));
                break;
            case "docParam":
                String[] info = element.content().split("\\s", 2);
                context.addExample(info[0], info[1]);
                break;
        }
        return null;
    }
    
    @Override
    public Void visit(JavadocUnknownBlockTag element, Context context) {
        
        switch(element.name()) {
            case "docObtention":
                commentFromElements(element, context).ifPresent(comment1 -> {
                    context.add(extra -> extra.withObtention(new Obtention(comment1)));
                });
                break;
            case "docParam":
                String[] info = element.content().split("\\s", 2);
                context.addExample(info[0], info[1]);
                break;
        }
        return null;
    }
    
    @Override
    public Void visit(JavadocValueTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocSnippet element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocNewLine element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocRootElement element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(Element e, Context context) {
        
        return e.accept(this, context);
    }
    
    @Override
    public Void visitPackage(PackageElement e, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitType(TypeElement e, Context context) {
        
        HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> {
            javadoc.accept(this, context);
        });
        
        Optionull.ifPresent(DocumentWrapper.wrap(e), DocumentWrapper::requiredMods, requiredMods -> {
            List<RequiredMods.Mod> mods = Arrays.stream(requiredMods)
                    .map(modWrapper -> new RequiredMods.Mod(modWrapper.modid(), modWrapper.displayName(), modWrapper.url()))
                    .collect(Collectors.toList());
            if(!mods.isEmpty()) {
                context.add(extra -> extra.withRequiredMods(new RequiredMods(mods)));
            }
            
        });
        
        Optionull.ifPresent(ZenRegisterWrapper.wrap(e), zenRegister -> new Loaders(zenRegister.loaders()), loaders -> {
            context.add(extra -> extra.withLoaders(loaders));
        });
        Optionull.ifPresent(BracketEnumWrapper.wrap(e), bracketEnumWrapper -> new BracketEnum(bracketEnumWrapper.value()), bracketEnum -> {
            context.add(extra -> extra.withBracketEnum(bracketEnum));
        });
        Optionull.ifPresent(e.getAnnotation(java.lang.Deprecated.class), depAnn -> {
            context.add(extra -> {
                Deprecated newDep = new Deprecated(Optional.empty(), Optional.of(new PlaintextComment(depAnn.since())), depAnn.forRemoval());
                return extra.withDeprecated(newDep, Deprecated::merge);
            });
            HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> javadoc.accept(this, context));
        });
        
        Optionull.ifPresent(ProcessingEnvironmentUtils.getElements().getTypeElement(Event.class.getName()), type -> {
            if(Util.isSubTypeOrGeneric(e.asType(), type.asType())) {
                context.add(extra -> extra.withEventParent(EventParent.INSTANCE));
            }
        });
        return null;
    }
    
    @Override
    public Void visitVariable(VariableElement e, Context context) {
        
        HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> {
            javadoc.accept(this, context);
        });
        Optionull.ifPresent(e.getAnnotation(java.lang.Deprecated.class), depAnn -> {
            context.add(extra -> extra.withDeprecated(new Deprecated(Optional.empty(), Optional.of(new PlaintextComment(depAnn.since())), depAnn.forRemoval()), Deprecated::merge));
            HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> javadoc.accept(this, context));
        });
        return null;
    }
    
    @Override
    public Void visitExecutable(ExecutableElement e, Context context) {
        
        HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> {
            javadoc.accept(this, context);
        });
        Optionull.ifPresent(e.getAnnotation(java.lang.Deprecated.class), depAnn -> {
            context.add(extra -> extra.withDeprecated(new Deprecated(Optional.empty(), Optional.of(new PlaintextComment(depAnn.since())), depAnn.forRemoval()), Deprecated::merge));
            HTMLAwareJavadocDescription.parse(e).ifPresent(javadoc -> javadoc.accept(this, context));
        });
        return null;
    }
    
    @Override
    public Void visitTypeParameter(TypeParameterElement e, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitUnknown(Element e, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(TypeMirror t, Context context) {
        
        return t.accept(this, context);
    }
    
    @Override
    public Void visitPrimitive(PrimitiveType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitNull(NullType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitArray(ArrayType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitDeclared(DeclaredType t, Context context) {
        //TODO read the javadoc?
        return null;
    }
    
    @Override
    public Void visitError(ErrorType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitTypeVariable(TypeVariable t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitWildcard(WildcardType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitExecutable(ExecutableType t, Context context) {
        //TODO read the javadoc?
        return null;
    }
    
    @Override
    public Void visitNoType(NoType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitUnknown(TypeMirror t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitUnion(UnionType t, Context context) {
        
        return null;
    }
    
    @Override
    public Void visitIntersection(IntersectionType t, Context context) {
        
        return null;
    }
    
    public static class Context {
        
        private final Optional<String> pkg;
        private final Map<String, String> imports;
        private Extra extra;
        
        private final Map<String, List<String>> examples = new LinkedHashMap<>();
        private final Map<String, Comment> comments = new LinkedHashMap<>();
        
        public static ExtraVisitor.Context from(Element element, Extra extra) {
            
            Trees trees = Tools.TREES.apply(ToolingProvider.getTooling().getProcessingEnvironment());
            TreePath path = trees.getPath(element);
            return Optional.ofNullable(trees.getPath(element)).map(trees::getDocComment).map(docComment -> {
                Map<String, String> imports = new HashMap<>();
                ImportVisitor visitor = new ImportVisitor();
                path.getCompilationUnit().accept(visitor, imports);
                if(element instanceof TypeElement type) {
                    imports.putIfAbsent(type.getSimpleName().toString(), ToolingProvider.getTooling()
                            .getElements()
                            .getPackageOf(element)
                            .getQualifiedName()
                            .toString());
                    Arrays.stream(ElementUtils.AccessTypeHierarchy.getSuperTypeElements(type))
                            .map(trees::getPath)
                            .filter(Objects::nonNull)
                            .forEach(trees1 -> trees1.getCompilationUnit().accept(visitor, imports));
                } else if(element.getEnclosingElement() instanceof TypeElement type) {
                    imports.putIfAbsent(type.getSimpleName().toString(), ToolingProvider.getTooling()
                            .getElements()
                            .getPackageOf(element)
                            .getQualifiedName()
                            .toString());
                }
                
                Optional<String> pkg = Util.getPackageName(element.asType())
                        .or(() -> Util.getPackageName(element.getEnclosingElement().asType()));
                return new ExtraVisitor.Context(pkg, imports, extra);
            }).orElseGet(() -> new Context(Optional.empty(), Collections.emptyMap(), extra));
        }
        
        public Context(Optional<String> pkg, Map<String, String> imports, Extra extra) {
            
            this.pkg = pkg;
            this.imports = imports;
            this.extra = extra;
        }
        
        public Optional<String> pkg() {
            
            return pkg;
        }
        
        public Map<String, String> imports() {
            
            return imports;
        }
        
        public Extra extra() {
            
            return extra;
        }
        
        public void add(UnaryOperator<Extra> updater) {
            
            this.extra = updater.apply(extra);
        }
        
        //        public <T extends Extra> void add(T extra, BiFunction<T, T, T> updater) {
        //
        //            String key = extra.kind().key();
        //            if(this.extra.containsKey(key)) {
        //                this.extra.compute(key, (s, extra1) -> updater.apply((T) extra1, extra));
        //            } else {
        //                this.extra.put(key, extra);
        //            }
        //        }
        
        public void addExample(String key, String example) {
            
            if(examples.isEmpty()) {
                this.add(extra -> extra.withExamples(new Examples(this.examples)));
            }
            examples.computeIfAbsent(key, k -> new ArrayList<>()).add(example);
        }
        
        public void addParameterComment(String key, Comment comment) {
            
            if(comments.isEmpty()) {
                this.add(extra -> extra.withParameterComment(new ParameterComments(this.comments)));
            }
            comments.put(key, comment);
        }
        
    }
    
}
