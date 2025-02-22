package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CodeComment extends Comment {
    
    public static final Codec<CodeComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.listOf().fieldOf("content").forGetter(CodeComment::content)
    ).apply(instance, CodeComment::new));
    
    private final List<Comment> content;
    
    public CodeComment(List<Comment> content) {
        
        super(CommentKind.CODE);
        this.content = content;
    }
    
    
    public List<Comment> content() {
        
        return content;
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.content().stream().allMatch(Comment::isEmpty);
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof CodeComment) {
            List<Comment> content = new LinkedList<>();
            content.addAll(this.content);
            content.addAll(((CodeComment) other).content());
            return Optional.of(new CodeComment(content));
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        
        return "CodeComment{" +
                "content=" + content +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        CodeComment that = (CodeComment) o;
        return Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(content);
    }
    
}
