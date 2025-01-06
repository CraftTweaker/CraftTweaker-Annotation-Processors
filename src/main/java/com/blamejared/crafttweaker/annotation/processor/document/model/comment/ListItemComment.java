package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Objects;

public class ListItemComment extends Comment {
    
    public static final Codec<ListItemComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.listOf().fieldOf("content").forGetter(ListItemComment::contents)
    ).apply(instance, ListItemComment::new));
    
    private final List<Comment> content;
    
    public ListItemComment(List<Comment> contents) {
        
        super(CommentKind.LIST_ITEM);
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
    public String toString() {
        
        return "ListItemComment{" +
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
        
        ListItemComment that = (ListItemComment) o;
        return Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(content);
    }
    
}
