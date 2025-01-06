package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Objects;

public class LinkComment extends Comment {
    
    public static final Codec<LinkComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("to").forGetter(LinkComment::to),
            Comment.CODEC.listOf().fieldOf("content").forGetter(LinkComment::content),
            Codec.BOOL.fieldOf("plain").forGetter(LinkComment::plain)
    ).apply(instance, LinkComment::new));
    
    private final String to;
    private final List<Comment> content;
    private final boolean plain;
    
    public LinkComment(String to, List<Comment> content, boolean plain) {
        
        super(CommentKind.LINK);
        this.to = to;
        this.content = content;
        this.plain = plain;
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.to().isEmpty() && this.content().stream().allMatch(Comment::isEmpty);
    }
    
    public String to() {
        
        return to;
    }
    
    public List<Comment> content() {
        
        return content;
    }
    
    public boolean plain() {
        
        return plain;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        LinkComment that = (LinkComment) o;
        return plain == that.plain && Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        int result = Objects.hashCode(content);
        result = 31 * result + Boolean.hashCode(plain);
        return result;
    }
    
    
    @Override
    public String toString() {
        
        return "LinkComment{" +
                "content=" + content +
                ", plain=" + plain +
                '}';
    }
    
}
