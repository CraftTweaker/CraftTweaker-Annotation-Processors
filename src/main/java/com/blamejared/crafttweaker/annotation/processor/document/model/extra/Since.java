package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;

public class Since {
    
    public static final Codec<Since> CODEC = Comment.CODEC.xmap(Since::new, Since::when);
    
    private final Comment when;
    
    public Since(Comment when) {
        
        this.when = when;
    }
    
    public Comment when() {
        
        return when;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Since{");
        sb.append("when=").append(when);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Since since = (Since) o;
        return when.equals(since.when);
    }
    
    @Override
    public int hashCode() {
        
        return when.hashCode();
    }
    
}
