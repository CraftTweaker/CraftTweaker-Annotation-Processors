package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;
import java.util.Optional;

public class PlaintextComment extends Comment {
    
    public static final Codec<PlaintextComment> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("content").forGetter(PlaintextComment::content)
    ).apply(instance, PlaintextComment::new));
    
    private final String content;
    
    public PlaintextComment(String content) {
        
        super(CommentKind.PLAINTEXT);
        this.content = content;
    }
    
    @Override
    public boolean isEmpty() {
        
        return this.content().trim().isEmpty();
    }
    
    public String content() {
        
        return content;
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof PlaintextComment) {
            return Optional.of(new PlaintextComment(this.content + " " + ((PlaintextComment) other).content()));
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        
        return "PlaintextComment{" +
                "content='" + content + '\'' +
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
        
        PlaintextComment that = (PlaintextComment) o;
        return Objects.equals(content, that.content);
    }
    
    @Override
    public int hashCode() {
        
        return Objects.hashCode(content);
    }
    
}
