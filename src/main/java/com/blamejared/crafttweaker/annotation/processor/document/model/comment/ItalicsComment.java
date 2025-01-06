package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ItalicsComment extends Comment {
    
    public static final Codec<ItalicsComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.listOf().fieldOf("content").forGetter(ItalicsComment::content)
    ).apply(instance, ItalicsComment::new));
    
    private final List<Comment> content;
    
    public ItalicsComment(List<Comment> content) {
        
        super(CommentKind.ITALICS);
        this.content = content;
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.content().stream().allMatch(Comment::isEmpty);
    }
    
    public List<Comment> content() {
        
        return content;
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof ItalicsComment) {
            List<Comment> content = new LinkedList<>();
            content.addAll(this.content);
            content.addAll(((ItalicsComment) other).content());
            return Optional.of(new ItalicsComment(content));
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        
        return "ItalicsComment{" +
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
        
        ItalicsComment that = (ItalicsComment) o;
        return Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(content);
    }
    
}
