package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;

import java.util.Map;

public class ParameterComments {
    
    public static final Codec<ParameterComments> CODEC = Codec.unboundedMap(Codec.STRING, Comment.CODEC)
            .xmap(ParameterComments::new, ParameterComments::comments);
    
    private final Map<String, Comment> comments;
    
    public ParameterComments(Map<String, Comment> comments) {
        
        this.comments = comments;
    }
    
    public Map<String, Comment> comments() {
        
        return comments;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        ParameterComments that = (ParameterComments) o;
        return comments.equals(that.comments);
    }
    
    @Override
    public int hashCode() {
        
        return comments.hashCode();
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("ParameterComments{");
        sb.append("comments=").append(comments);
        sb.append('}');
        return sb.toString();
    }
    
}
