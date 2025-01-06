package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;

public class See {
    
    public static final Codec<See> CODEC = Comment.CODEC.xmap(See::new, See::what);
    
    private final Comment what;
    
    public See(Comment what) {
        
        this.what = what;
    }
    
    public Comment what() {
        
        return what;
    }
    
    public See merge(See other) {
        
        return new See(what.merge(other.what));
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        See see = (See) o;
        return what.equals(see.what);
    }
    
    @Override
    public int hashCode() {
        
        return what.hashCode();
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("See{");
        sb.append("what=").append(what);
        sb.append('}');
        return sb.toString();
    }
    
}
