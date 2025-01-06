package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.mojang.serialization.Codec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Comment {
    
    public static final Codec<Comment> CODEC = CommentKind.CODEC.dispatch("kind", Comment::kind, CommentKind::codec);
    
    private final CommentKind kind;
    
    public Comment(CommentKind kind) {
        
        this.kind = kind;
    }
    
    public CommentKind kind() {
        
        return kind;
    }
    
    public boolean isEmpty() {
        
        return false;
    }
    
    /**
     * Attempts to collapse multiple comments of the same type into a single comment
     */
    public Optional<Comment> collapse(Comment other) {
        
        return Optional.empty();
    }
    
    /**
     * Merges multiple comments into a single comment of the same type, or a list comment if differing types
     */
    public Comment merge(Comment other) {
        
        return collapse(other).orElseGet(() -> {
            List<Comment> comments = new ArrayList<>();
            comments.add(this);
            comments.add(other);
            return new ListComment(comments);
        });
    }
    
}
