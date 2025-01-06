package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;

public class Obtention {
    
    public static final Codec<Obtention> CODEC = Comment.CODEC.xmap(Obtention::new, Obtention::method);
    
    private final Comment method;
    
    public Obtention(Comment method) {
        
        this.method = method;
    }
    
    public Comment method() {
        
        return method;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Obtention{");
        sb.append("method=").append(method);
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
        
        Obtention obtention = (Obtention) o;
        return method.equals(obtention.method);
    }
    
    @Override
    public int hashCode() {
        
        return method.hashCode();
    }
    
}
