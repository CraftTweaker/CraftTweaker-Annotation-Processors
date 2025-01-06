package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RootComment extends Comment {
    
    public static final Codec<RootComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.listOf().fieldOf("children").forGetter(RootComment::children)
    ).apply(instance, RootComment::new));
    
    private final List<Comment> children;
    
    public RootComment(List<Comment> children) {
        
        super(CommentKind.ROOT);
        this.children = children;
    }
    
    public RootComment(Comment child) {
        
        super(CommentKind.ROOT);
        this.children = Arrays.asList(child);
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.children().stream().allMatch(Comment::isEmpty);
    }
    
    public List<Comment> children() {
        
        return children;
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof RootComment) {
            List<Comment> children = new LinkedList<>();
            children.addAll(this.children());
            children.addAll(((RootComment) other).children());
            return Optional.of(new RootComment(children));
        }
        return Optional.empty();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        RootComment that = (RootComment) o;
        return Objects.equals(children, that.children);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(children);
    }
    
    @Override
    public String toString() {
        
        return "RootComment{" +
                "children=" + children +
                '}';
    }
    
}
