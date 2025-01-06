package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ListComment extends Comment {
    
    public static final Codec<ListComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.listOf().fieldOf("content").forGetter(ListComment::contents)
    ).apply(instance, ListComment::new));
    
    private final List<Comment> content;
    
    public ListComment(List<Comment> contents) {
        
        super(CommentKind.LIST);
        this.content = contents;
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.contents().stream().allMatch(Comment::isEmpty);
    }
    
    public List<Comment> contents() {
        
        return content;
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof ListComment) {
            List<Comment> content = new LinkedList<>();
            content.addAll(this.contents());
            content.addAll(((ListComment) other).contents());
            return Optional.of(new ListComment(content));
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        
        return "ListComment{" +
                "contents=" + content +
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
        
        ListComment that = (ListComment) o;
        return Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(content);
    }
    
}
