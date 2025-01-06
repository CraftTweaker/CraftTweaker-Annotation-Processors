package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;

public class Returns {
    
    public static final Codec<Returns> CODEC = Comment.CODEC.xmap(Returns::new, Returns::description);
    
    private final Comment description;
    
    public Returns(Comment description) {
        
        this.description = description;
    }
    
    public Comment description() {
        
        return description;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Returns returns = (Returns) o;
        return description.equals(returns.description);
    }
    
    @Override
    public int hashCode() {
        
        return description.hashCode();
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Returns{");
        sb.append("description=").append(description);
        sb.append('}');
        return sb.toString();
    }
    
}
