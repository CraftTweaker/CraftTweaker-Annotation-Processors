package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocStrongTag;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.BoldComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.CodeComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.ItalicsComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.LinkComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.ListComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.ListItemComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.NewlineComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.ParagraphComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.PlaintextComment;
import com.blamejared.crafttweaker.annotation.processor.document.model.comment.RootComment;
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
import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class JavadocCommentsVisitor implements JavadocElementVisitor<Optional<Comment>, JavadocCommentsVisitor.Context> {
    
    public static final JavadocCommentsVisitor INSTANCE = new JavadocCommentsVisitor();
    
    public static Optional<Comment> visit(Element element) {
        
        Trees trees = Tools.TREES.apply(ToolingProvider.getTooling().getProcessingEnvironment());
        return HTMLAwareJavadocDescription.parse(element)
                .flatMap(javadocRootElement ->
                        Context.from(trees, element)
                                .flatMap(context ->
                                        javadocRootElement.accept(JavadocCommentsVisitor.INSTANCE, context)));
        
    }
    
    private JavadocCommentsVisitor() {
    
    }
    
    public List<Comment> getContainerComments(JavaDocContainerElement element, Context context) {
        
        List<Comment> merged = new LinkedList<>();
        Comment last = null;
        
        for(JavadocElement javadocElement : element.elements()) {
            Optional<Comment> comment = javadocElement.accept(this, context);
            if(comment.isPresent()) {
                Comment current = comment.get();
                if(last == null) {
                    last = current;
                } else {
                    Optional<Comment> collapsed = last.collapse(current);
                    if(collapsed.isPresent()) {
                        last = collapsed.get();
                    } else {
                        if(!last.isEmpty()) {
                            merged.add(last);
                            last = current;
                        }
                    }
                }
            }
        }
        if(last != null) {
            merged.add(last);
        }
        
        Comment current;
        while(!merged.isEmpty() && ((current = merged.get(merged.size() - 1)) instanceof NewlineComment || current.isEmpty())) {
            merged.remove(merged.size() - 1);
        }
        
        return merged;
    }
    
    private LinkComment visitLink(String content, Context context, boolean plain) {
        
        String[] info = content.trim().split(" ", 2);
        String linkTo = info[0];
        if(context.imports().containsKey(linkTo)) {
            linkTo = context.imports().get(linkTo);
        } else if(context.pkg() != null) {
            TypeElement needle = TypeUtils.TypeRetrieval.getTypeElement(context.pkg() + "." + linkTo);
            if(needle != null) {
                linkTo = TypeMirrorWrapper.getQualifiedName(needle.asType());
            }
        }
        // TODO links do not work for same class, '#addAll'
        
        return new LinkComment(linkTo, Arrays.asList(new PlaintextComment(info.length > 1 ? info[1] : info[0])), plain);
    }
    
    @Override
    public Optional<Comment> visit(JavadocAuthorTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocDeprecatedTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocParamTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocReturnTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocSeeTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocSinceTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocVersionTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocBrTag element, Context context) {
        
        return Optional.of(new NewlineComment());
    }
    
    @Override
    public Optional<Comment> visit(JavadocCodeTag element, Context context) {
        
        return Optional.of(new CodeComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocEmTag element, Context context) {
        
        return Optional.of(new ItalicsComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocStrongTag element, Context context) {
        
        return Optional.of(new BoldComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocLiTag element, Context context) {
        
        return Optional.of(new ListItemComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocPTag element, Context context) {
        
        return Optional.of(new ParagraphComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocUlTag element, Context context) {
        
        return Optional.of(new ListComment(getContainerComments(element, context)));
    }
    
    @Override
    public Optional<Comment> visit(JavadocDocRootTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocInheritDocTag element, Context context) {
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Comment> visit(JavadocInlineCodeTag element, Context context) {
        
        return Optional.of(new CodeComment(Arrays.asList(new PlaintextComment(element.content()))));
    }
    
    @Override
    public Optional<Comment> visit(JavadocLinkPlainTag element, Context context) {
        
        return Optional.of(visitLink(element.content(), context, true));
    }
    
    @Override
    public Optional<Comment> visit(JavadocLinkTag element, Context context) {
        
        return Optional.of(visitLink(element.content(), context, false));
    }
    
    @Override
    public Optional<Comment> visit(JavadocLiteralTag element, Context context) {
        
        return Optional.of(new PlaintextComment(element.content()));
    }
    
    @Override
    public Optional<Comment> visit(JavadocUnknownTag element, Context context) {
        
        if(element.name().equals("docObtention") || element.name().equals("docParam")) {
            return Optional.empty();
        }
        return Optional.of(new PlaintextComment(element.content()));
    }
    
    @Override
    public Optional<Comment> visit(JavadocUnknownBlockTag element, Context context) {
        
        if(element.name().equals("docObtention") || element.name().equals("docParam")) {
            return Optional.empty();
        }
        
        List<Comment> containerComments = getContainerComments(element, context);
        if(containerComments.isEmpty()) {
            return Optional.empty();
        }
        if(containerComments.size() == 1) {
            return Optional.ofNullable(containerComments.get(0));
        }
        return Optional.of(new ListComment(containerComments));
    }
    
    @Override
    public Optional<Comment> visit(JavadocValueTag element, Context context) {
        
        return Optional.of(new PlaintextComment(element.content()));
    }
    
    @Override
    public Optional<Comment> visit(JavadocSnippet element, Context context) {
        
        return Optional.of(new PlaintextComment(element.text()));
    }
    
    @Override
    public Optional<Comment> visit(JavadocNewLine element, Context context) {
        
        return Optional.of(new NewlineComment());
    }
    
    @Override
    public Optional<Comment> visit(JavadocRootElement element, Context context) {
        
        List<Comment> children = getContainerComments(element, context);
        return Optional.of(new RootComment(children));
    }
    
    public static class Context {
        
        private final String pkg;
        private final Map<String, String> imports;
        
        public static Optional<Context> from(Trees trees, Element element) {
            
            TreePath path = trees.getPath(element);
            String docComment = trees.getDocComment(path);
            if(docComment != null) {
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
                
                
                String pkg = TypeMirrorWrapper.getPackage(element.asType());
                if(pkg == null) {
                    pkg = TypeMirrorWrapper.getPackage(element.getEnclosingElement().asType());
                }
                return Optional.of(new Context(pkg, imports));
            }
            return Optional.empty();
        }
        
        public Context(String pkg, Map<String, String> imports) {
            
            this.pkg = pkg;
            this.imports = imports;
        }
        
        public String pkg() {
            
            return pkg;
        }
        
        public Map<String, String> imports() {
            
            return imports;
        }
        
    }
    
}
