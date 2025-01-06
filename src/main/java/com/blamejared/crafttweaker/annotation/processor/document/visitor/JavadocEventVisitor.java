package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
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

import java.util.Optional;

public class JavadocEventVisitor implements JavadocElementVisitor<Void, JavadocEventVisitor.Context> {
    
    public static final JavadocEventVisitor INSTANCE = new JavadocEventVisitor();
    
    private JavadocEventVisitor() {}
    
    public void visitContainer(JavaDocContainerElement element, JavadocEventVisitor.Context context) {
        
        element.elements().forEach(javadocElement -> javadocElement.accept(this, context));
    }
    
    @Override
    public Void visit(JavadocAuthorTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocDeprecatedTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocParamTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocReturnTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocSeeTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocSinceTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocVersionTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocBrTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocCodeTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocEmTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocStrongTag element, Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocLiTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocPTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocUlTag element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @Override
    public Void visit(JavadocDocRootTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocInheritDocTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocInlineCodeTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLinkPlainTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLinkTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocLiteralTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocUnknownTag element, JavadocEventVisitor.Context context) {
        
        switch(element.name()) {
            case "canceled":
                //TODO this should ideally be fancy and not just a string
                context.canceledInfo(Optional.of(new PlaintextComment(element.content())));
                break;
            case "notCanceled":
                //TODO this should ideally be fancy and not just a string
                context.notCanceledInfo(Optional.of(new PlaintextComment(element.content())));
                break;
            case "allow":
                //TODO this should ideally be fancy and not just a string
                context.allowInfo(Optional.of(new PlaintextComment(element.content())));
                break;
            case "default":
                //TODO this should ideally be fancy and not just a string
                context.defaultInfo(Optional.of(new PlaintextComment(element.content())));
                break;
            case "deny":
                //TODO this should ideally be fancy and not just a string
                context.denyInfo(Optional.of(new PlaintextComment(element.content())));
                break;
        }
        return null;
    }
    
    @Override
    public Void visit(JavadocUnknownBlockTag element, Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocValueTag element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocSnippet element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocNewLine element, JavadocEventVisitor.Context context) {
        
        return null;
    }
    
    @Override
    public Void visit(JavadocRootElement element, JavadocEventVisitor.Context context) {
        
        visitContainer(element, context);
        return null;
    }
    
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Context {
        
        private Optional<Comment> canceledInfo = Optional.empty();
        private Optional<Comment> notCanceledInfo = Optional.empty();
        private Optional<Comment> allowInfo = Optional.empty();
        private Optional<Comment> defaultInfo = Optional.empty();
        private Optional<Comment> denyInfo = Optional.empty();
        
        public Context() {
        
        }
        
        public Optional<Comment> canceledInfo() {
            
            return canceledInfo;
        }
        
        public Optional<Comment> notCanceledInfo() {
            
            return notCanceledInfo;
        }
        
        public Optional<Comment> allowInfo() {
            
            return allowInfo;
        }
        
        public Optional<Comment> defaultInfo() {
            
            return defaultInfo;
        }
        
        public Optional<Comment> denyInfo() {
            
            return denyInfo;
        }
        
        public void canceledInfo(Optional<Comment> canceledInfo) {
            
            this.canceledInfo = canceledInfo;
        }
        
        public void notCanceledInfo(Optional<Comment> notCanceledInfo) {
            
            this.notCanceledInfo = notCanceledInfo;
        }
        
        public void allowInfo(Optional<Comment> allowInfo) {
            
            this.allowInfo = allowInfo;
        }
        
        public void defaultInfo(Optional<Comment> defaultInfo) {
            
            this.defaultInfo = defaultInfo;
        }
        
        public void denyInfo(Optional<Comment> denyInfo) {
            
            this.denyInfo = denyInfo;
        }
        
    }
    
}
