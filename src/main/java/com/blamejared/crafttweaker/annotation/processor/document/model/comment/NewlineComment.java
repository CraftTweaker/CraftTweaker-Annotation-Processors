package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;

import java.util.Optional;

public class NewlineComment extends Comment {
    
    public static final Codec<NewlineComment> CODEC = Codec.unit(NewlineComment::new);
    
    public NewlineComment() {
        
        super(CommentKind.NEW_LINE);
    }
    
    @Override
    public Optional<Comment> collapse(Comment other) {
        
        if(other instanceof NewlineComment) {
            // TODO look at how newlines are rendered, and maybe have a max of double new line
            return Optional.of(this);
        }
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        
        return "NewlineComment{}";
    }
    
}
